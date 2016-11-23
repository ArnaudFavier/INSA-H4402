package agile.vue;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import agile.modele.Livraison;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Livraison format�e pour l'affichage
 */
public class LivraisonVue extends RecursiveTreeObject<LivraisonVue> {

	StringProperty intersection;
	StringProperty duree;
	StringProperty plageDebut;
	StringProperty plageFin;

	public LivraisonVue(/*String intersection, String duree, String plageDebut, String plageFin*/Livraison livraison) {
		
		
		this.intersection = new SimpleStringProperty(livraison.getIntersection().getId()+"");
		//this.duree = new SimpleStringProperty(duree);
		//this.plageDebut = new SimpleStringProperty(plageDebut);
		//this.plageFin = new SimpleStringProperty(plageFin);
	}

}
