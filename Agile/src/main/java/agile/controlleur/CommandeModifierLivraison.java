package agile.controlleur;

import agile.modele.Livraison;
import agile.modele.Temps;

public class CommandeModifierLivraison implements Commande {

	private Livraison livraison;
	private Temps debutPlage;
	private Temps finPlage;
	private Temps oldDebutPlage;
	private Temps oldFinPlage;

	public CommandeModifierLivraison(Livraison livraison, Temps debutPlage, Temps finPlage) {
		this.livraison = livraison;
		oldDebutPlage = this.livraison.getDebutPlage();
		oldFinPlage = this.livraison.getFinPlage();
		this.debutPlage = debutPlage;
		this.finPlage = finPlage;
	}

	@Override
	public void doCde() {
		livraison.modifier(debutPlage, finPlage);
	}

	@Override
	public void undoCde() {
		livraison.modifier(oldDebutPlage, oldFinPlage);
	}

}