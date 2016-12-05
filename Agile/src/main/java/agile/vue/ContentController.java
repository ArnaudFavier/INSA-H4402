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
import javafx.scene.control.TreeItem;
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
     * Adresse de l'entrepôt
     */
    @FXML
    private Label entrepotAdresse;
    /**
     * Heure de départ de l'entrepôt
     */
    @FXML
    private Label entrepotHeureDepart;

    /**
     * Heure de retour du tableau de l'entrepôt
     */
    @FXML
    private Label entrepotHeureRetour;

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
    /**
     * Bouton undo premettant l'annulation d'une édition de livraison
     */
    @FXML
    private JFXButton boutonUndo;
    /**
     * Bouton redo premettant de refaire une action d'une édition de livraison
     */
    @FXML
    private JFXButton boutonRedo;

    // Snackbar
    /**
     * Snackbar : petit module apparaissant et disparaissant en affichant des
     * messages d'information ou des messages d'erreur
     */
    @FXML
    private JFXSnackbar snackbar;

    /* Code des élements d'architecture */
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

	colonneHeureArrivee.setCellValueFactory((TreeTableColumn.CellDataFeatures<LivraisonVue, String> param) -> {
	    if (colonneDuree.validateValue(param))
		return param.getValue().getValue().heureArrivee;
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

	// Modifier livraison
	colonnePlagePrevisionnelle.setOnEditStart((e) -> {
	    Livraison livraisonModifiee = livraisonTreeTableView.getSelectionModel().selectedItemProperty().get()
		    .getValue().livraison;
	    DialogModifierLivraison.show(this, root, livraisonModifiee);
	    miseAJourLivraison(controlleur.getTournee().getLivraisonsTSP());
	});

	// Binding du tableau de la liste des livraisons
	livraisonTreeTableView.setRoot(
		new RecursiveTreeItem<LivraisonVue>(observableListeLivraisons, RecursiveTreeObject::getChildren));
	livraisonTreeTableView.setShowRoot(false);
	livraisonTreeTableView.setEditable(true);

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

	    JFXTreeTableView livraisonTreeTableViewCopy = livraisonTreeTableView;
	    livraisonTreeTableView.setPredicate(livraison -> livraison.getValue().intersection.get().contains(newVal)
		    || livraison.getValue().duree.get().contains(newVal));

	    livraisonTreeTableView.setOnMouseClicked((e) -> {
		LivraisonVue livraisonVue = livraisonTreeTableView.getSelectionModel().selectedItemProperty().get()
			.getValue();

		int intersectionSearch = livraisonVue.getLivraison().getIntersection().getX();
		int i = 0;
		TreeItem<LivraisonVue> item = livraisonTreeTableView.getTreeItem(0);

		livraisonTreeTableView.setPredicate(null);

		while (item != null
			&& (item.getValue().getLivraison().getIntersection().getX() != intersectionSearch)) {
		    System.out.println(item.getValue().intersection.get().equals(intersectionSearch));
		    i++;
		    item = livraisonTreeTableView.getTreeItem(i);
		}

		System.out.println(i);

		selectionnerLivraison(i);

		// livraisonTreeTableView.getSelectionModel()
		// .select(livraisonTreeTableView.getSelectionModel().selectedItemProperty().get());

	    });

	    /*
	     * Afficher la bare en bas : non
	     * TreeTableViewSelectionModel<LivraisonVue> ttvsm =
	     * livraisonTreeTableView.getSelectionModel(); List<Integer> indices
	     * = new ArrayList<Integer>(); if (newVal.endsWith(" ")) {
	     * System.out.println("VALEUR recherchee : " + newVal.substring(0,
	     * newVal.length() - 1)); int i = 0; TreeItem<LivraisonVue> item =
	     * ttvsm.getModelItem(0); while (item != null) { item =
	     * ttvsm.getModelItem(i); if
	     * (item.getValue().intersection.get().contains(newVal.substring(0,
	     * newVal.length() - 1)) ||
	     * ttvsm.getModelItem(i).getValue().duree.get()
	     * .contains(newVal.substring(0, newVal.length() - 1))) {
	     * indices.add(i); // 3 fois pour 600 et 2 fois pour 14 } i++; item
	     * = ttvsm.getModelItem(i); }
	     * 
	     * String result =
	     * "Les r�sultats de la recherche sont les lignes : " +
	     * indices.toString(); afficherMessage(result); }
	     */

	    // for (int j : indices) {
	    // livraisonTreeTableView.getFocusModel().focus(j);
	    // }

	});

	// Binding des boutons undo/redo
	boutonUndo.setOnMouseClicked((e) -> {
	    controlleur.undo();
	    miseAJourLivraison(controlleur.getTournee().getLivraisonsTSP());
	});
	boutonRedo.setOnMouseClicked((e) -> {
	    controlleur.redo();
	    miseAJourLivraison(controlleur.getTournee().getLivraisonsTSP());
	});

	// Affectation de la snackbar
	snackbar.registerSnackbarContainer(root);
    }

    /**
     * Appelé quand l'uilisateur clique sur le bouton "Ouvrir plan"
     */
    @FXML
    private void boutonOuvrirPlan() {
	try {
	    controlleur.chargerPlan();
	    this.effacerAffichageEntrepot();
	    observableListeLivraisons.clear();
	    livraisonTreeTableView.currentItemsCountProperty().set(0);

	    // Mise à jour des boutons
	    boutonOuvrirLivraison.setVisible(true);
	    boutonCalculerTournee.setVisible(false);
	    boutonExporterTournee.setVisible(false);
	    boutonAjouterLivraison.setVisible(false);
	    boutonSupprimerLivraison.setVisible(false);
	    boutonUndo.setVisible(false);
	    boutonRedo.setVisible(false);
	    colonnePlagePrevisionnelle.setEditable(false);
	} catch (Exception e) {
	    if (e.getMessage() != null) {
		afficherMessage(e.getMessage());
	    } else {
		afficherMessage("Plan invalide.");
	    }
	}
    }

    /**
     * Appelé quand l'uilisateur clique sur le bouton "Ouvrir livraison"
     */
    @FXML
    private void boutonOuvrirLivraison() {
	if (controlleur.getPlan() == null) {
	    afficherMessage("Merci de sélectionner un plan avant une demande de livraisons.");
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
		boutonUndo.setVisible(false);
		boutonRedo.setVisible(false);
		colonnePlagePrevisionnelle.setEditable(false);
	    } catch (Exception e) {
		afficherMessage("Demande de livraisons invalide.");
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
	    miseAJourEntrepot(controlleur.getTournee().getDemandeInitiale().getEntrepot());
	    System.out.println("tmps: " + controlleur.getTournee().getLivraisonsTSP().get(0).getTempsAttente());

	    // Mise à jour des boutons
	    boutonOuvrirLivraison.setVisible(true);
	    boutonCalculerTournee.setVisible(true);
	    boutonExporterTournee.setVisible(true);
	    boutonAjouterLivraison.setVisible(true);
	    boutonSupprimerLivraison.setVisible(true);
	    boutonUndo.setVisible(true);
	    boutonRedo.setVisible(true);
	    colonnePlagePrevisionnelle.setEditable(true);
	    afficherMessage("Tounée calculée.");
	} catch (Exception e) {
	    afficherMessage("Calcul de tournée impossible.");
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
	    afficherMessage("Export annulé.");
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

    public void selectionnerLivraison(int index) {
	livraisonTreeTableView.getSelectionModel().select(index);
	livraisonTreeTableView.getFocusModel().focus(index);
	livraisonTreeTableView.scrollTo(index);
    }

    public void selectionnerLivraison(List<Integer> indexes) {
	for (int index : indexes) {
	    selectionnerLivraison(index);
	}
    }

    /**
     * 
     * Met à jour les données pour l'affichage de l'entrepôt
     */
    public void miseAJourEntrepot(Entrepot entrepot) {
	entrepotAdresse.setText(Integer.toString(entrepot.getIntersection().getId()) + " ("
		+ entrepot.getIntersection().getX() + ", " + entrepot.getIntersection().getY() + ")");
	entrepotHeureDepart.setText(entrepot.getHeureDepart().toString());
	entrepotHeureRetour.setText(entrepot.getHeureRetour() == null ? "" : entrepot.getHeureRetour().toString());
	entrepotAdresse.setVisible(true);
	entrepotHeureDepart.setVisible(true);
	entrepotHeureRetour.setVisible(true);
    }

    /**
     * Efface l'affichage l'entrepôt
     */
    public void effacerAffichageEntrepot() {
	entrepotAdresse.setVisible(false);
	entrepotHeureDepart.setVisible(false);
	entrepotHeureRetour.setVisible(false);
	entrepotAdresse.setText(null);
	entrepotHeureDepart.setText(null);
	entrepotHeureRetour.setText(null);
    }

    /**
     * Affiche en bas de l'écran sous la forme d'une Snackbar le message passé
     * en paramètre
     * 
     * @param message
     *            Le message à afficher
     */
    public void afficherMessage(String message) {
	if (message == null)
	    return;
	snackbar.fireEvent(new SnackbarEvent(message));
    }
}
