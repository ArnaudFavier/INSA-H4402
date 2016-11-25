package agile.vue;

import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import agile.controlleur.Controlleur;
import agile.modele.Entrepot;
import agile.modele.Livraison;
import io.datafx.controller.FXMLController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.StackPane;

@FXMLController(value = "Content.fxml")
public class ContentController {

	@FXML
	private StackPane root;

	/* FXML vue éléments */
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
	@FXML
	private JFXButton boutonAjouterLivraison;
	@FXML
	private JFXButton boutonSupprimerLivraison;

	// Snackbar
	@FXML
	private JFXSnackbar snackbar;

	/* Code architecture elements */
	private ObservableList<EntrepotVue> observableEntrepot = FXCollections.observableArrayList();
	private ObservableList<LivraisonVue> observableListeLivraisons = FXCollections.observableArrayList();
	public static Fenetre fenetre;
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
						return param.getValue().getValue().plagePrevisionnelle;
					else
						return colonnePlagePrevisionnelle.getComputedValue(param);
				});
		colonneTempsAttente.setCellValueFactory((TreeTableColumn.CellDataFeatures<LivraisonVue, String> param) -> {
			if (colonneTempsAttente.validateValue(param))
				return param.getValue().getValue().tempsAttente;
			else
				return colonneTempsAttente.getComputedValue(param);
		});

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
		boutonSupprimerLivraison.disableProperty()
				.bind(Bindings.equal(-1, livraisonTreeTableView.getSelectionModel().selectedIndexProperty()));
		boutonAjouterLivraison.setOnMouseClicked((e) -> {
			// DialogNouvelleLivraison.show(this, root);
		});
		boutonSupprimerLivraison.setOnMouseClicked((e) -> {
			Livraison livraisonSupprimer = livraisonTreeTableView.getSelectionModel().selectedItemProperty().get()
					.getValue().livraison;
			controlleur.supprimerLivraison(controlleur, livraisonSupprimer);
			miseAJourLivraison(controlleur.getTournee().getLivraisonsTSP());
		});
		searchField.textProperty().addListener((o, oldVal, newVal) -> {
			livraisonTreeTableView.setPredicate(person -> person.getValue().intersection.get().contains(newVal)
					|| person.getValue().duree.get().contains(newVal));
		});

		snackbar.registerSnackbarContainer(root);
	}

	/**
	 * Appelé quand l'uilisateur clique sur le bouton "Ouvrir plan"
	 */
	@FXML
	private void boutonOuvrirPlan() {
		try {
			controlleur.chargerPlan(this.controlleur);
			observableEntrepot.clear();
			observableListeLivraisons.clear();

			// Mise à jour des boutons
			boutonOuvrirLivraison.setVisible(true);
			boutonCalculerTournee.setVisible(false);
			boutonExporterTournee.setVisible(false);
			boutonAjouterLivraison.setVisible(false);
			boutonSupprimerLivraison.setVisible(false);
		} catch (Exception e) {
			snackbar.fireEvent(new SnackbarEvent("Plan invalide."));
		}
	}

	/**
	 * Appelé quand l'uilisateur clique sur le bouton "Ouvrir livraison"
	 */
	@FXML
	private void boutonOuvrirLivraison() {
		if (controlleur.getPlan() == null) {
			snackbar.fireEvent(new SnackbarEvent("Merci de sélectionner un plan avant une demande de livraisons."));
		} else {
			try {
				controlleur.chargerDemandeLivraisons(this.controlleur);
				miseAJourEntrepot(controlleur.getDemandeLivraisons().getEntrepot());
				miseAJourLivraison(controlleur.getDemandeLivraisons().getLivraisons());

				// Mise à jour des boutons
				boutonOuvrirLivraison.setVisible(true);
				boutonCalculerTournee.setVisible(true);
				boutonExporterTournee.setVisible(false);
				boutonAjouterLivraison.setVisible(false);
				boutonSupprimerLivraison.setVisible(false);
			} catch (Exception e) {
				snackbar.fireEvent(new SnackbarEvent("Demande de livraisons invalide."));
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
			miseAJourLivraison(controlleur.getTournee().getLivraisonsTSP());
			System.out.println("tmps: " + controlleur.getTournee().getLivraisonsTSP().get(0).getTempsAttente());

			// Mise à jour des boutons
			boutonOuvrirLivraison.setVisible(true);
			boutonCalculerTournee.setVisible(true);
			boutonExporterTournee.setVisible(true);
			boutonAjouterLivraison.setVisible(true);
			boutonSupprimerLivraison.setVisible(true);
		} catch (Exception e) {
			snackbar.fireEvent(new SnackbarEvent("Calcul de tournée impossible."));
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
			snackbar.fireEvent(new SnackbarEvent("Export annulé."));
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
			observableListeLivraisons.add(new LivraisonVue(livraison));
		}
		livraisonTreeTableView.currentItemsCountProperty().set(observableListeLivraisons.size());
	}

	/**
	 * Met à jour la liste de l'entrepôt
	 */
	public void miseAJourEntrepot(Entrepot entrepot) {
		observableEntrepot.clear();
		observableEntrepot.add(new EntrepotVue(entrepot));
	}

}
