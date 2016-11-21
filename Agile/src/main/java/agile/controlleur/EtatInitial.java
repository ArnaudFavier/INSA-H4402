package agile.controlleur;

public class EtatInitial extends EtatDefaut {

	public EtatInitial() {
	}

	@Override
	public void undo(Historique historique) {
		historique.undo();
	}

	@Override
	public void redo(Historique historique) {
		historique.redo();
	}

}