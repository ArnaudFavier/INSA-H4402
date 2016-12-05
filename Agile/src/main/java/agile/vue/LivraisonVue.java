package agile.vue;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import agile.modele.Livraison;
import agile.modele.Temps;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Container formattant les données d'une livraison pour les afficher dans un
 * tableau (TreeTableView)
 */
public class LivraisonVue extends RecursiveTreeObject<LivraisonVue> {

    /**
     * Référence vers la livraison du modèle
     */
    Livraison livraison;
    /**
     * ordre de la livraison après le calcul de la tournée
     */
    int numOrdre;

    // Informations à afficher
    StringProperty ordre;
    StringProperty intersection;
    StringProperty duree;
    StringProperty plagePrevisionnelle;
    StringProperty heureArrivee;
    StringProperty tempsAttente;

    /**
     * Constructeur d'une vue de livraison recevant en paramètre la livraison à
     * afficher. Se charge de garder une référence sur cette livraison puis
     * appelle la méthode {@link update()}
     * 
     * @param livraison
     *            Livraison à afficher
     */
    public LivraisonVue(Livraison livraison, int ordre) {
	this.livraison = livraison;
	this.numOrdre = ordre;
	update();
    }

    /**
     * Méthode permettant de lier les informations à afficher de l'attribut
     * {@link livraison}
     */
    public void update() {
	if (numOrdre == 0) {
	    this.ordre = new SimpleStringProperty("-");
	} else {
	    this.ordre = new SimpleStringProperty(Integer.toString(numOrdre));
	}

	this.intersection = new SimpleStringProperty(livraison.getIntersection().getId() + " ("
		+ livraison.getIntersection().getX() + ", " + livraison.getIntersection().getY() + ")");

	this.duree = new SimpleStringProperty(new Temps(livraison.getDuree()).getTimeString());

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
		: new SimpleStringProperty(livraison.getHeureArrivee().toString());

	this.tempsAttente = new SimpleStringProperty(new Temps((int) (livraison.getTempsAttente())).getTimeString());
    }
}
