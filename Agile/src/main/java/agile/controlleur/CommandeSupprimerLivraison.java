package agile.controlleur;

import agile.modele.Livraison;
import agile.modele.Temps;
import agile.modele.Tournee;

public class CommandeSupprimerLivraison implements Commande {

    private Tournee tournee;
    private Livraison livraison;
    private Tournee prevTournee;
    private int prevHeureRetourEntrepot, heureRetourEntrepot;
    private boolean alreadyChangeHour;

    /**
     * Cree la commande qui permet de supprimer une livraison de la tournee
     * 
     * @param tournee
     * @param livraison
     */
    public CommandeSupprimerLivraison(Tournee tournee, Livraison livraison) {
	this.tournee = tournee;
	this.livraison = livraison;
	alreadyChangeHour = false;
	prevTournee = tournee.clone();
	prevHeureRetourEntrepot = tournee.getDemandeInitiale().getEntrepot().getHeureRetour().getTotalSecondes();
    }

    @Override
    public void doCde(Controlleur controlleur) {
	if (!alreadyChangeHour) {
	    tournee.supprimerLivraison(livraison);
	    alreadyChangeHour = true;
	    controlleur.setTournee(tournee);
	    heureRetourEntrepot = controlleur.getTournee().getDemandeInitiale().getEntrepot().getHeureRetour()
		    .getTotalSecondes();

	} else {
	    Temps heure = new Temps(heureRetourEntrepot);

	    System.err.println(heure.getTimeString());
	    tournee.getDemandeInitiale().getEntrepot().setHeureRetour(heure);
	}
	controlleur.setTournee(tournee);

    }

    @Override
    public void undoCde(Controlleur controlleur) {
	Temps heure = new Temps(prevHeureRetourEntrepot);
	prevTournee.getDemandeInitiale().getEntrepot().setHeureRetour(heure);
	controlleur.setTournee(prevTournee);
    }

}