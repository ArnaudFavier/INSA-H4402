package agile.controlleur;

import agile.modele.DemandeLivraisons;
import agile.modele.Livraison;
import agile.modele.Plan;
import agile.modele.Temps;
import agile.modele.Tournee;

public interface Etat {
	public Plan chargerPlan(Controlleur controlleur, Historique historique) throws Exception;

	public DemandeLivraisons chargerDemandeLivraisons(Controlleur controlleur) throws Exception;

	public Tournee calculerTournee(Controlleur controlleur);

	public void enregistrerFeuilleDeRoute(Controlleur controlleur) throws Exception;

	public void ajouterLivraison(Controlleur controlleur, Livraison livraison);

	public void modifierLivraison(Controlleur controlleur, int idLivraison, Temps debutPlage, Temps finPlage);

	public void supprimerLivraison(Controlleur controlleur, Livraison livraison);

	public void undo(Historique historique);

	public void redo(Historique historique);
}