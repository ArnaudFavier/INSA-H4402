package agile.vue;

//import java.awt.Color;
import java.io.IOException;

import agile.controlleur.Controlleur;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Fenetre {

	private Stage stage;

	public Fenetre(Stage stage, Controlleur controlleur) {
		this.stage = stage;

		try {
			initialisation(controlleur);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void initialisation(Controlleur controlleur) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("fenetre.fxml"));

		this.stage.setTitle("PLD Agile - H4402");
		this.stage.setMinWidth(600);
		this.stage.setMinHeight(400);
		this.stage.getIcons().add(new Image("file:src/main/resources/icon.png"));
		this.stage.setScene(new Scene(loader.load()));
		this.stage.show();

		// Give the controller access to the main
		FenetreEcouteur fenetreControlleur = loader.getController();
		fenetreControlleur.setFenetre(this);
		fenetreControlleur.setControlleur(controlleur);

	}

}