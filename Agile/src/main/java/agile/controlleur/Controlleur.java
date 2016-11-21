package agile.controlleur;

import agile.modele.Plan;
import agile.xml.DeserialiseurPlanXML;

public class Controlleur {

	private Historique historique;
	private Etat etatCourant;

	// Instances associées à chaque etat possible du controleur
	protected final EtatInitial etatInitial = new EtatInitial();
	protected final EtatAjoutLivraison etatAjoutLivraison = new EtatAjoutLivraison();
	protected final EtatDemandeLivraisonChargee etatDemandeLivraisonChargee = new EtatDemandeLivraisonChargee();
	protected final EtatModifierLivraison etatModifierLivraison = new EtatModifierLivraison();
	protected final EtatPlanCharge etatPlanCharge = new EtatPlanCharge();
	protected final EtatTourneeCalculee etatTourneeCalculee = new EtatTourneeCalculee();

	public Controlleur() {
		historique = new Historique();
		etatCourant = etatInitial;
	}

	public void chargerPlan() {
		try {
			Plan plan = DeserialiseurPlanXML.charger();
			System.out.println(plan);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	// TODO : Implementer les événements (IHM etc), voir diagramme

}
