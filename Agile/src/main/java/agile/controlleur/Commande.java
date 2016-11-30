package agile.controlleur;

public interface Commande {

    /**
     * Execute la commande this
     */
    void doCde();

    /**
     * Execute la commande inverse a this
     */
    void undoCde(Controlleur controlleur);
}
