package agile.modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agile.xml.DeserialiseurPlanXML;

/**
 * Représente un Plan chargé {@link DeserialiseurPlanXML#charger()} permet le
 * chargement d'un plan
 */
public class Plan {

    /**
     * La liste des intersections du plan
     */
    private List<Intersection> intersections;

    /**
     * La liste des tronçons du plan
     */
    private List<Troncon> troncons;

    /**
     * Le constructeur du Plan
     */
    public Plan() {
	intersections = new ArrayList<>();
	troncons = new ArrayList<>();
    }

    /**
     * Ajoute une intersection à la liste des intersections
     * 
     * @param intersection
     *            l'intersection ajoutée
     */
    public void addIntersection(Intersection intersection) {
	intersections.add(intersection);
    }

    /**
     * Ajoute un tronçon à la liste des tronçons
     * 
     * @param troncon
     *            le tronçon ajouté
     */
    public void addTroncon(Troncon troncon) {
	troncons.add(troncon);
    }

    @Override
    public String toString() {
	return "Plan{" + "intersections=" + intersections + ", troncons=" + troncons + '}';
    }

    /**
     * @return Une vue non-modifiable de la liste des intersections
     */
    public List<Intersection> getIntersections() {
	return Collections.unmodifiableList(intersections);
    }

    /**
     * @return Une vue non-modifiable de la liste des tronçons
     */
    public List<Troncon> getTroncons() {
	return Collections.unmodifiableList(troncons);
    }

}