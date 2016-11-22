package agile.vue;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Livraison formatée pour l'affichage
 */
public class LivraisonVue extends RecursiveTreeObject<LivraisonVue> {

	StringProperty intersection;
	StringProperty duree;
	StringProperty debutPlage;
	StringProperty finPlage;

	public LivraisonVue(String intersection, String duree, String debutPlage, String finPlage) {
		this.intersection = new SimpleStringProperty(intersection);
		this.duree = new SimpleStringProperty(duree);
		this.debutPlage = new SimpleStringProperty(debutPlage);
		this.finPlage = new SimpleStringProperty(finPlage);
	}

}
