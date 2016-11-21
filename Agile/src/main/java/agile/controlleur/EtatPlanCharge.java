package agile.controlleur;

public class EtatPlanCharge extends EtatDefaut {

	public EtatPlanCharge() {
	}

	@Override
	public void chargerDemandeLivraison(Controlleur controlleur) {
		try {
			System.out.println("Charger demande livraison...");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}