package agile.modele;

import java.util.List;

/**
 * Cette classe permet d'englober plusieurs troncons, afin de représenter des
 * chemins entre deux intersections qui ne sont pas directement connectées
 */
public class Chemin {
    /**
     * La liste des troncons composant le chemin
     */
    private List<Troncon> troncons;
    
    /**
     * Le cout du chemin
     */
    private int cout;

    private Chemin(List<Troncon> troncons, int cout) {
	this.troncons = troncons;
	this.cout = cout;
    }

    /**
     * @return Le cout du chemin
     */
    public int getCout() {
	return cout;
    }

}
