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
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Classe Fenêtre affichant la fenêtre graphique JavaFX avec JFoenix. Se charge
 * du contour de la fenêtre, ainsi que de la toolbar
 */
public class Fenetre {

    /**
     * Stage principale de la fenêtre
     */
    private Stage stage;

    /**
     * ViewFlowContext permet de gérer facilement plusieurs vues grâce au
     * context
     */
    @FXMLViewFlowContext
    private ViewFlowContext flowContext;

    /**
     * Contructeur recevant la primaryStage ainsi que le controlleur (issus du
     * Main)
     * 
     * @param stage
     *            primaryStage
     * @param controlleur
     *            Controlleur principal de l'application
     */
    public Fenetre(Stage stage, Controlleur controlleur) {
	this.stage = stage;

	try {
	    initialisation(controlleur);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Méthode d'initialisation séparée pour une meilleure organisation
     * 
     * @param controlleur
     *            Controlleur principal de l'application
     * @throws IOException
     */
    private void initialisation(Controlleur controlleur) throws IOException {
	// Définition du Flow
	Flow flow = new Flow(FenetreController.class);
	DefaultFlowContainer container = new DefaultFlowContainer();
	flowContext = new ViewFlowContext();
	flowContext.register("Stage", this.stage);

	try {
	    flow.createHandler(flowContext).start(container);
	} catch (FlowException e) {
	    e.printStackTrace();
	}

	// Contour de la fenêtre
	JFXDecorator decorator = new JFXDecorator(this.stage, container.getView());
	decorator.setCustomMaximize(true);
	Scene scene = new Scene(decorator, 818, 600);
	// Styles CSS
	scene.getStylesheets().add(getClass().getResource("../../css/jfoenix-design.css").toExternalForm());
	scene.getStylesheets().add(getClass().getResource("../../css/jfoenix-main-demo.css").toExternalForm());
	// Police d'écriture
	Font.loadFont(getClass().getResource("../../fonts/Roboto-Regular.ttf").toExternalForm(), 16);
	Font.loadFont(getClass().getResource("../../fonts/Roboto-Medium.ttf").toExternalForm(), 16);
	Font.loadFont(getClass().getResource("../../fonts/Roboto-Light.ttf").toExternalForm(), 16);

	// Options de la fenêtre
	this.stage.setTitle("PLD Agile - H4402");
	this.stage.getIcons().add(new Image("file:src/main/resources/icon.png"));
	this.stage.setMinWidth(800);
	this.stage.setMinHeight(600);
	this.stage.setScene(scene);
	this.stage.show();

	// Informations transmisses au contenu de la fenêtre
	ContentController.controlleur = controlleur;
    }
}