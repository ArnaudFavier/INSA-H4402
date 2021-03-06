package agile.modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agile.pathfinding.Djikstra;
import agile.pathfinding.TSP;
import agile.pathfinding.TSP3;

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

	TSP tsp = new TSP3();
	tsp.chercheSolution(20000, durees.length, matriceCout, durees, tempsMin, tempsMax);

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
	    livr.setHeureArrivee((int) currTime);
	    if (livr.ContrainteDeTemps() && currTime < livr.getDebutPlage().getTotalSecondes()) {
		livr.setTempsAttente(livr.getDebutPlage().getTotalSecondes() - currTime + 60);
		currTime = livr.getDebutPlage().getTotalSecondes();
	    } else {
		livr.setTempsAttente(0);
	    }

	    currTime += livr.getDuree();
	    idPrecedenteIntersection = idCurrIntersection;
	}
	demandeInitiale.getEntrepot()
		.setHeureRetour(new Temps((int) (currTime + matriceChemin[idPrecedenteIntersection][0].getCout())));
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
	int heurePrecedent;
	// Si c'est un entrepot
	if (idLivraisonTSP == 0) {
	    idPreInters = 0;
	    heurePrecedent = demandeInitiale.getEntrepot().getHeureDepart().getTotalSecondes();
	} else {
	    Livraison livr = livraisonsTSP.get(idLivraisonTSP - 1);
	    idPreInters = livraisons.indexOf(livr) + 1;
	    heurePrecedent = livr.getHeureArrivee().getTotalSecondes() + (int) livr.getTempsAttente() + livr.getDuree();
	}

	// L'id (dans la matrice des chemins) de l'intersection de
	// livraison/entrepot après la livraison à supprimer
	int idPostInters;
	// Si c'est un entrepot
	if (idLivraisonTSP == livraisonsTSP.size() - 1) {
	    idPostInters = 0;
	    int nouveauTemps = heurePrecedent + (int) matriceChemin[idPreInters][idPostInters].getCout();
	    demandeInitiale.getEntrepot().setHeureRetour(new Temps(nouveauTemps));
	} else {
	    Livraison livr = livraisonsTSP.get(idLivraisonTSP + 1);
	    idPostInters = livraisons.indexOf(livr) + 1;

	    // Mettre à jour le temps d'attente de la livraison suivante

	    // float nouveauTemps =
	    // matriceChemin[idPreInters][idPostInters].getCout();
	    // float precedentTemps =
	    // matriceChemin[idPreInters][idLivraisonTSP].getCout()
	    // + matriceChemin[idLivraisonTSP][idPostInters].getCout();
	    // float diffTemps = precedentTemps - nouveauTemps;
	    int precedentTemps = livr.getHeureArrivee().getTotalSecondes();
	    int nouveauTemps = heurePrecedent + (int) matriceChemin[idPreInters][idPostInters].getCout();
	    livr.setHeureArrivee(nouveauTemps);

	    if (livr.ContrainteDeTemps()) {
		int nouveauTempsAttente = Math.max(0,
			livr.getDebutPlage().getTotalSecondes() - livr.getHeureArrivee().getTotalSecondes() + 60);
		livr.setTempsAttente(nouveauTempsAttente);
	    } else {
		int nouveauTempsAttente = Math.max(0, nouveauTemps - precedentTemps + 60);
		livr.setTempsAttente(nouveauTempsAttente);
	    }
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

    public void modifierLivraison(Livraison livraison, Temps debutPlage, Temps finPlage) {
	// L'id de la livraison dans la liste ordonnée pour le tsp
	int idLivraisonTSP = livraisonsTSP.indexOf(livraison);
	livraisonsTSP.get(idLivraisonTSP).setDebutPlage(debutPlage);
	livraisonsTSP.get(idLivraisonTSP).setFinPlage(finPlage);

	// TODO calculer la bonne tournee

    }

    /**
     * Ajoute une livraison à la tournee déjà calculée
     * 
     * @param livraison
     *            La livraison à ajouter
     * @return un boolean, vrai=succès de l'ajout, faux=erreur impossible ajout
     *         pour cette période
     */
    public boolean ajouterLivraison(Livraison livraison) {
	float coutMin = Integer.MAX_VALUE;
	Chemin cheminAjout1 = null;
	Chemin cheminAjout2 = null;
	int posAjout = -1;
	int tempsAttenteAdditionnel = 0;

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

	    Chemin cheminSupp = copy.matriceChemin[idPreInters][idPostInters];

	    float currCout = cheminAvant.getCout() + cheminApres.getCout() - cheminSupp.getCout();

	    int tempsAttenteAdd = 0;
	    // Si il y a un intervalle on regarde qu'on au bon moment
	    if (livraison.ContrainteDeTemps()) {
		// Pour prendre le temps d'arrive, on ajoute cheminAvant a
		// heureArrive du precedent ou heureDepart de l'entrepot
		int tempsArrivee;
		if (i == 0) {
		    tempsArrivee = demandeInitiale.getEntrepot().getHeureDepart().getTotalSecondes();
		} else {
		    tempsArrivee = livraisonsTSP.get(i - 1).getHeureArrivee().getTotalSecondes()
			    + livraisonsTSP.get(i - 1).getDuree();
		}
		tempsArrivee += cheminAvant.getCout();

		// Si on arrive après, c'est pas bon
		if (tempsArrivee > livraison.getFinPlage().getTotalSecondes()) {
		    continue;
		}
		// Si on arrive avant, on attend
		else if (tempsArrivee < livraison.getDebutPlage().getTotalSecondes()) {
		    tempsAttenteAdd = livraison.getDebutPlage().getTotalSecondes() - tempsArrivee;
		}
	    }
	    if (i == livraisonsTSP.size() && posAjout == -1) {
		coutMin = currCout;
		cheminAjout1 = cheminAvant;
		cheminAjout2 = cheminApres;
		posAjout = i;
		tempsAttenteAdditionnel = tempsAttenteAdd;
	    }

	    else if (i != livraisonsTSP.size() && currCout < coutMin
		    && currCout + livraison.getDuree() + tempsAttenteAdd <= livraisonsTSP.get(i).getTempsAttente()) {
		coutMin = currCout;
		cheminAjout1 = cheminAvant;
		cheminAjout2 = cheminApres;
		posAjout = i;
		tempsAttenteAdditionnel = tempsAttenteAdd;
	    }
	}

	if (posAjout != -1) {

	    // Si on ajoute pas à la fin, on reduit le temps d'attente du
	    // suivant et son heure d'arrive
	    if (posAjout != livraisonsTSP.size()) {
		Livraison livrSuivante = livraisonsTSP.get(posAjout);

		livrSuivante.setTempsAttente(
			livrSuivante.getTempsAttente() - coutMin - livraison.getDuree() - tempsAttenteAdditionnel);
		livrSuivante.setHeureArrivee((int) (livrSuivante.getHeureArrivee().getTotalSecondes() + coutMin
			+ livraison.getDuree() + tempsAttenteAdditionnel));
	    }
	    // Si le suivant est l'entrepot on met à jour l'heure de retour
	    else {
		demandeInitiale.getEntrepot().setHeureRetour(

			new Temps((int) (demandeInitiale.getEntrepot().getHeureRetour().getTotalSecondes() + coutMin
				+ livraison.getDuree() + tempsAttenteAdditionnel)));
	    }

	    livraison.setTempsAttente(tempsAttenteAdditionnel);

	    if (posAjout == 0) {

		livraison.setHeureArrivee((int) (demandeInitiale.getEntrepot().getHeureDepart().getTotalSecondes()
			+ cheminAjout1.getCout()));
	    } else {
		livraison.setHeureArrivee((int) (livraisonsTSP.get(posAjout - 1).getHeureArrivee().getTotalSecondes()
			+ livraisonsTSP.get(posAjout - 1).getDuree()

			+ cheminAjout1.getCout() + livraisonsTSP.get(posAjout - 1).getTempsAttente()));
	    }

	    cheminsTSP.remove(posAjout);
	    cheminsTSP.add(posAjout, cheminAjout2);
	    cheminsTSP.add(posAjout, cheminAjout1);
	    livraisonsTSP.add(posAjout, livraison);

	    livraisons = copy.livraisons;
	    matriceChemin = copy.matriceChemin;
	    matriceCout = copy.matriceCout;

	}
	return posAjout != -1;
    }

    /**
     * Permet de dupliquer la Tournee
     */
    @Override
    public Tournee clone() {
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
	copy.cheminsTSP = new ArrayList<>(cheminsTSP.size());

	for (int i = 0; i < cheminsTSP.size(); i++) {
	    copy.cheminsTSP.add(cheminsTSP.get(i).clone());
	}
	// Cloner cheminsTSP
	copy.livraisonsTSP = new ArrayList<>(livraisonsTSP.size());

	for (int i = 0; i < livraisonsTSP.size(); i++) {
	    copy.livraisonsTSP.add(livraisonsTSP.get(i).clone());
	}

	// Cloner livraisons
	copy.livraisons = new ArrayList<>(livraisons.size());

	for (int i = 0; i < livraisons.size(); i++) {
	    copy.livraisons.add(livraisons.get(i).clone());
	}

	return copy;
    }
}
