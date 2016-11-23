package agile.controlleur;

import agile.modele.DemandeLivraisons;

public class EtatInitial extends EtatDefaut {

	public EtatInitial() {
	}

	@Override
	public DemandeLivraisons chargerDemandeLivraisons(Controlleur controlleur) throws Exception {
		return controlleur.getDemandeLivraisons();
	}

}