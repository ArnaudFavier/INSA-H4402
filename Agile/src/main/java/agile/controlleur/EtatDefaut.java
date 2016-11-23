package agile.controlleur;

import agile.modele.DemandeLivraisons;
import agile.modele.Livraison;
import agile.modele.Plan;
import agile.modele.Temps;
import agile.modele.Tournee;
import agile.xml.DeserialiseurDemandeLivraisonsXML;
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
			if (controlleur.getDemandeLivraisons() != null) {
				controlleur.setDemandeLivraisons(null);
			}
			controlleur.setEtatCourant(controlleur.etatPlanCharge);
		}

		return plan;
	}

	@Override
	public DemandeLivraisons chargerDemandeLivraisons(Controlleur controlleur) throws Exception {
		DemandeLivraisons demande = controlleur.getDemandeLivraisons();
		DemandeLivraisons DemandeLivraisonsACharger = DeserialiseurDemandeLivraisonsXML.charger(controlleur.getPlan());

		if (DemandeLivraisonsACharger != null) {
			demande = DemandeLivraisonsACharger;
			controlleur.setEtatCourant(controlleur.etatDemandeLivraisonChargee);
		}

		return demande;
	}

	@Override
	public Tournee calculerTournee(Controlleur controlleur) {
		return controlleur.getTournee();
	}

	@Override
	public void enregistrerFeuilleDeRoute(Controlleur controlleur) {

	}

	@Override
	public void ajouterLivraison(Controlleur controlleur, Livraison livraison) {
	}

	@Override
	public void modifierLivraison(Controlleur controlleur, int idLivraison, Temps debutPlage, Temps finPlage) {

	}

	@Override
	public void supprimerLivraison(Controlleur controlleur, Livraison livraison) {
	}

	@Override
	public void undo(Historique historique) {
	}

	@Override
	public void redo(Historique historique) {
	}

}