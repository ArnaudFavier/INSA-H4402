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
	StringProperty plageDebut;
	StringProperty plageFin;

	public LivraisonVue(String intersection, String duree, String plageDebut, String plageFin) {
		this.intersection = new SimpleStringProperty(intersection);
		this.duree = new SimpleStringProperty(duree);
		this.plageDebut = new SimpleStringProperty(plageDebut);
		this.plageFin = new SimpleStringProperty(plageFin);
	}

}
