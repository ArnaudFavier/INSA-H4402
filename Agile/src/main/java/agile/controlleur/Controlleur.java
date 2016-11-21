package agile.controlleur;

import agile.vue.Fenetre;

public class Controlleur {

	private Fenetre fenetre;
	private Historique historique;
	private Etat etatCourant;

	// Instances associées à chaque etat possible du controleur
	protected final EtatInitial etatInitial = new EtatInitial();
	protected final EtatAjoutLivraison etatAjoutLivraison = new EtatAjoutLivraison();
	protected final EtatDemandeLivraisonChargee etatDemandeLivraisonChargee = new EtatDemandeLivraisonChargee();
	protected final EtatModifierLivraison etatModifierLivraison = new EtatModifierLivraison();
	protected final EtatPlanCharge etatPlanCharge = new EtatPlanCharge();
	protected final EtatTourneeCalculee etatTourneeCalculee = new EtatTourneeCalculee();

	public Controlleur(Fenetre fenetre) {
		this.fenetre = fenetre;
		historique = new Historique();
		etatCourant = etatInitial;
	}

	// TODO : Implementer les événements (IHM etc), voir diagramme

}
