package agile.modele;

import java.util.Collections;
import java.util.List;

/**
 * Cette classe permet d'englober plusieurs troncons, afin de repr�senter des
 * chemins entre deux intersections qui ne sont pas directement connect�es
 */
public class Chemin {
    /**
     * La liste des troncons composant le chemin
     */
    private List<Troncon> troncons;

    /**
     * La liste des intersections composant le chemin
     */
    private List<Intersection> intersections;

    /**
     * Le cout du chemin
     */
    private float cout;

    public Chemin(List<Troncon> troncons, List<Intersection> intersections, float cout) {
	this.troncons = troncons;
	this.intersections = intersections;
	this.cout = cout;
    }

    /**
     * @return Le cout du chemin
     */
    public float getCout() {
	return cout;
    }

    /**
     * @return Une vue non-modifiable de la liste des troncons composant le
     *         chemin
     */
    public List<Troncon> getTroncons() {
	return Collections.unmodifiableList(troncons);
    }

    /**
     * @return Une vue non-modifiable de la liste des intersections composant le
     *         chemin
     */
    public List<Intersection> getIntersections() {
	return Collections.unmodifiableList(intersections);
    }

    @Override
    protected Chemin clone() {
	Chemin copy = new Chemin(troncons, intersections, cout);

	return copy;
    }

}
