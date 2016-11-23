package agile.modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agile.pathfinding.Djikstra;
import agile.pathfinding.TSP;
import agile.pathfinding.TSP1;

/**
 * Une demande de livraisons après avoir été traitée par un algo resolvant le
 * TSP
 */
public class Tournee {
    /**
     * La demande de livraisons à l'origine de ce résultat
     */
    private DemandeLivraisons demandeInitiale;

    /**
     * Toutes les livraisons non-ordonnées
     */
    private List<Livraison> livraisons;

    /**
     * La matrice de cout de déplacement d'un point de livraison à un autre
     */
    protected float[][] matriceCout;

    /**
     * La matrice des chemin allant d'un point de livraison à un autre
     */
    protected Chemin[][] matriceChemin;

    /**
     * La liste des chemins à suivre (ordonné) pour faire le TSP
     */
    private List<Chemin> cheminsTSP;

    /**
     * la liste des livraisons ordonnée pour le TSP
     */
    private List<Livraison> livraisonsTSP;

    public Tournee(DemandeLivraisons demandeInitiale) {
	this.demandeInitiale = demandeInitiale;
	this.livraisons = new ArrayList<>(demandeInitiale.getLivraisons());
    }

    /**
     * Calcule un cycle de hamilton et ordonnance la liste des livraisons en
     * fonction
     * 
     * @throws Exception
     */
    public void calculerTSP() throws Exception {
	calculerMatrices();
	// Calcul des durées
	int[] durees = new int[livraisons.size() + 1];
	// Entrepot
	durees[0] = 0;
	// Points de livraison
	for (int livrId1 = 0; livrId1 < livraisons.size(); livrId1++) {
	    durees[livrId1 + 1] = livraisons.get(livrId1).getDuree();
	}

	final int secondesHeureDepart = demandeInitiale.getEntrepot().getHeureDepart().getTotalSecondes();

	// Calcul des temps d'arrivée max
	float[] tempsMin = new float[livraisons.size() + 1];
	// Entrepot
	tempsMin[0] = Float.MIN_VALUE;
	// Points de livraison
	for (int livrId1 = 0; livrId1 < livraisons.size(); livrId1++) {
	    if (livraisons.get(livrId1).ContrainteDeTemps()) {
		tempsMin[livrId1 + 1] = livraisons.get(livrId1).getDebutPlage().getTotalSecondes()
			- secondesHeureDepart;
	    } else {
		tempsMin[livrId1 + 1] = Float.MIN_VALUE;
	    }
	}

	// Calcul des temps d'arrivée max
	float[] tempsMax = new float[livraisons.size() + 1];
	// Entrepot
	tempsMax[0] = Float.MAX_VALUE;
	// Points de livraison
	for (int livrId1 = 0; livrId1 < livraisons.size(); livrId1++) {

	    if (livraisons.get(livrId1).ContrainteDeTemps()) {
		tempsMax[livrId1 + 1] = livraisons.get(livrId1).getFinPlage().getTotalSecondes() - secondesHeureDepart;
	    } else {
		tempsMax[livrId1 + 1] = Float.MAX_VALUE;
	    }
	}

	TSP tsp = new TSP1();
	tsp.chercheSolution(10000, durees.length, matriceCout, durees, tempsMin, tempsMax);

	if (tsp.getTempsLimiteAtteint()) {
	    throw new Exception("Temps limite tsp atteint");
	}

	livraisonsTSP = new ArrayList<>(livraisons.size());
	cheminsTSP = new ArrayList<>(livraisons.size() + 1);
	float currTime = demandeInitiale.getEntrepot().getHeureDepart().getTotalSecondes();
	int idPrecedenteIntersection = 0;
	for (int i = 0; i < livraisons.size(); i++) {
	    int idCurrIntersection = tsp.getMeilleureSolution(i + 1);
	    Livraison livr = livraisons.get(idCurrIntersection - 1);
	    livraisonsTSP.add(livr);
	    Chemin chemin = matriceChemin[idPrecedenteIntersection][idCurrIntersection];
	    cheminsTSP.add(chemin);
	    currTime += chemin.getCout();
	    if (livr.ContrainteDeTemps() && currTime < livr.getDebutPlage().getTotalSecondes()) {
		livr.setTempsAttente(livr.getDebutPlage().getTotalSecondes() - currTime);
		currTime = livr.getDebutPlage().getTotalSecondes();
	    } else {
		livr.setTempsAttente(0);
	    }

	    currTime += livr.getDuree();
	    idPrecedenteIntersection = idCurrIntersection;
	}
	cheminsTSP.add(matriceChemin[idPrecedenteIntersection][0]);
    }

