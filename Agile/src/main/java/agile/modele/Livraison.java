package agile.modele;

/**
 * Repr�sente une livraison � effectuer
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
     * Le constructeur 
     * @param duree Dur�e de la livraison
     * @param intersection L'intersection sur laquelle se situe la livraison
     */
    public Livraison(int duree, Intersection intersection) {
	super();
	this.duree = duree;
	this.intersection = intersection;
    }

    /**
     * @return Dur�e de la livraison
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