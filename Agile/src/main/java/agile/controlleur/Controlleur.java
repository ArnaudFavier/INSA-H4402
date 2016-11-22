package agile.controlleur;

import agile.modele.DemandeLivraisons;
import agile.modele.Plan;
import agile.modele.Tournee;

public class Controlleur {

	private Etat etatCourant;
	private Historique historique;
	private Plan plan;
	private DemandeLivraisons demandeLivraisons;
	private Tournee tournee;

	// Instances associées à chaque état possible du controleur
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

	public void chargerPlan(Controlleur controlleur) throws Exception {
		plan = etatCourant.chargerPlan(this, historique);

		if (plan == null) {
			throw new Exception();
		}

		System.out.println("Controlleur:: chargerPlan : " + plan);
		System.out.println(etatCourant.toString());
	}

	public void chargerDemandeLivraisons(Controlleur controlleur) throws Exception {
		demandeLivraisons = etatCourant.chargerDemandeLivraisons(this);
		System.out.println("Controlleur:: chargerDemandeLivraisons : " + demandeLivraisons);
		System.out.println(etatCourant.toString());

		if (demandeLivraisons == null) {
			throw new Exception();
		}

	}

	public void calculerTournee(Controlleur controlleur) throws Exception {
		tournee = etatCourant.calculerTournee(this);
		System.out.println("Controlleur:: calculerTournee : " + tournee);
		System.out.println(etatCourant.toString());

		if (tournee == null) {
			throw new Exception();
		}
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

	public void setDemandeLivraisons(DemandeLivraisons demandeLivraisons) {
		this.demandeLivraisons = demandeLivraisons;
	}

	public Tournee getTournee() {
		return tournee;
	}

	public void setTournee(Tournee tournee) {
		this.tournee = tournee;
	}

}
