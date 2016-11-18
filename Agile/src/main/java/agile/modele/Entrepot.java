package agile.modele;

/**
 * Représente un entrepot
 */
public class Entrepot {

    /**
     * Le temps de départ de l'entrepot
     */
    private Temps heureDepart;
    
    /**
     * L'intersection sur laquelle se situe l'entrepot
     */
    private Intersection intersection;
    
    /**
     * Le constructeur
     * @param heureDepart Le temps de départ de l'entrepot
     * @param intersection L'intersection sur laquelle se situe l'entrepot
     */
    public Entrepot(Temps heureDepart, Intersection intersection) {
	super();
	this.heureDepart = heureDepart;
	this.intersection = intersection;
    }

    /**
     * @return Le temps de départ de l'entrepot
     */
    public Temps getHeureDepart() {
        return heureDepart;
    }

    /**
     * @return L'intersection sur laquelle se situe l'entrepot
     */
    public Intersection getIntersection() {
        return intersection;
    }
    
}