package agile;

import agile.controlleur.Controlleur;
import agile.vue.Fenetre;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * La classe Main hérite de Application (JavaFX)
 *
 */
public class Main extends Application {

	/**
	 * La fenêtre graphique principale de l'application
	 */
	private Fenetre fenetre;

	/**
	 * Première méthode appelée, lance la méthode start()
	 * 
	 * @param args
	 *            non utilisé
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Instancie le controlleur général de l'application et la fenêtre graphique
	 * 
	 * @param primaryStage
	 *            Première stage de l'application (générée par JavaFX)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Controlleur controlleur = new Controlleur();
		fenetre = new Fenetre(primaryStage, controlleur);
	}
}
