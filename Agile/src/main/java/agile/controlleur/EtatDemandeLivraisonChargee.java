package agile.controlleur;

import agile.modele.Chemin;
import agile.modele.DemandeLivraisons;
import agile.modele.Livraison;
import agile.pathfinding.Djikstra;

public class EtatDemandeLivraisonChargee extends EtatDefaut {

	public EtatDemandeLivraisonChargee() {
	}

	@Override
	public Chemin calculerTournee(Controlleur controlleur) {
		DemandeLivraisons demandeLivraison = controlleur.getDemandeLivraisons();
		Chemin chemin = controlleur.getChemin();

		if (chemin == null) {
			for (Livraison livraison1 : demandeLivraison.getLivraisons()) {
				for (Livraison livraison2 : demandeLivraison.getLivraisons()) {
					if (livraison1 != livraison2) {
						chemin = Djikstra.plusCourtChemin(livraison1.getIntersection(), livraison2.getIntersection());
					}
				}
			}
		}
		if (chemin != null) {
			controlleur.setEtatCourant(controlleur.etatTourneeCalculee);
		}
		return chemin;
	}

}