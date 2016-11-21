package agile.modele;

/**
<<<<<<< HEAD
 * Repr�sente une livraison à effectuer
 */
public class Livraison {

    /**
     * Dur�e de la livraison
     */
    private int duree;
    
    /**
     * L'intersection sur laquelle se situe la livraison
     */
    private Intersection intersection;
    
    /**
     * Definit si la livraison a une contrainte de Temps (plage)
     */
    private boolean contrainteTemps;
    
    /**
     * Debut plage
     */
    private Temps debutPlage;
    
    /**
     * Fin plage
     */
    private Temps finPlage;
    
    /**
     * Le constructeur 
     * @param duree Durée de la livraison
     * @param intersection L'intersection sur laquelle se situe la livraison
     */
    public Livraison(int duree, Intersection intersection) {
	super();
	this.duree = duree;
	this.intersection = intersection;
	this.contrainteTemps = false;
    }
    
    /**
     * Le constructeur avec plage horaire
     * @param duree Durée de la livraison
     * @param intersection L'intersection sur laquelle se situe la livraison
     */
    public Livraison(int duree, Intersection intersection, Temps debutPlage, Temps finPlage) {
	super();
	this.duree = duree;
	this.intersection = intersection;
	this.debutPlage = debutPlage;
	this.finPlage = finPlage;
	this.contrainteTemps = true;
    }


    /**
     * @return Durée de la livraison
     */
    public int getDuree() {
        return duree;
    }

    /**
     * @return L'intersection sur laquelle se situe la livraison
     */
    public Intersection getIntersection() {
        return intersection;
    }

    /**
     * @return Un booléen disant si on a une plage à respecter
     */
    public boolean ContrainteDeTemps() {
        return contrainteTemps;
    }

    /**
     * @return Le début de la plage si elle existe, null sinon
     */
    public Temps getDebutPlage() {
        return debutPlage;
    }

    /**
     * @return La fin de la plage si elle existe, null sinon
     */
    public Temps getFinPlage() {
        return finPlage;
    }
}