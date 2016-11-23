package agile.vue;

import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import agile.controlleur.Controlleur;
import agile.modele.Entrepot;
import agile.modele.Intersection;
import agile.modele.Livraison;
import agile.modele.Temps;
import io.datafx.controller.FXMLController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;

@FXMLController(value = "Content.fxml")
public class ContentController {

	/* FXML view elements */
	// Entrepôt
	@FXML
	private JFXTreeTableView<EntrepotVue> entrepotTreeTableView;
	@FXML
	private JFXTreeTableColumn<EntrepotVue, String> colonneEntrepotAdresse;
	@FXML
	private JFXTreeTableColumn<EntrepotVue, String> colonneEntrepotHeureDepart;

	// Livraisons
	@FXML
	private JFXTreeTableView<LivraisonVue> livraisonTreeTableView;
	@FXML
	private JFXTreeTableColumn<LivraisonVue, String> colonneAdresse;
	@FXML
	private JFXTreeTableColumn<LivraisonVue, String> colonneHeureArrivee;
	@FXML
	private JFXTreeTableColumn<LivraisonVue, String> colonneTempsAttente;
	@FXML
	private JFXTreeTableColumn<LivraisonVue, String> colonneDuree;
	@FXML
	private JFXTreeTableColumn<LivraisonVue, String> colonneHeureDepart;
	@FXML
	private JFXTreeTableColumn<LivraisonVue, String> colonnePlagePrevisionnelle;
	@FXML
	private Label treeTableViewCount;
	@FXML
	private JFXButton treeTableViewAdd;
	@FXML
	private JFXButton treeTableViewRemove;
	@FXML
	private JFXTextField searchField;

	// Boutons
	@FXML
	private JFXButton boutonOuvrirPlan;
	@FXML
	private JFXButton boutonOuvrirLivraison;
	@FXML
	private JFXButton boutonCalculerTournee;
	@FXML
	private JFXButton boutonExporterTournee;

	/* Code architecture elements */
	private ObservableList<EntrepotVue> observableEntrepot = FXCollections.observableArrayList();
	private ObservableList<LivraisonVue> observableListeLivraisons = FXCollections.observableArrayList();
	private Fenetre fenetre;
	public static Controlleur controlleur;

	/**
	 * Le constructeur est appelé avant la méthode initialize()
	 */
	public ContentController() {
	}

