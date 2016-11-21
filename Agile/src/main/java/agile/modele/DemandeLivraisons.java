package agile.modele;

import java.util.Collections;
import java.util.List;

/**
 * Représente une demande de livraisons
 */
public class DemandeLivraisons {

	/**
	 * L'entrepot de la demande de livraisons
	 */
	private Entrepot entrepot;

	/**
	 * Les livraisons
	 */
	private List<Livraison> livraisons;

	/**
	 * Le plan dans lequel on effectue les livraisons
	 */
	private Plan plan;

	/**
	 * Constructeur de DemandeLivraisons
	 * 
	 * @param entrepot
	 *            L'entrepot de la demande de livraisons
	 * @param livraisons
	 *            Les livraisons
	 * @param plan
	 *            Le plan dans lequel on effectue les livraisons
	 */
	public DemandeLivraisons(Entrepot entrepot, List<Livraison> livraisons, Plan plan) {
		super();
		this.entrepot = entrepot;
		this.livraisons = livraisons;
		this.plan = plan;
	}

	/**
	 * @return L'entrepot de la demande de livraisons
	 */
	public Entrepot getEntrepot() {
		return entrepot;
	}

	/**
	 * @return Les livraisons
	 */
	public List<Livraison> getLivraisons() {
		return Collections.unmodifiableList(livraisons);
	}

	/**
	 * @return Le plan dans lequel on effectue les livraisons
	 */
	public Plan getPlan() {
		return plan;
	}

	@Override
	public String toString() {
		return "DemandeLivraison{" + "entrepot=" + entrepot + ", livraisons=" + livraisons + ", plan=" + plan + "}";
	}

}