package agile.vue;

import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;

import agile.modele.Intersection;
import agile.modele.Livraison;
import agile.modele.Temps;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public final class DialogNouvelleLivraison {
    private DialogNouvelleLivraison() {

    }

    public static void show(ContentController controlleur, StackPane stackPane) {
	Label labelAdresse = new Label("Adresse : ");
	Label labelDuree = new Label("Durée (en secondes) : ");
	JFXTextField textAdresse = new JFXTextField();
	JFXTextField textDuree = new JFXTextField();
	JFXDatePicker datePickerPlageDebut = new JFXDatePicker();
	JFXDatePicker datePickerPlageFin = new JFXDatePicker();
	JFXCheckBox checkBoxHoraire = new JFXCheckBox("Plage horaire");

	textAdresse.setPromptText("Entrez le numéro de l'intersection");
	textDuree.setPromptText("Entrez la durée en secondes");
	datePickerPlageDebut.setPromptText("Début de la plage");
	datePickerPlageFin.setPromptText("Fin de la plage");

	// Les validateurs pour les champs textes
	// Validateur champ non-vide
	RequiredFieldValidator requiredValidator = new RequiredFieldValidator();
	requiredValidator.setMessage("Ce champs est requis");

	DureeValidator dureeValidator = new DureeValidator();
	dureeValidator.setMessage("Ce champs doit contenir une durée valide");

	// TODO: plus de validateur pour les datePicker, à voir si nécéssaire
	HoraireValidator horaireValidator = new HoraireValidator();
	horaireValidator.setMessage("Ce champs doit contenir un horaire valide");

	AdresseValidator adresseValidator = new AdresseValidator(
		ContentController.controlleur.getPlan().getIntersections());
	adresseValidator.setMessage("Ce champs doit contenir une adresse valide");

	textAdresse.getValidators().add(requiredValidator);
	textAdresse.getValidators().add(adresseValidator);

	textDuree.getValidators().add(requiredValidator);
	textDuree.getValidators().add(dureeValidator);

	// TODO: plus de validateur pour les datePicker, à voir si nécéssaire
	// textPlageDebut.getValidators().add(requiredValidator);
	// textPlageDebut.getValidators().add(horaireValidator);

	// textPlageFin.getValidators().add(requiredValidator);
	// textPlageFin.getValidators().add(horaireValidator);
	datePickerPlageDebut.setShowTime(true);
	datePickerPlageFin.setShowTime(true);

	datePickerPlageDebut.setDefaultColor(Color.web("#3f51b5"));
	datePickerPlageFin.setDefaultColor(Color.web("#3f51b5"));

	checkBoxHoraire.setCheckedColor(Color.web("#3f51b5"));

	GridPane grid = new GridPane();
	grid.add(labelAdresse, 1, 1);
	grid.add(textAdresse, 2, 1, 2, 1);
	grid.add(labelDuree, 1, 2);
	grid.add(textDuree, 2, 2, 2, 1);
	grid.add(checkBoxHoraire, 1, 3);
	grid.add(datePickerPlageDebut, 1, 4);
	grid.add(datePickerPlageFin, 2, 4);

	grid.setHgap(10);
	grid.setVgap(10);
	grid.setPadding(new Insets(5, 10, 10, 10));

	JFXButton boutonValider = new JFXButton("Valider");
	JFXButton boutonAnnuler = new JFXButton("Annuler");

	boutonValider.getStyleClass().add("red-A400");
	boutonAnnuler.getStyleClass().add("red-A400");
	datePickerPlageDebut.setDisable(true);
	datePickerPlageFin.setDisable(true);

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
	Text title = new Text("Ajouter une nouvelle livraison");
	vbox.getChildren().add(title);
	vbox.getChildren().add(grid);
	vbox.getChildren().add(hbox);

	JFXDialog dialog = new JFXDialog(stackPane, vbox, JFXDialog.DialogTransition.CENTER);

	checkBoxHoraire.setOnAction(new EventHandler<ActionEvent>() {

	    @Override
	    public void handle(ActionEvent arg0) {
		datePickerPlageDebut.setDisable(!checkBoxHoraire.isSelected());
		datePickerPlageFin.setDisable(!checkBoxHoraire.isSelected());
	    }

	});

	boutonAnnuler.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		dialog.close();
	    }
	});

	boutonValider.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {

		// Convertir les infos dans le bon type de données
		boolean error = !textAdresse.validate();
		error |= !textDuree.validate();

		if (checkBoxHoraire.isSelected()) {
		    // TODO: à améliorer car plus de validateur
		    error |= !(datePickerPlageDebut.getTime() != null);
		    error |= !(datePickerPlageFin.getTime() != null);
		}

		// Pas d'erreur, on peut sauvegarder
		if (!error) {
		    String adresseString = textAdresse.getText();
		    String dureeString = textDuree.getText();

		    int interId = Integer.parseInt(adresseString);
		    Intersection inter = ContentController.controlleur.getPlan().getIntersectionParId(interId);

		    int duree = Integer.parseInt(dureeString);

		    Livraison livraison;
		    if (checkBoxHoraire.isSelected()) {
			String plageDebutString = datePickerPlageDebut.getTime().toString();
			String plageFinString = datePickerPlageFin.getTime().toString();

			String[] timesDebut = plageDebutString.split(":");
			int heureDebut = Integer.parseInt(timesDebut[0]);
			int minutesDebut = Integer.parseInt(timesDebut[1]);

			String[] timesFin = plageFinString.split(":");
			int heureFin = Integer.parseInt(timesFin[0]);
			int minuteFin = Integer.parseInt(timesFin[1]);

			Temps plageDebut = new Temps(heureDebut, minutesDebut, 0);
			Temps plageFin = new Temps(heureFin, minuteFin, 0);

			if (plageDebut.compareTo(plageFin) > 0) {
			    return;
			}

			livraison = new Livraison(duree, inter, plageDebut, plageFin);
		    } else {
			livraison = new Livraison(duree, inter);
		    }
		    ContentController.controlleur.ajouterLivraison(livraison);
		    controlleur.miseAJourLivraison(ContentController.controlleur.getTournee().getLivraisonsTSP());
		    int indexToSelect = ContentController.controlleur.getTournee().getLivraisonsTSP()
			    .indexOf(livraison);
		    controlleur.selectionnerLivraison(indexToSelect);

		    dialog.close();
		} else {
		    System.err.println("Error found:");
		    if (datePickerPlageDebut.getTime() == null) {
			// TODO: trouver comment récupérer la valeur entrée au
			// clavier par l'utilisateur
			System.err.println("Date Picker Debut Null:");
			System.err.println(datePickerPlageDebut.getValue());
			System.err.println(datePickerPlageDebut.promptTextProperty());
			System.err.println(datePickerPlageDebut.timeProperty());
			System.err.println(datePickerPlageDebut.valueProperty());
		    }
		}
	    }
	});

	dialog.show();
    }

    private static class AdresseValidator extends ValidatorBase {
	private List<Intersection> intersections;

	public AdresseValidator(List<Intersection> intersections) {
	    super();
	    this.intersections = intersections;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void eval() {
	    if (srcControl.get() instanceof TextInputControl)
		evalTextInputField();
	}

	private void evalTextInputField() {
	    TextInputControl textField = (TextInputControl) srcControl.get();
	    try {
		String idString = textField.getText();
		int interId = Integer.parseInt(idString);
		hasErrors.set(true);
		for (Intersection inter : intersections) {
		    if (inter.getId() == interId) {
			hasErrors.set(false);
			break;
		    }
		}
	    } catch (Exception e) {
		hasErrors.set(true);
	    }
	}
    }

    private static class HoraireValidator extends ValidatorBase {
	public HoraireValidator() {
	    super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void eval() {
	    if (srcControl.get() instanceof TextInputControl)
		evalTextInputField();
	}

	private void evalTextInputField() {
	    TextInputControl textField = (TextInputControl) srcControl.get();
	    try {
		String[] times = textField.getText().split(":");
		if (times.length != 2) {
		    hasErrors.set(true);
		    return;
		}

		int heure = Integer.parseInt(times[0]);
		int minutes = Integer.parseInt(times[1]);
		hasErrors.set(heure < 0 || heure >= 24 || minutes < 0 || minutes >= 60);
	    } catch (Exception e) {
		hasErrors.set(true);
	    }
	}
    }

    private static class DureeValidator extends ValidatorBase {
	public DureeValidator() {
	    super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void eval() {
	    if (srcControl.get() instanceof TextInputControl)
		evalTextInputField();
	}

	private void evalTextInputField() {
	    TextInputControl textField = (TextInputControl) srcControl.get();
	    try {
		String timeString = textField.getText();
		int time = Integer.parseInt(timeString);
		hasErrors.set(time < 0);
	    } catch (Exception e) {
		hasErrors.set(true);
	    }
	}
    }
}
