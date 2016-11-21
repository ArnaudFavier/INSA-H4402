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
	 * Heure de début de plage de livraison
	 */
	private Temps debutPlage;

	/**
	 * Heure de fin de plage de livraison
	 */
	private Temps finPlage;

	/**
	 * L'intersection sur laquelle se situe la livraison
	 */
	private Intersection intersection;

	/**
	 * Le constructeur
	 * 
	 * @param duree
	 *            Dur�e de la livraison
	 * @param intersection
	 *            L'intersection sur laquelle se situe la livraison
	 */
	public Livraison(int duree, Temps debutPlage, Temps finPlage, Intersection intersection) {
		super();
		this.duree = duree;
		this.debutPlage = debutPlage;
		this.finPlage = finPlage;
		this.intersection = intersection;
	}

	public void modifier(Temps debutPlage, Temps finPlage) {
		this.debutPlage = debutPlage;
		this.finPlage = finPlage;
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

	/**
	 * 
	 * @return L'heure de debut de plage
	 */
	public Temps getDebutPlage() {
		return debutPlage;
	}

	/**
	 * 
	 * @return L'heure de fin de plage
	 */
	public Temps getFinPlage() {
		return finPlage;
	}

}