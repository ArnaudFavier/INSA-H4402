package agile.controlleur;

import agile.modele.Plan;

public interface Etat {
	public Plan chargerPlan(Controlleur controlleur);

	public void chargerDemandeLivraison(Controlleur controlleur);

	public void calculerTournee(Controlleur controlleur);

	public void genererFeuilleDeRoute(Controlleur controlleur);

	public void enregistrerFeuilleDeRoute(Controlleur controlleur);

	public void ajouterLivraison(Controlleur controlleur);

	public void modifierLivraison(Controlleur controlleur);

	public void undo(Historique historique);

	public void redo(Historique historique);
}