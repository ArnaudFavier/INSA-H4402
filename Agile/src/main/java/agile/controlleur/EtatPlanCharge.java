package agile.controlleur;

import agile.modele.DemandeLivraisons;
import agile.xml.DeserialiseurDemandeLivraisonsXML;

public class EtatPlanCharge extends EtatDefaut {

	public EtatPlanCharge() {
	}

	@Override
	public void chargerDemandeLivraison(Controlleur controlleur) {
		try {
			DemandeLivraisons demandeLivraison = DeserialiseurDemandeLivraisonsXML.charger(controlleur.getPlan());
			System.out.println(demandeLivraison.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}