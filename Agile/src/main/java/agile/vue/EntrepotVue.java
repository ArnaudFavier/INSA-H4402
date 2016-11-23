package agile.vue;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import agile.modele.Entrepot;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EntrepotVue extends RecursiveTreeObject<EntrepotVue> {

	StringProperty intersection;
	StringProperty heureDepart;

	public EntrepotVue(Entrepot entrepot) {
		this.intersection = new SimpleStringProperty(entrepot.getIntersection().getId() + " ("
				+ entrepot.getIntersection().getX() + ", " + entrepot.getIntersection().getY() + ")");
		this.heureDepart = new SimpleStringProperty(entrepot.getHeureDepart().toString());
	}

}
