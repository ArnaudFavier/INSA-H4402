package agile.modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public final class Djikstra {
    private Djikstra() {

    }

    public static Chemin plusCourtChemin(Intersection inter1, Intersection inter2) {
	PriorityQueue<DijstraNoeud> open = new PriorityQueue<>();
	HashMap<Integer, DijstraNoeud> checked = new HashMap<>();
	open.add(new DijstraNoeud(0f, inter1, null, null));

	// Si pendant la recherche on a une distance plus grande que celle ci, pas besoin
	// d'ajouter le sommet à la liste des open
	float distanceMinToEnd = Float.POSITIVE_INFINITY;

	while (!open.isEmpty()) {
	    DijstraNoeud currentInter = open.poll();

	    if (currentInter.inter == inter2 && currentInter.distanceOrigine < distanceMinToEnd) {
		distanceMinToEnd = currentInter.distanceOrigine;
	    }

	    List<Troncon> tronconsVoisins = currentInter.inter.getTroncons();
	    
	    for (Troncon troncon : tronconsVoisins) {
		// Mettre a jour les distances des voisins et predecesseurs si
		// distance est inferieure a celle déjà existante
		// Si pas existant, alors créer
		Intersection interVoisin = troncon.getIntersections()[1];
		DijstraNoeud voisin = checked.get(interVoisin.hashCode());
		
		if (voisin == null) {
		    if (currentInter.distanceOrigine + troncon.getCout() < distanceMinToEnd) {
			voisin = new DijstraNoeud(currentInter.distanceOrigine + troncon.getCout(), interVoisin,
				currentInter.inter, troncon);
			open.add(voisin);
			checked.put(voisin.hashCode(), voisin);
		    }
		} else if (currentInter.distanceOrigine + troncon.getCout() < voisin.distanceOrigine) {
		    voisin.distanceOrigine = currentInter.distanceOrigine + troncon.getCout();
		    voisin.predecesseur = currentInter.inter;
		    voisin.tronconPredecesseur = troncon;
		    open.add(voisin);

		    checked.put(voisin.hashCode(), voisin);
		}
	    }
	}

	DijstraNoeud currNoeudChemin = checked.get(inter2.hashCode());
	float cout = currNoeudChemin.distanceOrigine;
	List<Troncon> cheminTroncons = new ArrayList<>();
	List<Intersection> cheminInters = new ArrayList<>();
	cheminInters.add(currNoeudChemin.inter);
	while (currNoeudChemin != null && currNoeudChemin.predecesseur != null && currNoeudChemin.inter != inter1) {
	    cheminTroncons.add(currNoeudChemin.tronconPredecesseur);
	    cheminInters.add(currNoeudChemin.predecesseur);
	    currNoeudChemin = checked.get(currNoeudChemin.predecesseur.hashCode());
	}
	Collections.reverse(cheminTroncons);
	Collections.reverse(cheminInters);

	return new Chemin(cheminTroncons, cheminInters, cout);
    }

    private static class DijstraNoeud implements Comparable<DijstraNoeud> {
	Float distanceOrigine;
	Intersection inter;
	Intersection predecesseur;
	Troncon tronconPredecesseur;

	public DijstraNoeud(Float distanceOrigine, Intersection inter, Intersection predecesseur,
		Troncon tronconPredecesseur) {
	    this.distanceOrigine = distanceOrigine;
	    this.inter = inter;
	    this.predecesseur = predecesseur;
	    this.tronconPredecesseur = tronconPredecesseur;
	}

	@Override
	public int compareTo(DijstraNoeud noeud) {
	    return Float.compare(distanceOrigine, noeud.distanceOrigine);
	}

	@Override
	public int hashCode() {
	    return inter.hashCode();
	}

	@Override
	public String toString() {
	    return "DijstraNoeud [distanceOrigine=" + distanceOrigine + ", inter=" + inter + ", predecesseur="
		    + predecesseur + ", tronconPredecesseur=" + tronconPredecesseur + "]";
	}
	
    }
}
