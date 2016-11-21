package agile.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import agile.modele.DemandeLivraisons;
import agile.modele.Entrepot;
import agile.modele.Livraison;
import agile.modele.Plan;
import agile.modele.Temps;

/**
 * La classe permettant le parsage XML (SAX) des fichiers de demande de
 * livraisons
 */
public class DeserialiseurDemandeLivraisonsXML extends DefaultHandler {

	/**
	 * Le nom du noeud xml demandeDeLivraisons
	 */
	private static final String DEMANDE_LIVRAISON_NODE_NAME = "demandeDeLivraisons";

	/**
	 * Le nom du noeud xml entrepot
	 */
	private static final String ENTREPOT_NODE_NAME = "entrepot";

	/**
	 * Le nom du noeud xml livraison
	 */
	private static final String LIVRAISON_NODE_NAME = "livraison";

	/**
	 * Le nom de l'attribut xml adresse
	 */
	private static final String ADRESSE_ATTR_NAME = "adresse";

	/**
	 * Le nom de l'attribut xml heureDepart
	 */
	private static final String HEURE_DEPART_ATTR_NAME = "heureDepart";

	/**
	 * Le nom de l'attribut xml duree
	 */
	private static final String DUREE_ATTR_NAME = "duree";

	/**
	 * Le nom de l'attribut xml debutPlage
	 */
	private static final String DEBUT_PLAGE = "debutPlage";

	/**
	 * Le nom de l'aittribut xml finPlage
	 */
	private static final String FIN_PLAGE = "finPlage";

	/**
	 * Le s�parateur de chaine utilis� dans les heures du xml
	 */
	private static final String HEURE_SEPARATEUR = ":";

	/**
	 * La liste des livraisons utilis�e pendant le parsing du xml
	 */
	private List<Livraison> livraisons;

	/**
	 * L'entrepot initialis� pendant le parsing du xml
	 */
	private Entrepot entrepot;

	/**
	 * Le plan auquel la demande de livraison se r�f�re
	 */
	private Plan plan;

	/**
	 * Le constructeur (priv�, manipul� par la classe)
	 * 
	 * @param plan
	 *            Le plan auquel la demande de livraison se r�f�re
	 */
	private DeserialiseurDemandeLivraisonsXML(Plan plan) {
		this.plan = plan;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase(DEMANDE_LIVRAISON_NODE_NAME)) {
			livraisons = new ArrayList<>();
		} else if (qName.equalsIgnoreCase(ENTREPOT_NODE_NAME)) {
			int id = Integer.parseInt(attributes.getValue(ADRESSE_ATTR_NAME));
			String heureDepartString = attributes.getValue(HEURE_DEPART_ATTR_NAME);
			Temps heureDepart = parseHeure(heureDepartString);

			entrepot = new Entrepot(heureDepart, plan.getIntersectionParId(id));
		} else if (qName.equalsIgnoreCase(LIVRAISON_NODE_NAME)) {
			int id = Integer.parseInt(attributes.getValue(ADRESSE_ATTR_NAME));
			int duree = Integer.parseInt(attributes.getValue(DUREE_ATTR_NAME));
			String debutPlageString = attributes.getValue(DEBUT_PLAGE);
			String finPlageString = attributes.getValue(FIN_PLAGE);
			Temps debutPlage = parseHeure(debutPlageString);
			Temps finPlage = parseHeure(finPlageString);

			Livraison livraison = new Livraison(duree, debutPlage, finPlage, plan.getIntersectionParId(id));
			livraisons.add(livraison);
		}
	}

	/**
	 * Permet la conversion d'une chaine repr�sentant une heure en un objet
	 * Temps
	 * 
	 * @param heureString
	 *            La chaine repr�sentant un temps
	 * @return L'objet Temps cr��
	 * @throws NumberFormatException
	 *             D�clench� la chaine contient des caract�res non num�riques
	 *             (autre que le s�parateur)
	 * @throws ArrayIndexOutOfBoundsException
	 *             D�clench� si la chaine ne contient pas assez de s�parateur
	 */
	private static Temps parseHeure(String heureString) throws NumberFormatException, ArrayIndexOutOfBoundsException {
		String[] heureDepartSplit = heureString.split(HEURE_SEPARATEUR);
		int heure = Integer.parseInt(heureDepartSplit[0]);
		int minutes = Integer.parseInt(heureDepartSplit[1]);
		int secondes = Integer.parseInt(heureDepartSplit[2]);
		return new Temps(heure, minutes, secondes);
	}

	/**
	 * Permet le chargement d'une demande de livraisons, ouvre une fen�tre
	 * FileChooser dans laquelle il faut choisir un xml d'une demande de
	 * livraisons
	 * 
	 * @param plan
	 *            Le plan auquel la demande de livraisons se r�f�re
	 * @return La demande de livraisons cr��e � partir du xml
	 * @throws SAXException
	 *             Renvoy� si une erreur de parsage est d�tect�e par SAX
	 * @throws ParserConfigurationException
	 * @throws IOException
	 *             Renvoy� si il y a une erreur d'ouverture du fichier
	 */
	public static DemandeLivraisons charger(Plan plan) throws SAXException, ParserConfigurationException, IOException {
		File xml = OuvreurDeFichierXml.getInstance().ouvre();

		if (xml == null) {
			return null;
		}

		return charger(xml, plan);
	}

	/**
	 * Permet le chargement d'une demande de livraisons a partir d'un fichier
	 * pass� en param�tre
	 * 
	 * @param plan
	 *            Le plan auquel la demande de livraisons se r�f�re
	 * @param file
	 *            Le fichier duquel on charge la demande de livraisons
	 * @return La demande de livraisons cr��e � partir du xml
	 * @throws SAXException
	 *             Renvoy� si une erreur de parsage est d�tect�e par SAX
	 * @throws ParserConfigurationException
	 * @throws IOException
	 *             Renvoy� si il y a une erreur d'ouverture du fichier
	 */
	public static DemandeLivraisons charger(File file, Plan plan)
			throws SAXException, ParserConfigurationException, IOException {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

		SAXParser saxParser = saxParserFactory.newSAXParser();
		DeserialiseurDemandeLivraisonsXML handler = new DeserialiseurDemandeLivraisonsXML(plan);
		saxParser.parse(file, handler);
		return new DemandeLivraisons(handler.entrepot, handler.livraisons, plan);
	}

}
