package agile.controlleur;

import java.util.LinkedList;

public class Historique {

    private LinkedList<Commande> liste;
    private int indiceCrt;

    public Historique() {
	indiceCrt = -1;
	liste = new LinkedList<Commande>();
    }

    /**
     * Ajout de la commande c a la liste this
     * 
     * @param c
     */
    public void ajoute(Commande c, Controlleur controlleur) {
	int i = indiceCrt + 1;
	while (i < liste.size()) {
	    liste.remove(i);
	}
	indiceCrt++;
	liste.add(indiceCrt, c);
	c.doCde(controlleur);
    }

    /**
     * Annule temporairement la derniere commande ajoutee (cette commande pourra
     * etre remise dans la liste avec redo)
     */
    public void undo(Controlleur controlleur) {
	if (indiceCrt >= 0) {
	    Commande cde = liste.get(indiceCrt);
	    indiceCrt--;
	    cde.undoCde(controlleur);
	}
    }

    /**
     * Remet dans la liste la derniere commande annulee avec undo
     */
    public void redo(Controlleur controlleur) {
	if (indiceCrt < liste.size() - 1) {
	    indiceCrt++;
	    Commande cde = liste.get(indiceCrt);
	    cde.doCde(controlleur);
	}
    }

    /**
     * Supprime definitivement toutes les commandes de liste
     */
    public void reset() {
	indiceCrt = -1;
	liste.clear();
    }

}
