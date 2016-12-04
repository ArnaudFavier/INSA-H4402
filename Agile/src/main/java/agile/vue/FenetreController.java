package agile.vue;

import javax.annotation.PostConstruct;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;
import com.jfoenix.controls.JFXRippler;

import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.ContainerAnimations;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * Controlleur de Fenetre.fxml en charge du contour de la fenêtre, ainsi que de
 * la toolbar
 */
@FXMLController(value = "Fenetre.fxml")
public class FenetreController {

    /**
     * Le contexte courrant à afficher
     */
    @FXMLViewFlowContext
    private ViewFlowContext context;

    /**
     * Le stackpane principal de la fenêtre
     */
    @FXML
    private StackPane root;

    /**
     * Bouton d'options de la toolbar
     */
    @FXML
    private StackPane optionsBurger;
    /**
     * Effets sur le bouton d'options
     */
    @FXML
    private JFXRippler optionsRippler;

    /**
     * Contenu de la fenêtre, espace contenant le context
     */
    @FXML
    private JFXDrawer drawer;
    /**
     * Liste des labels à afficher dans la toolbar
     */
    @FXML
    private JFXPopup toolbarPopup;
    /**
     * Label quitter de la toolbar
     */
    @FXML
    private Label quitter;
    /**
     * Label A propos de la toolbar
     */
    @FXML
    private Label aPropos;
    /**
     * Boite de dialogue A propos, avec le nom des auteurs
     */
    @FXML
    private JFXDialog dialogAPropos;
    /**
     * Bouton permetant de quitter la boite de dialogue A propos
     */
    @FXML
    private JFXButton boutonRetourAPropos;

    /**
     * Gestion du flow courant (animation si plusieurs flow, swipe à gauche...)
     */
    private FlowHandler flowHandler;

    /**
     * Méthode d'initialisiton du controlleur de la fenêtre. Appelé
     * automatiquement. Se charge d'initialiser les actions des composants de la
     * fenêtre
     * 
     * @throws FlowException
     * @throws VetoException
     */
    @PostConstruct
    public void init() throws FlowException, VetoException {
	// Les options
	toolbarPopup.setPopupContainer(root);
	toolbarPopup.setSource(optionsRippler);
	root.getChildren().remove(toolbarPopup);

	optionsBurger.setOnMouseClicked((e) -> {
	    toolbarPopup.show(PopupVPosition.TOP, PopupHPosition.RIGHT, -12, 15);
	});

	// Option "Quitter" fermant l'application
	quitter.setOnMouseClicked((e) -> {
	    Platform.exit();
	});

	// Option "A propos" avec boite de dialogue
	aPropos.setOnMouseClicked((e) -> {
	    toolbarPopup.close();
	    dialogAPropos.setTransitionType(DialogTransition.TOP);
	    dialogAPropos.show((StackPane) context.getRegisteredObject("ContentPane"));
	});
	boutonRetourAPropos.setOnMouseClicked((e) -> {
	    dialogAPropos.close();
	});

	// Créé le flow et le contenu interne
	context = new ViewFlowContext();
	// Controlleur par défaut à afficher
	Flow innerFlow = new Flow(ContentController.class);

	flowHandler = innerFlow.createHandler(context);
	context.register("ContentFlowHandler", flowHandler);
	context.register("ContentFlow", innerFlow);
	drawer.setContent(flowHandler.start(new io.datafx.controller.flow.container.AnimatedFlowContainer(
		Duration.millis(320), ContainerAnimations.SWIPE_LEFT)));
	context.register("ContentPane", drawer.getContent().get(0));
    }
}
