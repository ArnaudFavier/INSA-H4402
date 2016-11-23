package agile.controlleur;

<<<<<<< Updated upstream
import java.io.File;
=======
import java.io.BufferedWriter;
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream

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
=======
		Tournee tournee = controlleur.getTournee();

		SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy_HH-mm");
		String date = formatDate.format(new Date());
		String nomFichier = NOM_FICHIER + date + EXTENSION_FICHIER;
		String fichier = CHEMIN + nomFichier;

		try {
			// Création et ouverture du fichier texte
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fichier)));

			// En-tête
			out.println("\t\t\tTOURNEE");
			formatDate = new SimpleDateFormat("dd MMMM yyyy 'à' hh:mm:ss");
			date = formatDate.format(new Date());
			out.println("Editée le: " + date);
			out.println();

			// Contenu

			// -----------TEST----------- //
			// intersections
			Intersection it0 = new Intersection(1, 50, 50); // entrepot
			Intersection it1 = new Intersection(3, 10, 56); // livraison 1
			Intersection it4 = new Intersection(8, 10, 10);
			Intersection it2 = new Intersection(25, 99, 24); // livraison 2
			Intersection it5 = new Intersection(9, 0, 10);
			Intersection it6 = new Intersection(30, 27, 69);
			Intersection it3 = new Intersection(37, 0, 0); // livraison 3
			// Tonçons
			Troncon tr1 = new Troncon(4, 40, "rue Insa", it0, it1);
			Troncon tr2 = new Troncon(4, 40, "rue Bob", it1, it4);
			Troncon tr3 = new Troncon(4, 40, "rue Bob", it4, it5);
			Troncon tr4 = new Troncon(4, 40, "avenue Dussauge", it5, it2);
			Troncon tr5 = new Troncon(4, 40, "champ Favier", it2, it6);
			Troncon tr6 = new Troncon(4, 40, "rue Moll", it6, it3);
			Troncon tr7 = new Troncon(4, 40, "Avenu Delay", it3, it0);
			// entrepot
			Entrepot entrepot = new Entrepot(new Temps(8, 0, 0), it0);
			// livraisons
			List<Livraison> livraisons = new ArrayList<Livraison>();
			livraisons.add(new Livraison(10, it1));
			livraisons.add(new Livraison(7, it2, new Temps(10, 30, 27), new Temps(11, 0, 0)));
			livraisons.add(new Livraison(4, it3));
			// Listes Intersection
			List<Intersection> intersections1 = new ArrayList<Intersection>();
			intersections1.add(it0);
			intersections1.add(it1);
			List<Intersection> intersections2 = new ArrayList<Intersection>();
			intersections2.add(it1);
			intersections2.add(it4);
			intersections2.add(it5);
			intersections2.add(it2);
			List<Intersection> intersections3 = new ArrayList<Intersection>();
			intersections3.add(it2);
			intersections3.add(it6);
			intersections3.add(it3);
			List<Intersection> intersections4 = new ArrayList<Intersection>();
			intersections4.add(it3);
			intersections4.add(it0);
			// Listes tronçons
			List<Troncon> troncons1 = new ArrayList<Troncon>();
			troncons1.add(tr1);
			List<Troncon> troncons2 = new ArrayList<Troncon>();
			troncons2.add(tr2);
			troncons2.add(tr3);
			troncons2.add(tr4);
			List<Troncon> troncons3 = new ArrayList<Troncon>();
			troncons3.add(tr5);
			troncons3.add(tr6);
			List<Troncon> troncons4 = new ArrayList<Troncon>();
			troncons4.add(tr7);
			// Liste Chemins
			List<Chemin> chemins = new ArrayList<Chemin>();
			chemins.add(new Chemin(troncons1, intersections1, 1));
			chemins.add(new Chemin(troncons2, intersections2, 1));
			chemins.add(new Chemin(troncons3, intersections3, 1));
			chemins.add(new Chemin(troncons4, intersections4, 1));

			Iterator<Livraison> iterator = livraisons.iterator();
			// ---------FIN-TEST--------- //

			// Entrepot entrepot = tournee.getEntrepot();
			int etapeActuelle = 1;
			String derniereRue = null;

			out.println("Départ de l'entrepôt[" + entrepot.getIntersection().getId() + "] à "
					+ entrepot.getHeureDepart().toString() + ".");

			// Iterator<Livraison> iterator =
			// tournee.getLivraisonsTSP().iterator();

			// for (Chemin chemin : tournee.getCheminsTSP()) {
			for (Chemin chemin : chemins) {
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
>>>>>>> Stashed changes
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

	@Override
	public void supprimerLivraison(Controlleur controlleur, Livraison livraison) {
		controlleur.getTournee().supprimerLivraison(livraison);
	}
}