package agile.controlleur;

import agile.modele.Plan;

public class Controlleur {

	private Historique historique;
	private Etat etatCourant;
	private Plan plan;

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
		plan = etatCourant.chargerPlan(this);
		System.out.println("Controlleur:: chargerPlan : " + plan);
		System.out.println(etatCourant.toString());
	}

	public void chargerDemandeLivraison(Controlleur controlleur) {
		etatCourant.chargerDemandeLivraison(this);
	}

	public void calculerTournee(Controlleur controlleur) {
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

}
