package agile.controlleur;

public interface Commande {

    /**
     * Execute la commande this
     */
    void doCde(Controlleur controlleur);

    /**
     * Execute la commande inverse a this
     */
    void undoCde(Controlleur controlleur);
}
