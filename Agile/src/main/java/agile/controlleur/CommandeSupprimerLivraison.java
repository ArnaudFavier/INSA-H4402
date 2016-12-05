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
	this.livraison = livraison;
	prevTournee = tournee.clone();
    }

    @Override
    public void doCde(Controlleur controlleur) {
	tournee.supprimerLivraison(livraison);
	controlleur.setTournee(tournee);

    }

    @Override
    public void undoCde(Controlleur controlleur) {
	controlleur.setTournee(prevTournee);
    }

}