package agile.controlleur;

import agile.modele.Tournee;

public class EtatDemandeLivraisonChargee extends EtatDefaut {

	public EtatDemandeLivraisonChargee() {
	}

	@Override
	public Tournee calculerTournee(Controlleur controlleur) {
		Tournee tournee = new Tournee(controlleur.getDemandeLivraisons());
		tournee.calculerTSP();

		if (tournee.getCheminsTSP() == null) {
			return null;
		} else {
			controlleur.setEtatCourant(controlleur.etatTourneeCalculee);
			return tournee;
		}
	}

}