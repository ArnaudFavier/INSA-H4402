package agile.controlleur;

import agile.modele.Livraison;
import agile.modele.Temps;

public class CommandeModifierLivraison implements Commande {

    private Livraison livraison;
    private Temps debutPlage;
    private Temps finPlage;
    private Temps oldDebutPlage;
    private Temps oldFinPlage;

    /**
     * Cree la commande qui permet de modifier une livraison de la tournee
     * 
     * @param livraison
     * @param debutPlage
     * @param finPlage
     */
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