    /**
     * @return La matrice de coup entre chaque point de livraison
     */
    private void calculerMatrices() {
	ArrayList<Intersection> inters = new ArrayList<>(livraisons.size() + 1);
	inters.add(demandeInitiale.getEntrepot().getIntersection());
	for (Livraison livr : livraisons) {
	    inters.add(livr.getIntersection());
	}

	matriceChemin = new Chemin[inters.size()][inters.size()];
	matriceCout = new float[inters.size()][inters.size()];
	for (int intersId1 = 0; intersId1 < inters.size(); intersId1++) {
	    Intersection inters1 = inters.get(intersId1);
	    for (int intersId2 = 0; intersId2 < inters.size(); intersId2++) {
		Intersection inters2 = inters.get(intersId2);
		if (inters1 == inters2) {
		    matriceChemin[intersId1][intersId2] = new Chemin(new ArrayList<>(), new ArrayList<>(), 0f);
		    matriceCout[intersId1][intersId2] = 0f;
		} else {
		    matriceChemin[intersId1][intersId2] = Djikstra.plusCourtChemin(inters1, inters2);
		    matriceCout[intersId1][intersId2] = matriceChemin[intersId1][intersId2].getCout();
		}
	    }
	}
    }

    /**
     * @return La demande de livraisons à l'origine de ce résultat
     */
    public DemandeLivraisons getDemandeInitiale() {
	return demandeInitiale;
    }

    /**
     * @return Une vue non-modifiable de la liste des chemins à suivre (ordonné)
     *         pour faire le TSP
     */
    public List<Chemin> getCheminsTSP() {
	return Collections.unmodifiableList(cheminsTSP);
    }

    /**
     * @return Une vue non-modifiable de la liste des livraisons ordonnée pour
     *         le TSP
     */
    public List<Livraison> getLivraisonsTSP() {
	return Collections.unmodifiableList(livraisonsTSP);
    }

    /**
     * @return La liste ordonnée des intersections à parcourir pour effectuer le
     *         cycle complet
     */
    public List<Intersection> getListeIntersectionsTSP() {
	List<Intersection> intersectionsTSP = new ArrayList<>();
	intersectionsTSP.add(getDemandeInitiale().getEntrepot().getIntersection());
	for (Chemin chemin : cheminsTSP) {
	    List<Intersection> intersections = chemin.getIntersections();
	    for (int i = 1; i < intersections.size(); i++) {
		intersectionsTSP.add(intersections.get(i));
	    }
	}
	return intersectionsTSP;
    }

    public void supprimerLivraison(Livraison livraison) {
	// Pour supprimer la livraisons B entre A et C, on se contente de
	// l'enlever et on supprime les chemin AB et BC, qu'on remplace par AC

	// L'id de la livraison dans la liste ordonnée pour le tsp
	int idLivraisonTSP = livraisonsTSP.indexOf(livraison);

	if (idLivraisonTSP == -1) {
	    // Impossible de supprimer, livraison introuvable
	    return;
	}

	// L'id (dans la matrice des chemins) de l'intersection de
	// livraison/entrepot avant la livraison à supprimer
	int idPreInters;
	// Si c'est un entrepot
	if (idLivraisonTSP == 0) {
	    idPreInters = 0;
	} else {
	    Livraison livr = livraisonsTSP.get(idLivraisonTSP - 1);
	    idPreInters = livraisons.indexOf(livr) + 1;
	}

	// L'id (dans la matrice des chemins) de l'intersection de
	// livraison/entrepot après la livraison à supprimer
	int idPostInters;
	// Si c'est un entrepot
	if (idLivraisonTSP == livraisonsTSP.size() - 1) {
	    idPostInters = 0;
	} else {
	    Livraison livr = livraisonsTSP.get(idLivraisonTSP + 1);
	    idPostInters = livraisons.indexOf(livr) + 1;
	}

	// Obtenir le nouveau chemin dans la matrice
	Chemin nouveauChemin = matriceChemin[idPreInters][idPostInters];

	// Retirer les deux chemins avant et après
	// Le chemin avant
	cheminsTSP.remove(idLivraisonTSP);
	// Le chemin après
	cheminsTSP.remove(idLivraisonTSP);

	// On peut supprimer la livraison la liste des livraisons du TSP
	livraisonsTSP.remove(livraison);

	cheminsTSP.add(idLivraisonTSP, nouveauChemin);
    }

