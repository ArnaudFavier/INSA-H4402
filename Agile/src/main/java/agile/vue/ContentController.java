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

/**
 * Controlleur de Content.fxml en charge du contenu de la fenêtre graphique
 */
@FXMLController(value = "Content.fxml")
public class ContentController {

    /**
     * StackPane principal du contenu de la fenêtre
     */
    @FXML
    private StackPane root;

    /* FXML vue éléments */
    // Entrepôt
    /**
     * Tableau de l'entrepôt
     */
    @FXML
    private JFXTreeTableView<EntrepotVue> entrepotTreeTableView;
    /**
     * Colonne adresse du tableau de l'entrepôt
     */
    @FXML
    private JFXTreeTableColumn<EntrepotVue, String> colonneEntrepotAdresse;
    /**
     * Colonne heure de départ du tableau de l'entrepôt
     */
    @FXML
    private JFXTreeTableColumn<EntrepotVue, String> colonneEntrepotHeureDepart;

    // Livraisons
    /**
     * Tableau de la listes des livraisons
     */
    @FXML
    private JFXTreeTableView<LivraisonVue> livraisonTreeTableView;
    /**
     * Colonne adresse du tableau des livraisons
     */
    @FXML
    private JFXTreeTableColumn<LivraisonVue, String> colonneAdresse;
    /**
     * Colonne heure d'arrivée du tableau des livraisons
     */
    @FXML
    private JFXTreeTableColumn<LivraisonVue, String> colonneHeureArrivee;
    /**
     * Colonne temps d'attente du tableau des livraisons
     */
    @FXML
    private JFXTreeTableColumn<LivraisonVue, String> colonneTempsAttente;
    /**
     * Colonne durée du tableau des livraisons
     */
    @FXML
    private JFXTreeTableColumn<LivraisonVue, String> colonneDuree;
    /**
     * Colonne heure de départ du tableau des livraisons
     */
    @FXML
    private JFXTreeTableColumn<LivraisonVue, String> colonneHeureDepart;
    /**
     * Colonne de la plage prévisionnelle du tableau des livraisons
     */
    @FXML
    private JFXTreeTableColumn<LivraisonVue, String> colonnePlagePrevisionnelle;
    /**
     * Nombre total de livraisons dans le tableau des livraisons
     */
    @FXML
    private Label treeTableViewCount;
    /**
     * Champs de recherche du tableau des livraisons
     */
    @FXML
    private JFXTextField searchField;

    // Boutons
    /**
     * Bouton permettant d'ouvrir un plan
     */
    @FXML
    private JFXButton boutonOuvrirPlan;
    /**
     * Bouton permettant d'ouvrir une demande de livraisons
     */
    @FXML
    private JFXButton boutonOuvrirLivraison;
    /**
     * Bouton pour calculer l'ordre et les temps d'attente d'une tournée de
     * livraisons
     */
    @FXML
    private JFXButton boutonCalculerTournee;
    /**
     * Bouton pour exporter la tournée sous une feuille de route
     */
    @FXML
    private JFXButton boutonExporterTournee;
    /**
     * Bouton permettant l'ajout d'une nouvelle livraison
     */
    @FXML
    private JFXButton boutonAjouterLivraison;
    /**
     * Bouton permettant la suppresion de la livraison sélectionnée dans le
     * tableau
     */
    @FXML
    private JFXButton boutonSupprimerLivraison;

    // Snackbar
    /**
     * Snackbar : petit module apparaissant et disparaissant en affichant des
     * messages d'information ou des messages d'erreur
     */
    @FXML
    private JFXSnackbar snackbar;

    /* Code des élements d'architecture */
    /**
     * Liste de l'entrepôt à afficher, formaté en {@link EntrepotVue}
     */
    private ObservableList<EntrepotVue> observableEntrepot = FXCollections.observableArrayList();
    /**
     * Liste des livraisons à afficher, formatées en {@link LivraisonVue}
     */
    private ObservableList<LivraisonVue> observableListeLivraisons = FXCollections.observableArrayList();
    /**
     * Controlleur principal de l'application
     */
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

	// Binding du tableau de l'entrepôt
	entrepotTreeTableView
		.setRoot(new RecursiveTreeItem<EntrepotVue>(observableEntrepot, RecursiveTreeObject::getChildren));
	entrepotTreeTableView.setShowRoot(false);

	// Binding du tableau de la liste des livraisons
	livraisonTreeTableView.setRoot(
		new RecursiveTreeItem<LivraisonVue>(observableListeLivraisons, RecursiveTreeObject::getChildren));
	livraisonTreeTableView.setShowRoot(false);

	// Binding des boutons et recherche de la liste des livraisons
	treeTableViewCount.textProperty()
		.bind(Bindings.createStringBinding(
			() -> "(total : " + livraisonTreeTableView.getCurrentItemsCount() + ")",
			livraisonTreeTableView.currentItemsCountProperty()));
	boutonSupprimerLivraison.disableProperty()
		.bind(Bindings.equal(-1, livraisonTreeTableView.getSelectionModel().selectedIndexProperty()));
	boutonAjouterLivraison.setOnMouseClicked((e) -> {
	    DialogNouvelleLivraison.show(this, root);
	});
	boutonSupprimerLivraison.setOnMouseClicked((e) -> {
	    Livraison livraisonSupprimer = livraisonTreeTableView.getSelectionModel().selectedItemProperty().get()
		    .getValue().livraison;
	    controlleur.supprimerLivraison(livraisonSupprimer);
	    miseAJourLivraison(controlleur.getTournee().getLivraisonsTSP());
	});
	searchField.textProperty().addListener((o, oldVal, newVal) -> {
	    livraisonTreeTableView.setPredicate(person -> person.getValue().intersection.get().contains(newVal)
		    || person.getValue().duree.get().contains(newVal));
	});

	// Affectation de a snackbar
	snackbar.registerSnackbarContainer(root);
    }

    /**
     * Appelé quand l'uilisateur clique sur le bouton "Ouvrir plan"
     */
    @FXML
    private void boutonOuvrirPlan() {
	try {
	    controlleur.chargerPlan();
	    observableEntrepot.clear();
	    observableListeLivraisons.clear();
	    livraisonTreeTableView.currentItemsCountProperty().set(0);

	    // Mise à jour des boutons
	    boutonOuvrirLivraison.setVisible(true);
	    boutonCalculerTournee.setVisible(false);
	    boutonExporterTournee.setVisible(false);
	    boutonAjouterLivraison.setVisible(false);
	    boutonSupprimerLivraison.setVisible(false);
	} catch (Exception e) {
	    if (e.getMessage() != null) {
		snackbar.fireEvent(new SnackbarEvent(e.getMessage()));
	    } else {
		snackbar.fireEvent(new SnackbarEvent("Plan invalide."));
	    }
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
		controlleur.chargerDemandeLivraisons();
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
	    controlleur.calculerTournee();
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
	    controlleur.enregistrerFeuilleDeRoute();
	} catch (Exception e) {
	    snackbar.fireEvent(new SnackbarEvent("Export annulé."));
	}
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
     * Met à jour le tableau de l'entrepôt
     */
    public void miseAJourEntrepot(Entrepot entrepot) {
	observableEntrepot.clear();
	observableEntrepot.add(new EntrepotVue(entrepot));
    }
}
