package agile.vue;

import java.io.IOException;

import com.jfoenix.controls.JFXDecorator;

import agile.controlleur.Controlleur;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Fenetre {

    private Stage stage;

    @FXMLViewFlowContext
    private ViewFlowContext flowContext;

    public Fenetre(Stage stage, Controlleur controlleur) {
	this.stage = stage;

	try {
	    initialisation(controlleur);
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    private void initialisation(Controlleur controlleur) throws IOException {
	Flow flow = new Flow(FenetreController.class);
	DefaultFlowContainer container = new DefaultFlowContainer();
	flowContext = new ViewFlowContext();
	flowContext.register("Stage", this.stage);

	try {
	    flow.createHandler(flowContext).start(container);
	} catch (FlowException e) {
	    e.printStackTrace();
	}

	JFXDecorator decorator = new JFXDecorator(this.stage, container.getView());
	decorator.setCustomMaximize(true);
	Scene scene = new Scene(decorator, 800, 600);
	scene.getStylesheets().add(getClass().getResource("../../css/jfoenix-design.css").toExternalForm());
	scene.getStylesheets().add(getClass().getResource("../../css/jfoenix-main-demo.css").toExternalForm());
	this.stage.setTitle("PLD Agile - H4402");
	this.stage.getIcons().add(new Image("file:src/main/resources/icon.png"));
	this.stage.setMinWidth(800);
	this.stage.setMinHeight(600);
	this.stage.setScene(scene);
	this.stage.show();

	ContentController.fenetre = this;
	ContentController.controlleur = controlleur;
    }

    public Stage getStage() {
	return this.stage;
    }

}