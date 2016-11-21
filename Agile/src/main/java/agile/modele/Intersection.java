package agile.modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Représente une intersection du plan
 */
public class Intersection {

    /**
     * L'identifiant de l'intersection
     */
    private int id;

    /**
     * La coordonnée x de l'intersection dans le plan
     */
    private int x;

    /**
     * La coordonnée y de l'intersection dans le plan
     */
    private int y;

    /**
     * La liste des Troncon auquel l'Intersection appartient
     */
    private List<Troncon> troncons;

    /**
     * Le constructeur de Intersection
     * 
     * @param id
     *            L'identifiant de l'intersection
     * @param x
     *            La coordonnée x de l'intersection dans le plan
     * @param y
     *            La coordonn�e y de l'intersection dans le plan
     */
    public Intersection(int id, int x, int y) {
	this.id = id;
	this.x = x;
	this.y = y;

	troncons = new ArrayList<>();
    }

    @Override
    public String toString() {
	return "{" + "id=" + id + ", x=" + x + ", y=" + y + '}';
    }

    /**
     * @return L'identifiant de l'intersection
     */
    public int getId() {
	return id;
    }

    /**
     * @return La coordonn�e x de l'intersection dans le plan
     */
    public int getX() {
	return x;
    }

    /**
     * @return La coordonn�e y de l'intersection dans le plan
     */
    public int getY() {
	return y;
    }

    @Override
    public int hashCode() {
	return id;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Intersection other = (Intersection) obj;
	if (id != other.id)
	    return false;
	return true;
    }

    /**
     * Ajouter un troncon auquel l'intersection appartient On ne vérifie pas
     * qu'on appartient bien, l'opération est faite au sein de ce package
     * uniquement (protected)
     * 
     * @param troncon
     *            Le troncon ajouté
     */
    protected void ajouterTroncon(Troncon troncon) {
	troncons.add(troncon);
    }

    /**
     * @return La liste des Troncon auquel l'Intersection appartient
     */
    public List<Troncon> getTroncons() {
        return Collections.unmodifiableList(troncons);
    }
}