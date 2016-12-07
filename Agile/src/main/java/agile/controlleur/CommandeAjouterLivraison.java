package agile.controlleur;

import agile.modele.Livraison;
import agile.modele.Temps;
import agile.modele.Tournee;

public class CommandeAjouterLivraison implements Commande {

    protected Tournee tournee;
    protected Tournee prevTournee;
    protected Livraison livraison;
    protected boolean success, alreadyAdd;
    protected int prevHeureRetourEntrepot, heureRetourEntrepot;

    /**
     * Cree la commande qui permet d'ajouter une livraison à la tournee
     * 
     * @param tournee
     * @param livraison
     */
    public CommandeAjouterLivraison(Tournee tournee, Livraison livraison) {
	this.tournee = tournee;
	this.livraison = livraison;
	prevTournee = tournee.clone();
	prevHeureRetourEntrepot = tournee.getDemandeInitiale().getEntrepot().getHeureRetour().getTotalSecondes();
	success = alreadyAdd = false;

    }

    @Override
    public void doCde(Controlleur controlleur) {
	if (!alreadyAdd) {
	    success = tournee.ajouterLivraison(livraison);
	    if (isSuccess()) {
		alreadyAdd = true;
		controlleur.setTournee(tournee);
		heureRetourEntrepot = controlleur.getTournee().getDemandeInitiale().getEntrepot().getHeureRetour()
			.getTotalSecondes();
	    }
	} else {
	    Temps heure = new Temps(heureRetourEntrepot);

	    System.err.println(heure.getTimeString());
	    tournee.getDemandeInitiale().getEntrepot().setHeureRetour(heure);
	}
	controlleur.setTournee(tournee);
    }

    @Override
    public void undoCde(Controlleur controlleur) {
	if (isSuccess()) {
	    Temps heure = new Temps(prevHeureRetourEntrepot);
	    prevTournee.getDemandeInitiale().getEntrepot().setHeureRetour(heure);
	    controlleur.setTournee(prevTournee);
	}
    }

    /**
     * @return vrai si l'ajout a fonctionné, faux sinon (contrainte de plage
     *         horaire trop restrictive). A appeller après un doCde
     */
    public boolean isSuccess() {
	return success;
    }

}