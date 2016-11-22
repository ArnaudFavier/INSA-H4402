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
public class DemandeLivraisonsTraitee {
    /**
     * La demande de livraisons à l'origine de ce résultat
     */
    private DemandeLivraisons demandeInitiale;

    /**
     * La matrice de cout de déplacement d'un point de livraison à un autre
     */
    private float[][] matriceCout;

    /**
     * La matrice des chemin allant d'un point de livraison à un autre
     */
    private Chemin[][] matriceChemin;

    /**
     * La liste des chemins à suivre (ordonné) pour faire le TSP
     */
    private List<Chemin> cheminsTSP;

    /**
     * la liste des livraisons ordonnée pour le TSP
     */
    private List<Livraison> livraisonsTSP;

    public DemandeLivraisonsTraitee(DemandeLivraisons demandeInitiale) {
	this.demandeInitiale = demandeInitiale;
    }

    /**
     * Calcule un cycle de hamilton et ordonnance la liste des livraisons en
     * fonction
     */
    public void calculerTSP() {
	calculerMatrices();

	List<Livraison> livraisons = demandeInitiale.getLivraisons();
	int[] durees = new int[livraisons.size() + 1];
	// Entrepot
	durees[0] = 0;
	// Points de livraison
	for (int livrId1 = 0; livrId1 < livraisons.size(); livrId1++) {
	    durees[livrId1 + 1] = livraisons.get(livrId1).getDuree();
	}
	TSP tsp = new TSP1();
	tsp.chercheSolution(10000, durees.length, matriceCout, durees);

	livraisonsTSP = new ArrayList<>(livraisons.size());
	cheminsTSP = new ArrayList<>(livraisons.size() + 1);
	int idPrecedenteIntersection = 0;
	for (int i = 0; i < livraisons.size(); i++) {
	    int idCurrIntersection = tsp.getMeilleureSolution(i + 1);
	    Livraison livr = livraisons.get(idCurrIntersection - 1);
	    livraisonsTSP.add(livr);
	    cheminsTSP.add(matriceChemin[idPrecedenteIntersection][idCurrIntersection]);

	    idPrecedenteIntersection = idCurrIntersection;
	}
	cheminsTSP.add(matriceChemin[idPrecedenteIntersection][0]);
    }

    /**
     * @return La matrice de coup entre chaque point de livraison
     */
    private void calculerMatrices() {
	List<Livraison> livraisons = demandeInitiale.getLivraisons();
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
     * @return La liste ordonnée des intersections à parcourir pour effectuer le cycle complet
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
}
