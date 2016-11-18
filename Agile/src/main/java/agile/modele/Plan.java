package agile.modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import agile.xml.DeserialiseurPlanXML;

/**
 * Repr�sente un Plan charg� {@link DeserialiseurPlanXML#charger()} permet le
 * chargement d'un plan
 */
public class Plan {

    /**
     * La liste des intersections du plan
     */
    private List<Intersection> intersections;

    /**
     * Une map des ids des intersections en fonction
     */
    private Map<Integer, Intersection> intersectionsParId;

    /**
     * La liste des tron�ons du plan
     */
    private List<Troncon> troncons;

    /**
     * Le constructeur du Plan
     */
    public Plan() {
	intersections = new ArrayList<>();
	intersectionsParId = new HashMap<>();
	troncons = new ArrayList<>();
    }

    /**
     * Ajoute une intersection � la liste des intersections
     * 
     * @param intersection
     *            l'intersection ajout�e
     */
    public void addIntersection(Intersection intersection) {
	intersections.add(intersection);
	intersectionsParId.put(intersection.getId(), intersection);
    }

    /**
     * Ajoute un tron�on � la liste des tron�ons
     * 
     * @param troncon
     *            le tron�on ajout�
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
     * @return Une vue non-modifiable de la liste des tron�ons
     */
    public List<Troncon> getTroncons() {
	return Collections.unmodifiableList(troncons);
    }

    /**
     * @param id L'id de l'intersection recherch�e
     * @return l'intersection correspondant � l'id donn�
     */
    public Intersection getIntersectionParId(int id) {
	return intersectionsParId.get(id);
    }

}