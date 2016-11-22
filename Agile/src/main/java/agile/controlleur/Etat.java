package agile.controlleur;

import agile.modele.Chemin;
import agile.modele.DemandeLivraisons;
import agile.modele.Plan;

public interface Etat {
	public Plan chargerPlan(Controlleur controlleur, Historique historique) throws Exception;

	public DemandeLivraisons chargerDemandeLivraisons(Controlleur controlleur) throws Exception;

	public Chemin calculerTournee(Controlleur controlleur);

	public void genererFeuilleDeRoute(Controlleur controlleur);

	public void enregistrerFeuilleDeRoute(Controlleur controlleur);

	public void ajouterLivraison(Controlleur controlleur);

	public void modifierLivraison(Controlleur controlleur);

	public void undo(Historique historique);

	public void redo(Historique historique);
}