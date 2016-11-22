package agile.vue;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import agile.controlleur.Controlleur;
import agile.modele.DemandeLivraisons;
import agile.modele.Livraison;
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
	@FXML
	private JFXTreeTableView<LivraisonVue> livraisonTreeTableView;
	@FXML
	private JFXTreeTableColumn<LivraisonVue, String> colonneAdresse;
	@FXML
	private JFXTreeTableColumn<LivraisonVue, String> colonneDuree;
	@FXML
	private JFXTreeTableColumn<LivraisonVue, String> colonnePlageDebut;
	@FXML
	private JFXTreeTableColumn<LivraisonVue, String> colonnePlageFin;
	@FXML
	private JFXTreeTableColumn<LivraisonVue, String> colonneHeureArrivee;
	@FXML
	private JFXTreeTableColumn<LivraisonVue, String> colonneHeureDepart;
	@FXML
	private JFXTreeTableColumn<LivraisonVue, String> colonneTempsAttente;
	@FXML
	private Label treeTableViewCount;
	@FXML
	private JFXButton treeTableViewAdd;
	@FXML
	private JFXButton treeTableViewRemove;
	@FXML
	private JFXTextField searchField;

	/* Code architecture elements */
	private ObservableList<LivraisonVue> listeLivraisons = FXCollections.observableArrayList();
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
		// Colonnes de la LivraisonTreeTableViez
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
		colonnePlageDebut.setCellValueFactory((TreeTableColumn.CellDataFeatures<LivraisonVue, String> param) -> {
			if (colonnePlageDebut.validateValue(param))
				return param.getValue().getValue().plageDebut;
			else
				return colonnePlageDebut.getComputedValue(param);
		});
		colonnePlageFin.setCellValueFactory((TreeTableColumn.CellDataFeatures<LivraisonVue, String> param) -> {
			if (colonnePlageFin.validateValue(param))
				return param.getValue().getValue().plageFin;
			else
				return colonnePlageFin.getComputedValue(param);
		});

		// Des exemples
		listeLivraisons.add(new LivraisonVue("42 rue Agile", "12", "8:00", "17:00"));
		listeLivraisons.add(new LivraisonVue("24 rue Mars", "32", "9:00", "12:00"));
		listeLivraisons.add(new LivraisonVue("18 rue Waso", "43", "-", "-"));

		livraisonTreeTableView
				.setRoot(new RecursiveTreeItem<LivraisonVue>(listeLivraisons, RecursiveTreeObject::getChildren));

		// Binding des autres composants
		livraisonTreeTableView.setShowRoot(false);
		treeTableViewCount.textProperty()
				.bind(Bindings.createStringBinding(
						() -> "(total : " + livraisonTreeTableView.getCurrentItemsCount() + " )",
						livraisonTreeTableView.currentItemsCountProperty()));
		treeTableViewAdd.disableProperty()
				.bind(Bindings.notEqual(-1, livraisonTreeTableView.getSelectionModel().selectedIndexProperty()));
		treeTableViewRemove.disableProperty()
				.bind(Bindings.equal(-1, livraisonTreeTableView.getSelectionModel().selectedIndexProperty()));
		treeTableViewAdd.setOnMouseClicked((e) -> {
			listeLivraisons.add(new LivraisonVue("?", "?", "?", "?"));
			livraisonTreeTableView.currentItemsCountProperty()
					.set(livraisonTreeTableView.currentItemsCountProperty().get() + 1);
		});
		treeTableViewRemove.setOnMouseClicked((e) -> {
			listeLivraisons.remove(livraisonTreeTableView.getSelectionModel().selectedItemProperty().get().getValue());
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
				DemandeLivraisons demandeLivraison = controlleur.getDemandeLivraisons();

				listeLivraisons.clear();
				livraisonTreeTableView.currentItemsCountProperty().set(0);
				for (Livraison livraison : demandeLivraison.getLivraisons()) {
					String debutPlage = "";
					String finPlage = "";

					if (livraison.getDebutPlage() != null)
						debutPlage = livraison.getDebutPlage().toString();
					if (livraison.getFinPlage() != null)
						finPlage = livraison.getFinPlage().toString();

					listeLivraisons.add(new LivraisonVue(livraison.getIntersection().toString(),
							String.valueOf(livraison.getDuree()), debutPlage, finPlage));
					livraisonTreeTableView.currentItemsCountProperty()
							.set(livraisonTreeTableView.currentItemsCountProperty().get() + 1);
				}
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
		} catch (Exception e) {
			e.printStackTrace();
		}
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
