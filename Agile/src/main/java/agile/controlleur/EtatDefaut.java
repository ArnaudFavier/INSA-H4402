package agile.controlleur;

import agile.modele.Plan;
import agile.xml.DeserialiseurPlanXML;

public abstract class EtatDefaut implements Etat {

	/**
	 * Default constructor
	 */
	public EtatDefaut() {
	}

	@Override
	public Plan chargerPlan(Controlleur controlleur) {
		Plan plan = controlleur.getPlan();

		try {
			Plan planACharger = DeserialiseurPlanXML.charger();

			if (planACharger != null) {
				plan = planACharger;
				controlleur.setEtatCourant(controlleur.etatPlanCharge);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return plan;

	}

	@Override
	public void chargerDemandeLivraison(Controlleur controlleur) {
		// TODO Auto-generated method stub

	}

	@Override
	public void calculerTournee(Controlleur controlleur) {
		// TODO Auto-generated method stub

	}

	@Override
	public void genererFeuilleDeRoute(Controlleur controlleur) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enregistrerFeuilleDeRoute(Controlleur controlleur) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ajouterLivraison(Controlleur controlleur) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modifierLivraison(Controlleur controlleur) {
		// TODO Auto-generated method stub

	}

	@Override
	public void undo(Historique historique) {
		// TODO Auto-generated method stub

	}

	@Override
	public void redo(Historique historique) {
		// TODO Auto-generated method stub

	}

}