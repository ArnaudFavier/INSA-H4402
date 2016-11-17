package agile.modele;

import java.util.Arrays;

/**
 * Représente un tronçon
 */
public class Troncon {

    /**
     * La longueur du tronçon
     */
    private int longueur;
    
    /**
     * La vitesse assignée au tronçon
     */
    private int vitesse;
    
    /**
     * Le nom de la rue
     */
    private String nomRue;
    
    /**
     * Les deux intersections associées au tronçon
     */
    private Intersection[] intersections;

    /**
     * Le constructeur
     * @param longueur La longueur du tronçon
     * @param vitesse La vitesse assignée au tronçon
     * @param nomRue Le nom de la rue
     * @param inter1 La première intersection du tronçon
     * @param inter2 La seconde intersection du tronçon
     */
    public Troncon(int longueur, int vitesse, String nomRue, Intersection inter1, Intersection inter2) {
	super();
	this.longueur = longueur;
	this.vitesse = vitesse;
	this.nomRue = nomRue;
	intersections = new Intersection[2];
	intersections[0] = inter1;
	intersections[1] = inter2;
    }

    @Override
    public String toString() {
	return "Troncon{" + "longueur=" + longueur + ", vitesse=" + vitesse + ", nomRue='" + nomRue + '\''
		+ ", intersections=" + Arrays.toString(intersections) + '}';
    }

    /**
     * @return La longueur du tronçon
     */
    public int getLongueur() {
        return longueur;
    }

    /**
     * @return La vitesse assignée au tronçon
     */
    public int getVitesse() {
        return vitesse;
    }

    /**
     * @return Le nom de la rue
     */
    public String getNomRue() {
        return nomRue;
    }

    /**
     * @return Les deux intersections du tronçon
     */
    public Intersection[] getIntersections() {
        return intersections;
    }
    
    /**
     * @return La première intersection du tronçon
     */
    public Intersection getOrigine(){
	return intersections[0];
    }
    
    /**
     * @return La seconde intersection du tronçon
     */
    public Intersection getDestination(){
	return intersections[1];
    }
}