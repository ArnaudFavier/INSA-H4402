package agile.controlleur;

import agile.modele.Livraison;
import agile.modele.Tournee;

public class CommandeAjouterLivraison implements Commande {

	private Tournee tournee;
	private Livraison livraison;

	public CommandeAjouterLivraison(Tournee tournee, Livraison livraison) {
		this.tournee = tournee;
		this.livraison = livraison;
	}

	@Override
	public void doCde() {
		tournee.ajouterLivraison(livraison);

	}

	@Override
	public void undoCde() {
		tournee.supprimerLivraison(livraison);

	}

}