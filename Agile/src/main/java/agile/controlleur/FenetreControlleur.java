package agile.controlleur;

import agile.modele.Plan;
import agile.vue.Fenetre;
import agile.vue.VuePlan;
import agile.xml.DeserialiseurPlanXML;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class FenetreControlleur {

	@FXML
	private AnchorPane dessinTest;

	// Reference to the fenetre.
	private Fenetre fenetre;

	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	public FenetreControlleur() {
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		VuePlan.AffichageTest(dessinTest);
	}

	/**
	 * Called when the user clicks on the "Ouvrir un plan" button.
	 */
	@FXML
	private void handleOuvrirPlan() {
		try {
			Plan plan = DeserialiseurPlanXML.charger();
			System.out.println(plan);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Is called by the fenetre to give a reference back to itself.
	 * 
	 * @param fenetre
	 */
	public void setFenetre(Fenetre fenetre) {
		this.fenetre = fenetre;

	}

}
