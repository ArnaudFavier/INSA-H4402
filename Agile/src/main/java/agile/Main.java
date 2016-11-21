package agile;

import agile.controlleur.Controlleur;
import agile.vue.Fenetre;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	Fenetre fenetre;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Controlleur controlleur = new Controlleur();
		fenetre = new Fenetre(primaryStage, controlleur);
	}
}
