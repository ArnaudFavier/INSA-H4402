package agile.controlleur;

import agile.modele.DemandeLivraisons;
import agile.modele.Livraison;
import agile.modele.Plan;
import agile.modele.Temps;
import agile.modele.Tournee;
import agile.xml.DeserialiseurDemandeLivraisonsXML;
import agile.xml.DeserialiseurPlanXML;

public abstract class EtatDefaut implements Etat {
    // Definition des comportements par defaut des methodes

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
    public void enregistrerFeuilleDeRoute(Controlleur controlleur) throws Exception {
    }

    @Override
    public boolean ajouterLivraison(Controlleur controlleur, Livraison livraison, boolean historisation) {
	return false;
    }

    @Override
    public void modifierLivraison(Controlleur controlleur, Livraison livraison, Temps debutPlage, Temps finPlage) {

    }

    @Override
    public void supprimerLivraison(Controlleur controlleur, Livraison livraison) {
    }

    @Override
    public void undo(Controlleur controlleur) {
    }

    @Override
    public void redo(Controlleur controlleur) {
    }

}