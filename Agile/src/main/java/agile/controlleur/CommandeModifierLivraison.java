package agile.controlleur;

import agile.modele.Livraison;
import agile.modele.Temps;
import agile.modele.Tournee;

public class CommandeModifierLivraison implements Commande {

    private Livraison livraison;
    private Tournee tournee;
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
    public CommandeModifierLivraison(Tournee tournee, Livraison livraison, Temps debutPlage, Temps finPlage) {
	this.tournee = tournee;
	this.livraison = livraison;
	oldDebutPlage = this.livraison.getDebutPlage();
	oldFinPlage = this.livraison.getFinPlage();
	this.debutPlage = debutPlage;
	this.finPlage = finPlage;
    }

    @Override
    public void doCde() {
	tournee.modifierLivraison(livraison, debutPlage, finPlage);
    }

    @Override
    public void undoCde() {
	tournee.modifierLivraison(livraison, oldDebutPlage, oldFinPlage);
    }

}