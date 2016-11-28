package agile.controlleur;

import agile.modele.DemandeLivraisons;
import agile.modele.Livraison;
import agile.modele.Plan;
import agile.modele.Temps;
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

    public Plan chargerPlan() throws Exception {
	Plan previousPlan = this.plan;
	plan = etatCourant.chargerPlan(this, historique);

	if (plan == null || plan == previousPlan) {
	    throw new Exception("Ouverture de plan annulée.");
	}

	System.out.println("Controlleur:: chargerPlan : " + plan);
	System.out.println(etatCourant.toString());

	return plan;
    }

    public void chargerDemandeLivraisons() throws Exception {
	DemandeLivraisons previousDemandeLivraisons = this.demandeLivraisons;
	demandeLivraisons = etatCourant.chargerDemandeLivraisons(this);
	System.out.println("Controlleur:: chargerDemandeLivraisons : " + demandeLivraisons);
	System.out.println(etatCourant.toString());

	if (demandeLivraisons == null || demandeLivraisons == previousDemandeLivraisons) {
	    throw new Exception("Ouverture de demande de livraisons annulée.");
	}

    }

    public void calculerTournee() throws Exception {
	tournee = etatCourant.calculerTournee(this);
	System.out.println("Controlleur:: calculerTournee : " + tournee);
	System.out.println(etatCourant.toString());

	if (tournee == null) {
	    throw new Exception();
	}
    }

    public void enregistrerFeuilleDeRoute() throws Exception {
	etatCourant.enregistrerFeuilleDeRoute(this);
    }

    public void ajouterLivraison(Livraison livraison) {
	etatCourant.ajouterLivraison(this, livraison);

	// Gestion de l'historique
	CommandeAjouterLivraison cAjouterLivraison = new CommandeAjouterLivraison(tournee, livraison);
	historique.ajoute(cAjouterLivraison);
    }

    public void modifierLivraison(int idLivraison, Temps debutPlage, Temps finPlage) {
	etatCourant.modifierLivraison(this, idLivraison, debutPlage, finPlage);
	Livraison l = null;
	for (Livraison livraison : tournee.getLivraisonsTSP()) {
	    if (livraison.getIntersection().getId() == idLivraison) {
		l = livraison;
		break;
	    }
	}

	// Gestion de l'historique
	CommandeModifierLivraison cModifierLivraison = new CommandeModifierLivraison(l, debutPlage, finPlage);
	historique.ajoute(cModifierLivraison);

    }

    public void supprimerLivraison(Livraison livraison) {
	etatCourant.supprimerLivraison(this, livraison);

	CommandeSupprimerLivraison cSupprimerLivraison = new CommandeSupprimerLivraison(tournee, livraison);
	historique.ajoute(cSupprimerLivraison);
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
