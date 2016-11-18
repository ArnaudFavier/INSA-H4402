package agile.modele;

/**
 * Représente une livraison à effectuer
 */
public class Livraison {

    /**
     * Durée de la livraison
     */
    private int duree;
    
    /**
     * L'intersection sur laquelle se situe la livraison
     */
    private Intersection intersection;
    
    /**
     * Le constructeur 
     * @param duree Durée de la livraison
     * @param intersection L'intersection sur laquelle se situe la livraison
     */
    public Livraison(int duree, Intersection intersection) {
	super();
	this.duree = duree;
	this.intersection = intersection;
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
    
}