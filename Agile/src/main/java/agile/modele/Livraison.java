package agile.modele;

/**
 * Représente une livraison à effectuer
 */
public class Livraison {

    /**
     * Un identifiant de livraison, unique pour chaque livraison
     */
    private int id;

    /**
     * Le prochain id donné à une livraison
     */
    private static int NEXT_ID = 0;

    /**
     * Durée de la livraison
     */
    private int duree;

    /**
     * L'intersection sur laquelle se situe la livraison
     */
    private Intersection intersection;

    /**
     * Definit si la livraison a une contrainte de Temps (plage)
     */
    private boolean contrainteTemps;

    /**
     * Debut plage
     */
    private Temps debutPlage;

    /**
     * Fin plage
     */
    private Temps finPlage;

    /**
     * Temps d'attente en secondes
     */
    private float tempsAttente;

    /**
     * Heure départ
     */
    private Temps heureArrivee;

    /**
     * Le constructeur
     * 
     * @param duree
     *            Durée de la livraison
     * @param intersection
     *            L'intersection sur laquelle se situe la livraison
     */
    public Livraison(int duree, Intersection intersection) {
	super();
	this.id = NEXT_ID++;
	this.duree = duree;
	this.intersection = intersection;
	this.contrainteTemps = false;
    }

    /**
     * Le constructeur avec plage horaire
     * 
     * @param duree
     *            Durée de la livraison
     * @param intersection
     *            L'intersection sur laquelle se situe la livraison
     */
    public Livraison(int duree, Intersection intersection, Temps debutPlage, Temps finPlage) {
	super();
	this.id = NEXT_ID++;
	this.duree = duree;
	this.intersection = intersection;
	this.debutPlage = debutPlage;
	this.finPlage = finPlage;
	this.contrainteTemps = true;
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

    /**
     * @return Un booléen disant si on a une plage à respecter
     */
    public boolean ContrainteDeTemps() {
	return contrainteTemps;
    }

    /**
     * @return Le début de la plage si elle existe, null sinon
     */
    public Temps getDebutPlage() {
	return debutPlage;
    }

    public void setDebutPlage(Temps debutPlage) {
	this.debutPlage = debutPlage;
    }

    /**
     * @return La fin de la plage si elle existe, null sinon
     */
    public Temps getFinPlage() {
	return finPlage;
    }

    public void setFinPlage(Temps finPlage) {
	this.finPlage = finPlage;
    }

    @Override
    public String toString() {
	return Integer.toString(intersection.getId());
    }

    /**
     * @return Le temps d'attente, en secondes
     */
    public float getTempsAttente() {
	return tempsAttente;
    }

    /**
     * Definir le temps d'attente
     * 
     * @param tempsAttente
     *            Le temps d'attente en secondes
     */
    public void setTempsAttente(float tempsAttente) {
	this.tempsAttente = tempsAttente;
    }

    /**
     * Definir l'heure d'arrivee sur le point de livraison
     * 
     * @param heureArrivee
     *            Le temps d'arrivee en secondes
     */
    public void setHeureArrivee(int heureArrivee) {
	this.heureArrivee = new Temps(heureArrivee);
    }

    public Temps getHeureArrivee() {
	return heureArrivee;
    }

    @Override
    public Livraison clone() {
	Livraison copy = new Livraison(duree, intersection, debutPlage, finPlage);

	copy.id = id;
	copy.contrainteTemps = contrainteTemps;
	copy.heureArrivee = heureArrivee;
	copy.tempsAttente = tempsAttente;

	return copy;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Livraison other = (Livraison) obj;
	if (intersection == null) {
	    if (other.intersection != null)
		return false;
	} else if (id != other.id)
	    return false;
	return true;
    }

}