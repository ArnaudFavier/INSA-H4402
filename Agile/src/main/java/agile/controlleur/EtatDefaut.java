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
	public Plan chargerPlan(Controlleur controlleur, Historique historique) {
		Plan plan = controlleur.getPlan();

		try {
			Plan planACharger = DeserialiseurPlanXML.charger();

			if (planACharger != null) {
				plan = planACharger;
				historique.reset();
				controlleur.setEtatCourant(controlleur.etatPlanCharge);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return plan;

	}

	@Override
	public DemandeLivraisons chargerDemandeLivraisons(Controlleur controlleur) {
		System.out.println("Chargement livraisons...");
		return controlleur.getDemandeLivraisons();

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