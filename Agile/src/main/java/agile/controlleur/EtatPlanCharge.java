package agile.controlleur;

import agile.modele.DemandeLivraisons;
import agile.xml.DeserialiseurDemandeLivraisonsXML;

public class EtatPlanCharge extends EtatDefaut {

    public EtatPlanCharge() {
    }

    @Override
    public DemandeLivraisons chargerDemandeLivraisons(Controlleur controlleur, Historique historique) throws Exception {
	DemandeLivraisons demandeLivraisons = null;
	demandeLivraisons = DeserialiseurDemandeLivraisonsXML.charger(controlleur.getPlan());

	if (demandeLivraisons != null) {
	    controlleur.setEtatCourant(controlleur.etatDemandeLivraisonChargee);
	}

	return demandeLivraisons;
    }
}
