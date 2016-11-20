package agile;

import agile.vue.Fenetre;
import agile.controlleur.Controlleur;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	
	Fenetre fenetre;
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		fenetre = new Fenetre(primaryStage);
		new Controlleur(fenetre);
	}
}
