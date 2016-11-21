package agile.vue;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class VuePlan implements Observer {

	public static void AffichageTest(AnchorPane pane) {
		// Tronçon
		Line troncon = new Line(20, 20, 20, 100);
		troncon.setStroke(Color.GRAY);

		// Intersection
		Circle intersection = new Circle();
		intersection.setCenterX(20);
		intersection.setCenterY(20);
		intersection.setRadius(1);
		intersection.setFill(Color.GRAY);

		// Livraison
		Circle livraison = new Circle();
		livraison.setCenterX(20);
		livraison.setCenterY(100);
		livraison.setRadius(2);
		livraison.setFill(Color.BLUE);

		// Entrepôt
		Circle entrepot = new Circle();
		entrepot.setCenterX(50);
		entrepot.setCenterY(50);
		entrepot.setRadius(2);
		entrepot.setFill(Color.RED);

		// Tronçon d'une tournée
		Line chemin = new Line(20, 20, 50, 50);
		chemin.setStroke(Color.GREEN);

		// Ajout au groupe
		pane.getChildren().add(troncon);
		pane.getChildren().add(chemin);
		pane.getChildren().add(intersection);
		pane.getChildren().add(livraison);
		pane.getChildren().add(entrepot);
	}
}
