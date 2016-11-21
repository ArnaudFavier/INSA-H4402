package agile.controlleur;

import agile.modele.DemandeLivraisons;
import agile.xml.DeserialiseurDemandeLivraisonsXML;

public class EtatPlanCharge extends EtatDefaut {

	public EtatPlanCharge() {
	}

	@Override
	public void chargerDemandeLivraison(Controlleur controlleur) {
		try {

			/*
			 * DemandeLivraisons demandeLivraison =
			 * DeserialiseurDemandeLivraisonsXML.charger(plan);
			 * System.out.println(demandeLivraison);
			 */

			System.out.println("Charger demande livraison...");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}