package agile.controlleur;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

import agile.modele.Livraison;
import agile.modele.Tournee;

public class EtatTourneeCalculee extends EtatDefaut {

	public EtatTourneeCalculee() {
	}

	@Override
	public void enregistrerFeuilleDeRoute(Controlleur controlleur) {
		Tournee tournee = controlleur.getTournee();
		String chemin = "src/main/resources/";
		String nomFichier = "tournee.txt";
		String fichier = chemin + nomFichier;
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fichier)));

			for (Livraison livraison : tournee.getLivraisonsTSP()) {
				out.println(livraison.getIntersection().getId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Enregistrement de la feuille de route");
	}

}