package agile.controlleur;

import agile.modele.Plan;

public class EtatDemandeLivraisonChargee extends EtatDefaut {

	public EtatDemandeLivraisonChargee() {
	}

	@Override
	public Plan chargerPlan(Controlleur controlleur, Historique historique) throws Exception {
		return controlleur.getPlan();
	}

}