    public void ajouterLivraison(Livraison livraison) {
	float coutMin = Integer.MAX_VALUE;
	Chemin cheminAjout1 = null;
	Chemin cheminAjout2 = null;
	int posAjout = -1;

	// On ajoute la livraison à la fin dans une copie pour pouvoir
	// recalculer nos matrices
	Tournee copy = clone();
	copy.livraisons.add(livraison);
	copy.calculerMatrices();

	// L'id (dans la matrice des chemins) de l'intersection à ajouter
	int idInters = livraisons.size() + 1;

	// On regarde pour chaque id de livraison, le cout de la suppression du
	// chemin déjà existant et de l'ajout de la nouvelle livraison entre les
	// deux
	for (int i = 0; i <= livraisonsTSP.size(); i++) {
	    // L'id (dans la matrice des chemins) de l'intersection de
	    // livraison/entrepot avant la livraison à ajouter
	    int idPreInters;
	    // Si c'est un entrepot
	    if (i == 0) {
		idPreInters = 0;
	    } else {
		Livraison livr = livraisonsTSP.get(i - 1);
		idPreInters = livraisons.indexOf(livr) + 1;
	    }

	    // L'id (dans la matrice des chemins) de l'intersection de
	    // livraison/entrepot après la livraison à ajouter
	    int idPostInters;
	    // Si c'est un entrepot
	    if (i == livraisonsTSP.size()) {
		idPostInters = 0;
	    } else {
		Livraison livr = livraisonsTSP.get(i);
		idPostInters = livraisons.indexOf(livr) + 1;
	    }

	    Chemin cheminAvant = copy.matriceChemin[idPreInters][idInters];
	    Chemin cheminApres = copy.matriceChemin[idInters][idPostInters];
	    Chemin cheminSupp = cheminsTSP.get(i);

	    float currCout = cheminAvant.getCout() + cheminApres.getCout() - cheminSupp.getCout();

	    if (currCout < coutMin) {
		coutMin = currCout;
		cheminAjout1 = cheminAvant;
		cheminAjout2 = cheminApres;
		posAjout = i;
	    }
	}

	if (posAjout != -1) {
	    cheminsTSP.remove(posAjout);
	    cheminsTSP.add(posAjout, cheminAjout2);
	    cheminsTSP.add(posAjout, cheminAjout1);
	    livraisonsTSP.add(posAjout, livraison);

	    livraisons = copy.livraisons;
	    matriceChemin = copy.matriceChemin;
	    matriceCout = copy.matriceCout;
	}
    }

    /**
     * Permet de dupliquer la Tournee
     */
    @Override
    protected Tournee clone() {
	Tournee copy = new Tournee(demandeInitiale);

	// Cloner matriceChemin[][]
	copy.matriceChemin = new Chemin[matriceChemin.length][];
	for (int i = 0; i < matriceChemin.length; i++)
	    copy.matriceChemin[i] = matriceChemin[i].clone();

	// Cloner matriceCout[][]
	copy.matriceCout = new float[matriceCout.length][];
	for (int i = 0; i < matriceCout.length; i++)
	    copy.matriceCout[i] = matriceCout[i].clone();

	// Cloner cheminsTSP
	copy.cheminsTSP = new ArrayList<>(cheminsTSP);

	// Cloner cheminsTSP
	copy.livraisonsTSP = new ArrayList<>(livraisonsTSP);

	// Cloner livraisons
	copy.livraisons = new ArrayList<>(livraisons);

	return copy;
    }
}
