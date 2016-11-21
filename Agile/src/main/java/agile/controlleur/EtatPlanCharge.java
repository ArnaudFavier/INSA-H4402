package agile.controlleur;

import agile.modele.DemandeLivraisons;
import agile.xml.DeserialiseurDemandeLivraisonsXML;

public class EtatPlanCharge extends EtatDefaut {

	public EtatPlanCharge() {
	}

	@Override
	public DemandeLivraisons chargerDemandeLivraisons(Controlleur controlleur) {
		DemandeLivraisons demandeLivraisons = null;
		try {
			demandeLivraisons = DeserialiseurDemandeLivraisonsXML.charger(controlleur.getPlan());
			if (demandeLivraisons != null) {
				controlleur.setEtatCourant(controlleur.etatDemandeLivraisonChargee);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return demandeLivraisons;
	}
}
