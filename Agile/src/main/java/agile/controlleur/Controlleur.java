package agile.controlleur;

import agile.modele.DemandeLivraisons;
import agile.modele.Livraison;
import agile.modele.Plan;
import agile.modele.Temps;
import agile.modele.Tournee;

/**
 * Controlleur principal de l'application, chargé de faire le lien entre les
 * interactions de la vue et le modèle. Gère aussi l'ensemble des états de
 * l'application
 */
public class Controlleur {

    private Etat etatCourant;
    private Historique historique;
    private Plan plan;
    private DemandeLivraisons demandeLivraisons;
    private Tournee tournee;

    // Instances associées à chaque état possible du controleur
    protected final EtatInitial etatInitial = new EtatInitial();
    protected final EtatDemandeLivraisonChargee etatDemandeLivraisonChargee = new EtatDemandeLivraisonChargee();
    protected final EtatPlanCharge etatPlanCharge = new EtatPlanCharge();
    protected final EtatTourneeCalculee etatTourneeCalculee = new EtatTourneeCalculee();

    /**
     * Crée le controlleur de l'application
     */
    public Controlleur() {
	historique = new Historique();
	etatCourant = etatInitial;
    }

    /**
     * Change l'état courant du controlleur
     * 
     * @param etat
     *            le nouvel etat courant
     */
    protected void setEtatCourant(Etat etat) {
	etatCourant = etat;
    }

    /**
     * Methode appelee par la fenetre apres un clic sur le bouton "Ouvrir plan"
     * 
     * @return
     * @throws Exception
     */
    public Plan chargerPlan() throws Exception {
	Plan previousPlan = this.plan;
	plan = etatCourant.chargerPlan(this, historique);

	if (plan == null || plan == previousPlan) {
	    throw new Exception("Ouverture de plan annulée.");
	}

	return plan;
    }

    /**
     * Methode appelee par la fenetre apres un clic sur le bouton "Ouvrir
     * livraisons"
     * 
     * @throws Exception
     */
    public void chargerDemandeLivraisons() throws Exception {
	DemandeLivraisons previousDemandeLivraisons = this.demandeLivraisons;
	demandeLivraisons = etatCourant.chargerDemandeLivraisons(this, historique);

	if (demandeLivraisons == null || demandeLivraisons == previousDemandeLivraisons) {
	    throw new Exception("Ouverture de demande de livraisons annulée.");
	}

    }

    /**
     * Methode appelee par la fenetre apres un clic sur le bouton "Calculer
     * tournee"
     * 
     * @throws Exception
     */
    public void calculerTournee() throws Exception {
	tournee = etatCourant.calculerTournee(this);

	if (tournee == null) {
	    throw new Exception();
	}
    }

    /**
     * Methode appelee par la fenetre apres un clic sur le bouton "Exporter
     * tournee"
     * 
     * @throws Exception
     */
    public void enregistrerFeuilleDeRoute() throws Exception {
	etatCourant.enregistrerFeuilleDeRoute(this);
    }

    /**
     * Methode appelee par la fenetre apres un clic sur le bouton "Ajouter"
     * 
     * @param livraison
     */
    public boolean ajouterLivraison(Livraison livraison, boolean historisation) {
	boolean ajoutSucces = etatCourant.ajouterLivraison(this, livraison, historisation);
	return ajoutSucces;
    }

    /**
     * Methode appelee par la fenetre apres un clic sur le bouton "Modifier"
     * 
     * @param idLivraison
     * @param debutPlage
     * @param finPlage
     */
    public void modifierLivraison(Livraison livraison, Temps debutPlage, Temps finPlage) {
	etatCourant.modifierLivraison(this, livraison, debutPlage, finPlage);
    }

    /**
     * Methode appelee par la fenetre apres un clic sur le bouton "Supprimer"
     * 
     * @param livraison
     */
    public void supprimerLivraison(Livraison livraison) {
	etatCourant.supprimerLivraison(this, livraison);
    }

    /**
     * Methode appelee par la fenetre apres un clic sur le bouton "Undo"
     * 
     * @param historique
     */
    public void undo() {
	etatCourant.undo(this);
    }

    /**
     * Methode appelee par la fenetre apres un clic sur le bouton "Redo"
     * 
     * @param historique
     */
    public void redo() {
	etatCourant.redo(this);
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
