package agile;

import agile.modele.Plan;
import agile.xml.DeserialiseurPlanXML;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
	launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
	Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
	primaryStage.setTitle("Hello World");
	primaryStage.setScene(new Scene(root, 300, 275));
	primaryStage.show();

	try {
	    Plan plan = DeserialiseurPlanXML.charger();
	    System.out.println(plan);
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }
}
