package agile.vue;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import agile.controlleur.Controlleur;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

public class FenetreEcouteur {

	/* FXML view elements */
	@FXML
	private com.jfoenix.controls.JFXTreeTableView<LivraisonVue> livraisonTableView;

	/* Code architecture elements */
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
		JFXTreeTableColumn<LivraisonVue, String> colonneAdresse = new JFXTreeTableColumn<>("Adresse");
		colonneAdresse.setPrefWidth(150);
		colonneAdresse.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<LivraisonVue, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<LivraisonVue, String> param) {
						return param.getValue().getValue().intersection;
					}
				});

		JFXTreeTableColumn<LivraisonVue, String> colonneDuree = new JFXTreeTableColumn<>("Durée");
		colonneDuree.setPrefWidth(50);
		colonneDuree.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<LivraisonVue, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<LivraisonVue, String> param) {
						return param.getValue().getValue().duree;
					}
				});

		JFXTreeTableColumn<LivraisonVue, String> colonnePlageDebut = new JFXTreeTableColumn<>("Plage début");
		colonnePlageDebut.setPrefWidth(80);
		colonnePlageDebut.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<LivraisonVue, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<LivraisonVue, String> param) {
						return param.getValue().getValue().debutPlage;
					}
				});

		JFXTreeTableColumn<LivraisonVue, String> colonnePlageFin = new JFXTreeTableColumn<>("Plage fin");
		colonnePlageFin.setPrefWidth(80);
		colonnePlageFin.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<LivraisonVue, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<LivraisonVue, String> param) {
						return param.getValue().getValue().finPlage;
					}
				});

		ObservableList<LivraisonVue> livraisons = FXCollections.observableArrayList();
		livraisons.add(new LivraisonVue("42 rue Agile", "15", "8:00", "17:00"));

		final TreeItem<LivraisonVue> root = new RecursiveTreeItem<LivraisonVue>(livraisons,
				RecursiveTreeObject::getChildren);
		livraisonTableView.getColumns().setAll(colonneAdresse, colonneDuree, colonnePlageDebut, colonnePlageFin);
		livraisonTableView.setRoot(root);
		livraisonTableView.setShowRoot(false);
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
		}
		try {
			controlleur.chargerDemandeLivraisons(this.controlleur);
		} catch (Exception e) {
			e.printStackTrace();
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
