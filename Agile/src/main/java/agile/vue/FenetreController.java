package agile.vue;

import javax.annotation.PostConstruct;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
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

@FXMLController(value = "Fenetre.fxml")
public class FenetreController {

	@FXMLViewFlowContext
	private ViewFlowContext context;

	@FXML
	private StackPane root;

	@FXML
	private StackPane titleBurgerContainer;
	@FXML
	private JFXHamburger titleBurger;

	@FXML
	private StackPane optionsBurger;
	@FXML
	private JFXRippler optionsRippler;

	@FXML
	private JFXDrawer drawer;
	@FXML
	private JFXPopup toolbarPopup;
	@FXML
	private Label exit;
	@FXML
	private Label aPropos;
	@FXML
	private JFXDialog dialogAPropos;
	@FXML
	private JFXButton boutonRetourAPropos;

	private FlowHandler flowHandler;

	@PostConstruct
	public void init() throws FlowException, VetoException {
		// Les options
		toolbarPopup.setPopupContainer(root);
		toolbarPopup.setSource(optionsRippler);
		root.getChildren().remove(toolbarPopup);

		optionsBurger.setOnMouseClicked((e) -> {
			toolbarPopup.show(PopupVPosition.TOP, PopupHPosition.RIGHT, -12, 15);
		});

		// Option "Quitter" qui ferme l'application
		exit.setOnMouseClicked((e) -> {
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

		// create the inner flow and content
		context = new ViewFlowContext();
		// set the default controller
		Flow innerFlow = new Flow(ContentController.class);

		flowHandler = innerFlow.createHandler(context);
		context.register("ContentFlowHandler", flowHandler);
		context.register("ContentFlow", innerFlow);
		drawer.setContent(flowHandler.start(new io.datafx.controller.flow.container.AnimatedFlowContainer(
				Duration.millis(320), ContainerAnimations.SWIPE_LEFT)));
		context.register("ContentPane", drawer.getContent().get(0));
	}
}
