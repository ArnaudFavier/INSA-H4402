package agile.vue;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import agile.modele.Livraison;
import agile.modele.Temps;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Livraison formatée pour l'affichage
 */
public class LivraisonVue extends RecursiveTreeObject<LivraisonVue> {

    Livraison livraison;

    StringProperty intersection;
    StringProperty duree;
    StringProperty plagePrevisionnelle;
    StringProperty heureArrivee;
    StringProperty tempsAttente;

    public LivraisonVue(Livraison livraison) {
	this.livraison = livraison;
	update();
    }

    public void update() {
	this.intersection = new SimpleStringProperty(livraison.getIntersection().getId() + " ("
		+ livraison.getIntersection().getX() + ", " + livraison.getIntersection().getY() + ")");
	this.duree = new SimpleStringProperty(String.valueOf(livraison.getDuree()));

	String plagePrevisionnelle = "";
	if (livraison.getDebutPlage() != null) {
	    plagePrevisionnelle += livraison.getDebutPlage().toString();
	    if (livraison.getFinPlage() != null) {
		plagePrevisionnelle += " - " + livraison.getFinPlage().toString();
	    } else {
		plagePrevisionnelle += " - ?";
	    }
	} else if (livraison.getFinPlage() != null) {
	    plagePrevisionnelle = "? - " + livraison.getFinPlage().toString();
	}
	this.plagePrevisionnelle = new SimpleStringProperty(plagePrevisionnelle);
	this.heureArrivee = livraison.getHeureArrivee() == null ? new SimpleStringProperty("")
		: new SimpleStringProperty(livraison.getHeureArrivee().getTimeString());

	this.tempsAttente = new SimpleStringProperty(new Temps((int) (livraison.getTempsAttente())).getTimeString());
    }
}
