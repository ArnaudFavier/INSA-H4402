package agile.vue;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import agile.modele.Entrepot;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Container formattant les données d'un entrepôt pour les afficher dans un
 * tableau (TreeTableView)
 */
public class EntrepotVue extends RecursiveTreeObject<EntrepotVue> {
    // Informations à afficher
    public StringProperty intersection;
    public StringProperty heureDepart;

    /**
     * Constructeur liant les informations à afficher de l'entrepôt passé en
     * paramètre
     * 
     * @param entrepot
     *            Entrepôt à afficher
     */
    public EntrepotVue(Entrepot entrepot) {
	this.intersection = new SimpleStringProperty(entrepot.getIntersection().getId() + " ("
		+ entrepot.getIntersection().getX() + ", " + entrepot.getIntersection().getY() + ")");
	this.heureDepart = new SimpleStringProperty(entrepot.getHeureDepart().toString());
    }
}