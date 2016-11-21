package agile.modele;

import java.util.List;

public class Tournee {

	private List<Troncon> troncons;
	private List<Livraison> livraisons;

	public Tournee() {
	}

	public int dureeTotale;

	public void ajouterLivraison(Livraison livraison) {
		// TODO
		livraisons.add(livraison);

	}

	public void supprimerLivraison(Livraison livraison) {
		// TODO
		livraisons.remove(livraisons.indexOf(livraison));

	}

	public void modifierLivraison(Livraison livraison) {
		// TODO
	}

}