package agile.controlleur;

import agile.modele.Livraison;
import agile.modele.Tournee;

public class CommandeSupprimerLivraison implements Commande {

    private Tournee tournee;
    private Livraison livraison;

    /**
     * Cree la commande qui permet de supprimer une livraison de la tournee
     * 
     * @param tournee
     * @param livraison
     */
    public CommandeSupprimerLivraison(Tournee tournee, Livraison livraison) {
	this.tournee = tournee;
	this.livraison = livraison;
    }

    @Override
    public void doCde() {
	tournee.supprimerLivraison(livraison);
    }

    @Override
    public void undoCde() {
	tournee.ajouterLivraison(livraison);

    }

}