package agile.controlleur;

import agile.modele.Livraison;
import agile.modele.Temps;

public class EtatModifierLivraison extends EtatDefaut {

	public EtatModifierLivraison() {

	}

	@Override
	public void modifierLivraison(Controlleur controlleur, int idLivraison, Temps debutPlage, Temps finPlage) {
		for (Livraison livraison : controlleur.getTournee().getLivraisonsTSP()) {
			if (livraison.getIntersection().getId() == idLivraison) {
				livraison.setDebutPlage(debutPlage);
				livraison.setFinPlage(finPlage);
				break;
			}
		}
	}

}