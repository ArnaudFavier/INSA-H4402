package agile.vue;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;

import agile.modele.Livraison;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public final class DialogModifierLivraison {
    private DialogModifierLivraison() {
    }

    public static void show(ContentController controlleur, StackPane stackPane, Livraison livraison) {
	Label labelPlageDebut = new Label("Plade horaire début :");
	Label labelPlageFin = new Label("Plade horaire fin :");
	JFXDatePicker datePickerPlageDebut = new JFXDatePicker();
	JFXDatePicker datePickerPlageFin = new JFXDatePicker();
	JFXButton boutonValider = new JFXButton("Valider");
	JFXButton boutonAnnuler = new JFXButton("Annuler");

	datePickerPlageDebut.setPromptText("Début de la plage");
	datePickerPlageFin.setPromptText("Fin de la plage");
	datePickerPlageDebut.setShowTime(true);
	datePickerPlageFin.setShowTime(true);
	datePickerPlageDebut.setDefaultColor(Color.web("#3f51b5"));
	datePickerPlageFin.setDefaultColor(Color.web("#3f51b5"));

	GridPane grid = new GridPane();
	grid.add(labelPlageDebut, 1, 1);
	grid.add(labelPlageFin, 2, 1);
	grid.add(datePickerPlageDebut, 1, 2);
	grid.add(datePickerPlageFin, 2, 2);

	grid.setHgap(10);
	grid.setVgap(10);
	grid.setPadding(new Insets(5, 10, 10, 10));

	boutonValider.getStyleClass().add("red-A400");
	boutonAnnuler.getStyleClass().add("red-A400");

	HBox hbox = new HBox();
	hbox.setPadding(new Insets(0, 10, 10, 10));
	hbox.setSpacing(10);
	boutonValider.setMinWidth(100);
	boutonAnnuler.setMinWidth(100);
	hbox.getChildren().add(boutonValider);
	hbox.getChildren().add(boutonAnnuler);
	hbox.setAlignment(Pos.CENTER);

	VBox vbox = new VBox();
	vbox.setPadding(new Insets(5, 10, 0, 10));
	Text title = new Text("Modifier une livraison");
	title.setFont(Font.font(null, FontWeight.BOLD, 16));
	vbox.getChildren().add(title);
	vbox.getChildren().add(grid);
	vbox.getChildren().add(hbox);

	JFXDialog dialog = new JFXDialog(stackPane, vbox, JFXDialog.DialogTransition.CENTER);

	boutonAnnuler.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		dialog.close();
	    }
	});

	boutonValider.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		// TODO

		dialog.close();
	    }
	});

	dialog.show();
    }
}
