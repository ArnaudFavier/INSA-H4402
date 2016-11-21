package agile.controlleur;

import agile.modele.DemandeLivraisons;
import agile.modele.Plan;
import agile.xml.DeserialiseurPlanXML;

public abstract class EtatDefaut implements Etat {

	/**
	 * Default constructor
	 */
	public EtatDefaut() {
	}

	@Override
	public Plan chargerPlan(Controlleur controlleur, Historique historique) throws Exception {
		Plan plan = controlleur.getPlan();
		Plan planACharger = DeserialiseurPlanXML.charger();

		if (planACharger != null) {
			plan = planACharger;
			historique.reset();
			controlleur.setEtatCourant(controlleur.etatPlanCharge);
		}

		return plan;
	}

	@Override
	public DemandeLivraisons chargerDemandeLivraisons(Controlleur controlleur) throws Exception {
		System.out.println("Chargement livraisons...");
		return controlleur.getDemandeLivraisons();
	}

	@Override
	public void calculerTournee(Controlleur controlleur) {
	}

	@Override
	public void genererFeuilleDeRoute(Controlleur controlleur) {
	}

	@Override
	public void enregistrerFeuilleDeRoute(Controlleur controlleur) {
	}

	@Override
	public void ajouterLivraison(Controlleur controlleur) {
	}

	@Override
	public void modifierLivraison(Controlleur controlleur) {
	}

	@Override
	public void undo(Historique historique) {
	}

	@Override
	public void redo(Historique historique) {
	}

}