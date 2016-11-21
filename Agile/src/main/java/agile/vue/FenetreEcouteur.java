package agile.vue;

import agile.controlleur.Controlleur;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class FenetreEcouteur {

	@FXML
	private AnchorPane dessinTest;

	private Fenetre fenetre; // Référence vers la fenêtre
	private Controlleur controlleur;

	/**
	 * Le constructeur est appelé avant la méthode initialize()
	 */
	public FenetreEcouteur() {
	}

	/**
	 * Initialise le controlleur. Cette méthode est automatiquement appelée
	 * après que le fichier fxml soit chargé
	 */
	@FXML
	private void initialize() {
		VuePlan.AffichageTest(dessinTest);
	}

	/**
	 * Appelé quand l'uilisateur clique sur le bouton "Ouvrir plan"
	 */
	@FXML
	private void boutonOuvrirPlan() {
		controlleur.chargerPlan(this.controlleur);
	}

	/**
	 * Appelé quand l'uilisateur clique sur le bouton "Ouvrir livraison"
	 */
	@FXML
	private void boutonOuvrirLivraison() {
		controlleur.chargerDemandeLivraison(this.controlleur);
	}

	/**
	 * Est appelé par la fenêtre pour passer une référence vers celle-ci
	 * 
	 * @param fenetre
	 */
	public void setFenetre(Fenetre fenetre) {
		this.fenetre = fenetre;
	}

	/**
	 * Est appelé par le controlleur pour passer une référence vers celui-ci
	 * 
	 * @param controlleur
	 */
	public void setControlleur(Controlleur controlleur) {
		this.controlleur = controlleur;
	}

}
