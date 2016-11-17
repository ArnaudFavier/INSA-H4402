package agile.xml;

import java.io.File;

import javafx.stage.FileChooser;

public class OuvreurDeFichierXml {
	private static OuvreurDeFichierXml instance = null;

	private OuvreurDeFichierXml() {
	}

	protected static OuvreurDeFichierXml getInstance() {
		if (instance == null)
			instance = new OuvreurDeFichierXml();
		return instance;
	}

	public File ouvre() {
		FileChooser fileChooserXML = new FileChooser();
		fileChooserXML.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichier XML", "*.xml"));
		return fileChooserXML.showOpenDialog(null);
	}
}
