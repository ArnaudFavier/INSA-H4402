package agile.vue;

import java.io.IOException;

import agile.controlleur.FenetreControlleur;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Fenetre {

	private Stage stage;

	public Fenetre(Stage stage) {
		this.stage = stage;

		try {
			initialisation();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void initialisation() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("fenetre.fxml"));

		this.stage.setTitle("PLD Agile - H4402");
		this.stage.setScene(new Scene(loader.load(), 300, 275));
		this.stage.show();

		// Give the controller access to the main
		FenetreControlleur fenetreControlleur = loader.getController();
		fenetreControlleur.setFenetre(this);

	}

}