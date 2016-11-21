package agile.controlleur;

import agile.modele.DemandeLivraisons;
import agile.modele.Plan;

public class Controlleur {

	private Etat etatCourant;
	private Historique historique;
	private Plan plan;
	private DemandeLivraisons demandeLivraisons;

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

	protected void setEtatCourant(Etat etat) {
		etatCourant = etat;
	}

	public void chargerPlan(Controlleur controlleur) {
		plan = etatCourant.chargerPlan(this, historique);
		System.out.println("Controlleur:: chargerPlan : " + plan);
		System.out.println(etatCourant.toString());
	}

	public void chargerDemandeLivraisons(Controlleur controlleur) {
		demandeLivraisons = etatCourant.chargerDemandeLivraisons(this);
		System.out.println("Controlleur:: chargerDemandeLivraisons : " + demandeLivraisons);
		System.out.println(etatCourant.toString());

	}

	public void calculerTournee(Controlleur controlleur) {
		// TODO
		etatCourant.calculerTournee(this);
	}

	public void genererFeuilleDeRoute(Controlleur controlleur) {
		etatCourant.genererFeuilleDeRoute(this);
	}

	public void enregistrerFeuilleDeRoute(Controlleur controlleur) {
		etatCourant.enregistrerFeuilleDeRoute(this);
	}

	public void ajouterLivraison(Controlleur controlleur) {
		etatCourant.ajouterLivraison(this);
	}

	public void modifierLivraison(Controlleur controlleur) {
		etatCourant.modifierLivraison(this);
	}

	public void undo(Historique historique) {
		etatCourant.undo(historique);
	}

	public void redo(Historique historique) {
		etatCourant.redo(historique);
	}

	public Plan getPlan() {
		return this.plan;
	}

	public Historique getHistorique() {
		return this.historique;
	}

	public DemandeLivraisons getDemandeLivraisons() {
		return this.demandeLivraisons;
	}

}