	/**
	 * Initialise le controlleur. Cette méthode est automatiquement appelée
	 * après que le fichier fxml soit chargé
	 */
	@FXML
	private void initialize() {
		// Colonnes du entrepotTreeTableView
		colonneEntrepotAdresse.setCellValueFactory((TreeTableColumn.CellDataFeatures<EntrepotVue, String> param) -> {
			if (colonneEntrepotAdresse.validateValue(param))
				return param.getValue().getValue().intersection;
			else
				return colonneEntrepotAdresse.getComputedValue(param);
		});
		colonneEntrepotHeureDepart
				.setCellValueFactory((TreeTableColumn.CellDataFeatures<EntrepotVue, String> param) -> {
					if (colonneEntrepotHeureDepart.validateValue(param))
						return param.getValue().getValue().heureDepart;
					else
						return colonneEntrepotHeureDepart.getComputedValue(param);
				});

		// Colonnes de la livraisonTreeTableView
		colonneAdresse.setCellValueFactory((TreeTableColumn.CellDataFeatures<LivraisonVue, String> param) -> {
			if (colonneAdresse.validateValue(param))
				return param.getValue().getValue().intersection;
			else
				return colonneAdresse.getComputedValue(param);
		});
		colonneDuree.setCellValueFactory((TreeTableColumn.CellDataFeatures<LivraisonVue, String> param) -> {
			if (colonneDuree.validateValue(param))
				return param.getValue().getValue().duree;
			else
				return colonneDuree.getComputedValue(param);
		});
		colonnePlagePrevisionnelle
				.setCellValueFactory((TreeTableColumn.CellDataFeatures<LivraisonVue, String> param) -> {
					if (colonnePlagePrevisionnelle.validateValue(param))
						return param.getValue().getValue().plageFin;
					else
						return colonnePlagePrevisionnelle.getComputedValue(param);
				});

		// Des exemples
		observableEntrepot.add(new EntrepotVue(new Entrepot(new Temps(5, 3, 4), new Intersection(6, 7, 8))));

		// Binding des autres composants
		entrepotTreeTableView
				.setRoot(new RecursiveTreeItem<EntrepotVue>(observableEntrepot, RecursiveTreeObject::getChildren));
		entrepotTreeTableView.setShowRoot(false);

		livraisonTreeTableView.setRoot(
				new RecursiveTreeItem<LivraisonVue>(observableListeLivraisons, RecursiveTreeObject::getChildren));
		livraisonTreeTableView.setShowRoot(false);
		treeTableViewCount.textProperty()
				.bind(Bindings.createStringBinding(
						() -> "(total : " + livraisonTreeTableView.getCurrentItemsCount() + ")",
						livraisonTreeTableView.currentItemsCountProperty()));
		treeTableViewRemove.disableProperty()
				.bind(Bindings.equal(-1, livraisonTreeTableView.getSelectionModel().selectedIndexProperty()));
		treeTableViewAdd.setOnMouseClicked((e) -> {

			livraisonTreeTableView.currentItemsCountProperty()
					.set(livraisonTreeTableView.currentItemsCountProperty().get() + 1);
		});
		treeTableViewRemove.setOnMouseClicked((e) -> {
			observableListeLivraisons
					.remove(livraisonTreeTableView.getSelectionModel().selectedItemProperty().get().getValue());
			livraisonTreeTableView.currentItemsCountProperty()
					.set(livraisonTreeTableView.currentItemsCountProperty().get() - 1);
		});
		searchField.textProperty().addListener((o, oldVal, newVal) -> {
			livraisonTreeTableView.setPredicate(person -> person.getValue().intersection.get().contains(newVal)
					|| person.getValue().duree.get().contains(newVal));
		});
	}

	/**
	 * Appelé quand l'uilisateur clique sur le bouton "Ouvrir plan"
	 */
	@FXML
	private void boutonOuvrirPlan() {
		try {
			controlleur.chargerPlan(this.controlleur);

			// Mise à jour des boutons
			boutonOuvrirLivraison.setVisible(true);
			boutonCalculerTournee.setVisible(false);
			boutonExporterTournee.setVisible(false);
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
				miseAJourLivraison(controlleur.getDemandeLivraisons().getLivraisons());

				// Mise à jour des boutons
				boutonOuvrirLivraison.setVisible(true);
				boutonCalculerTournee.setVisible(true);
				boutonExporterTournee.setVisible(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Appelé quand l'uilisateur clique sur le bouton "Calculer tournée"
	 */
	@FXML
	private void boutonCalculerTournee() {
		try {
			controlleur.calculerTournee(this.controlleur);

			// Mise à jour des boutons
			boutonOuvrirLivraison.setVisible(true);
			boutonCalculerTournee.setVisible(true);
			boutonExporterTournee.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Appelé quand l'uilisateur clique sur le bouton "Exporter tournée"
	 */
	@FXML
	private void boutonExporterTournee() {
		try {
			controlleur.enregistrerFeuilleDeRoute(this.controlleur);
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(fenetre.getStage());
			alert.setTitle("Plan invalide");
			alert.setHeaderText("Plan incorrect");
			alert.setContentText("Erreur lors de l'export de la feuille de route.");

			alert.showAndWait();
		}
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

	/**
	 * Met à jour la liste des livraisons
	 */
	public void miseAJourLivraison(List<Livraison> livraisons) {
		observableListeLivraisons.clear();
		livraisonTreeTableView.currentItemsCountProperty().set(0);
		for (Livraison livraison : livraisons) {
			String debutPlage = "";
			String finPlage = "";

			if (livraison.getDebutPlage() != null)
				debutPlage = livraison.getDebutPlage().toString();
			if (livraison.getFinPlage() != null)
				finPlage = livraison.getFinPlage().toString();
			observableListeLivraisons.add(new LivraisonVue(livraison));
			
			livraisonTreeTableView.currentItemsCountProperty()
					.set(livraisonTreeTableView.currentItemsCountProperty().get() + 1);
		}

	}

}
