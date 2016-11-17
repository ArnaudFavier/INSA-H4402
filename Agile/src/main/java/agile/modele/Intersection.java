package agile.modele;

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
     * Le constructeur de Intersection
     * 
     * @param id
     *            L'identifiant de l'intersection
     * @param x
     *            La coordonnée x de l'intersection dans le plan
     * @param y
     *            La coordonnée y de l'intersection dans le plan
     */
    public Intersection(int id, int x, int y) {
	super();
	this.id = id;
	this.x = x;
	this.y = y;
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
     * @return La coordonnée x de l'intersection dans le plan
     */
    public int getX() {
        return x;
    }

    /**
     * @return La coordonnée y de l'intersection dans le plan
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
    
}