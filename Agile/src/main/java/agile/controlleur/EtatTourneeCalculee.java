package agile.controlleur;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import agile.modele.Chemin;
import agile.modele.Entrepot;
import agile.modele.Livraison;
import agile.modele.Tournee;
import agile.modele.Troncon;
import javafx.stage.FileChooser;

public class EtatTourneeCalculee extends EtatDefaut {

	// private static String NOM_FICHIER = "tournee";
	// private static String EXTENSION_FICHIER = ".txt";
	// private static String CHEMIN = "src/main/resources/";

	public EtatTourneeCalculee() {
	}

	@Override
	public void enregistrerFeuilleDeRoute(Controlleur controlleur) {

		FileChooser fileChooserTXT = new FileChooser();
		fileChooserTXT.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichier texte", "*.txt"));
		File fichier = fileChooserTXT.showSaveDialog(null);

		if (fichier.equals(null)) {
			System.out.println("Erreur: Aucun fichier pour l'export n'a été séléctionné.");
		} else {
			if (fichier != null) {
				try {
					fichier.createNewFile();
				} catch (IOException e) {
					System.err.println("Impossible de créer le fichier d'export " + fichier.toString());
					System.err.println(e.getMessage());
					e.printStackTrace();
				}
			}

			Tournee tournee = controlleur.getTournee();

			// SimpleDateFormat formatDate = new
			// SimpleDateFormat("dd-MM-yyyy_HH-mm");
			// String date = formatDate.format(new Date());
			// String nomFichier = NOM_FICHIER + date + EXTENSION_FICHIER;
			// String fichier = CHEMIN + nomFichier;

			try {
				// Création et ouverture du fichier texte
				PrintWriter out = new PrintWriter(new FileWriter(fichier));

				// En-tête
				out.println("\t\t\tTOURNEE");
				SimpleDateFormat formatDate = new SimpleDateFormat("dd MMMM yyyy 'à' hh:mm:ss");
				String date = formatDate.format(new Date());
				out.println("Editée le: " + date);
				out.println();

				// Contenu

				Entrepot entrepot = tournee.getDemandeInitiale().getEntrepot();
				int etapeActuelle = 1;
				String derniereRue = null;

				out.println("Départ de l'entrepôt[" + entrepot.getIntersection().getId() + "] à "
						+ entrepot.getHeureDepart().toString() + ".");

				Iterator<Livraison> iterator = tournee.getLivraisonsTSP().iterator();

				for (Chemin chemin : tournee.getCheminsTSP()) {
					// Description du chemin
					for (Troncon troncon : chemin.getTroncons()) {
						out.print(etapeActuelle + ". ");
						if (troncon.getNomRue().equals(derniereRue)) {
							out.print("Continuer sur ");
						} else {
							out.print("Prendre ");
						}
						out.println(troncon.getNomRue() + ".");
						derniereRue = troncon.getNomRue();
						etapeActuelle++;
					}

					// Description de la livraison
					if (iterator.hasNext()) {
						Livraison livraison = iterator.next();
						out.print("\t====> Effectuer la livraison à " + livraison.getIntersection().getId());
						if (livraison.ContrainteDeTemps() == true) {
							out.print(" entre " + livraison.getDebutPlage().toString() + " et "
									+ livraison.getFinPlage().toString());
						}
						out.println(". [Durée de la livraison: " + livraison.getDuree() + " secondes]");
						derniereRue = null;
					}
				}

				out.println("\t====> Arrivée à l'entrepôt[" + entrepot.getIntersection().getId() + "].");
				out.println();
				out.println("Fin de la tournée.");

				// Fermeture du fichier
				out.close();
				System.out.println("La tournée a été exportée dans " + fichier.toString() + ".");
			} catch (Exception e) {
				System.err.println("Erreur lors de l'export de la tournée");
				System.err.println(e.getMessage());
				e.printStackTrace();
			}

			System.out.println("Enregistrement de la feuille de route");
		}
	}

}