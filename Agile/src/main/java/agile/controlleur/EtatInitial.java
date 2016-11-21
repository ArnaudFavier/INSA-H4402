package agile.controlleur;

import agile.modele.Plan;
import agile.xml.DeserialiseurPlanXML;

public class EtatInitial extends EtatDefaut {

	public EtatInitial() {
	}

	@Override
	public Plan chargerPlan(Controlleur controlleur) {
		// TODO Auto-generated method stub

		try {
			Plan plan = DeserialiseurPlanXML.charger();

			if (plan != null) {
				System.out.println(plan);
				controlleur.setEtatCourant(controlleur.etatPlanCharge);
			}
			return plan;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}

}