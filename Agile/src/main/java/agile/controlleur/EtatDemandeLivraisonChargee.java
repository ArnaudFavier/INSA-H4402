package agile.controlleur;

import agile.modele.Plan;

public class EtatDemandeLivraisonChargee extends EtatDefaut {

	public EtatDemandeLivraisonChargee() {
	}

	@Override
	public Plan chargerPlan(Controlleur controlleur) {
		return controlleur.getPlan();
	}

}