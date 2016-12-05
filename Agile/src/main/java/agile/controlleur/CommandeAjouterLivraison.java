package agile.controlleur;

import agile.modele.Livraison;
import agile.modele.Tournee;

public class CommandeAjouterLivraison implements Commande {

    private Tournee tournee;
    private Tournee prevTournee;
    private Livraison livraison;
    private boolean success, alreadyAdd;

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
	success = alreadyAdd = false;

    }

    @Override
    public void doCde(Controlleur controlleur) {
	if (!alreadyAdd) {
	    success = tournee.ajouterLivraison(livraison);
	    if (isSuccess()) {
		alreadyAdd = true;
	    }
	}
	controlleur.setTournee(tournee);
    }

    @Override
    public void undoCde(Controlleur controlleur) {
	if (isSuccess()) {
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