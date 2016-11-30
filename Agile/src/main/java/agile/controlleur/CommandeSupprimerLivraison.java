package agile.controlleur;

import agile.modele.Livraison;
import agile.modele.Tournee;

public class CommandeSupprimerLivraison implements Commande {

    private Tournee tournee;
    private Livraison livraison;
    private Tournee prevTournee;

    /**
     * Cree la commande qui permet de supprimer une livraison de la tournee
     * 
     * @param tournee
     * @param livraison
     */
    public CommandeSupprimerLivraison(Tournee tournee, Livraison livraison) {
	this.tournee = tournee;
	this.prevTournee = tournee.clone();
	this.livraison = livraison;
    }

    @Override
    public void doCde() {
	prevTournee = tournee.clone();
	tournee.supprimerLivraison(livraison);

    }

    @Override
    public void undoCde() {
	tournee = prevTournee;

    }

}