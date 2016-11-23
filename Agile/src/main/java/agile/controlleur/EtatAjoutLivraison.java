package agile.controlleur;

import agile.modele.Livraison;

public class EtatAjoutLivraison extends EtatDefaut {

	public EtatAjoutLivraison() {
	}

	@Override
	public void ajouterLivraison(Controlleur controlleur, Livraison livraison) {
		controlleur.getTournee().ajouterLivraison(livraison);
	}

}