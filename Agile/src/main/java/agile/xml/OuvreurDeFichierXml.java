package agile.xml;

import java.io.File;

import javafx.stage.FileChooser;

/**
 * La classe permettant permettant l'affichage d'un FileChooser proposant des
 * fichiers xml Utilisé par les Deserialiseur de plan et de demandes de
 * livraisons
 * 
 * @see DeserialiseurPlanXML
 * @see DeserialiseurDemandeLivraisonsXML
 */
public final class OuvreurDeFichierXml {

	/**
	 * L'instance du Singleton
	 */
	private static OuvreurDeFichierXml instance = null;

	/**
	 * Le chemin du répertoire initial qui sera ouvert par le fileChooser
	 */
	private static String CHEMIN_REPERTOIRE_INITIAL = "src/main/resources/";

	private OuvreurDeFichierXml() {
	}

	/**
	 * @return L'instance du Singleton
	 */
	protected static OuvreurDeFichierXml getInstance() {
		if (instance == null)
			instance = new OuvreurDeFichierXml();
		return instance;
	}

	/**
	 * Permet l'affichage d'un FileChooser proposant des fichiers xml
	 * 
	 * @return Le fichier selectionné par l'utilisateur
	 */
	public File ouvre() {
		FileChooser fileChooserXML = new FileChooser();
		fileChooserXML.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichier XML", "*.xml"));
		fileChooserXML.setInitialDirectory(new File(CHEMIN_REPERTOIRE_INITIAL));
		return fileChooserXML.showOpenDialog(null);
	}
}
