package agile.controlleur;

import agile.modele.Plan;
import agile.xml.DeserialiseurPlanXML;

public class EtatInitial extends EtatDefaut {

	public EtatInitial() {
	}

	@Override
	public void chargerPlan(Controlleur controlleur) {
		// TODO Auto-generated method stub

		try {
			Plan plan = DeserialiseurPlanXML.charger();
			System.out.println(plan);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}