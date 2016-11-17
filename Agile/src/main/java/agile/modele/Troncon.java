package agile.modele;

import java.util.Arrays;

/**
 * Repr�sente un tron�on
 */
public class Troncon {

    /**
     * La longueur du tron�on
     */
    private int longueur;
    
    /**
     * La vitesse assign�e au tron�on
     */
    private int vitesse;
    
    /**
     * Le nom de la rue
     */
    private String nomRue;
    
    /**
     * Les deux intersections associ�es au tron�on
     */
    private Intersection[] intersections;

    /**
     * Le constructeur
     * @param longueur La longueur du tron�on
     * @param vitesse La vitesse assign�e au tron�on
     * @param nomRue Le nom de la rue
     * @param inter1 La premi�re intersection du tron�on
     * @param inter2 La seconde intersection du tron�on
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
     * @return La longueur du tron�on
     */
    public int getLongueur() {
        return longueur;
    }

    /**
     * @return La vitesse assign�e au tron�on
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
     * @return Les deux intersections du tron�on
     */
    public Intersection[] getIntersections() {
        return intersections;
    }
    
    /**
     * @return La premi�re intersection du tron�on
     */
    public Intersection getOrigine(){
	return intersections[0];
    }
    
    /**
     * @return La seconde intersection du tron�on
     */
    public Intersection getDestination(){
	return intersections[1];
    }
}