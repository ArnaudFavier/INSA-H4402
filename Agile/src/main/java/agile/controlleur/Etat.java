package agile.controlleur;

import agile.modele.DemandeLivraisons;
import agile.modele.Plan;
import agile.modele.Tournee;

public interface Etat {
	public Plan chargerPlan(Controlleur controlleur, Historique historique) throws Exception;

	public DemandeLivraisons chargerDemandeLivraisons(Controlleur controlleur) throws Exception;

	public Tournee calculerTournee(Controlleur controlleur);

	public void enregistrerFeuilleDeRoute(Controlleur controlleur);

	public void ajouterLivraison(Controlleur controlleur);

	public void modifierLivraison(Controlleur controlleur);

	public void undo(Historique historique);

	public void redo(Historique historique);
}