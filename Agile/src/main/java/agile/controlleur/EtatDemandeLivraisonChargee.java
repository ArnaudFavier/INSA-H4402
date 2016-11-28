package agile.controlleur;

import agile.modele.Tournee;

public class EtatDemandeLivraisonChargee extends EtatDefaut {

    public EtatDemandeLivraisonChargee() {
    }

    @Override
    public Tournee calculerTournee(Controlleur controlleur) {
	Tournee tournee = new Tournee(controlleur.getDemandeLivraisons());
	try {
	    tournee.calculerTSP();
	} catch (Exception e) {
	    e.printStackTrace();
	}

	if (tournee.getCheminsTSP() == null) {
	    return null;
	} else {
	    controlleur.setEtatCourant(controlleur.etatTourneeCalculee);
	    return tournee;
	}
    }

}