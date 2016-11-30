package agile.controlleur;

import agile.modele.Livraison;
import agile.modele.Tournee;

public class CommandeAjouterLivraison implements Commande {

    private Tournee tournee;
    private Livraison livraison;
    private boolean success;

    /**
     * Cree la commande qui permet d'ajouter une livraison à la tournee
     * 
     * @param tournee
     * @param livraison
     */
    public CommandeAjouterLivraison(Tournee tournee, Livraison livraison) {
	this.tournee = tournee;
	this.livraison = livraison;
    }

    @Override
    public void doCde() {
	success = tournee.ajouterLivraison(livraison);
    }

    @Override
    public void undoCde(Controlleur controlleur) {
	if (isSuccess()) {
	    tournee.supprimerLivraison(livraison);
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