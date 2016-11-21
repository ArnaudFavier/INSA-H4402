package agile.vue;

import agile.controlleur.Controlleur;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class FenetreEcouteur {

	@FXML
	private AnchorPane dessinTest;

	private Fenetre fenetre;
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
		try {
			controlleur.chargerPlan(this.controlleur);
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(fenetre.getStage());
			alert.setTitle("Plan invalide");
			alert.setHeaderText("Plan incorrect");
			alert.setContentText("Merci de sélectionner un plan valide.");

			alert.showAndWait();
		}
	}

	/**
	 * Appelé quand l'uilisateur clique sur le bouton "Ouvrir livraison"
	 */
	@FXML
	private void boutonOuvrirLivraison() {
		if (controlleur.getPlan() == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(fenetre.getStage());
			alert.setTitle("Plan invalide");
			alert.setHeaderText("Plan incorrect");
			alert.setContentText("Merci de sélectionner un plan avant une demande de livraisons.");

			alert.showAndWait();
		} else {
			try {
				controlleur.chargerDemandeLivraisons(this.controlleur);
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.initOwner(fenetre.getStage());
				alert.setTitle("Livraisons invalide");
				alert.setHeaderText("Livraisons incorrectes");
				alert.setContentText("Merci de sélectionner une demande de livraisons valide.");

				alert.showAndWait();
			}
		}
	}

	/**
	 * Appelé quand l'uilisateur clique sur le bouton "Calculer tournée"
	 */
	@FXML
	private void boutonCalculerTournee() {
		controlleur.calculerTournee(this.controlleur);
	}

	/**
	 * Appelé quand l'uilisateur clique sur le bouton "Exporter tournée"
	 */
	@FXML
	private void boutonExporterTournee() {
		controlleur.enregistrerFeuilleDeRoute(this.controlleur);
	}

	/**
	 * Est appelé par le controlleur pour passer une référence vers celle-ci
	 * 
	 * @param controlleur
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
