package agile.xml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import agile.modele.Intersection;
import agile.modele.Plan;
import agile.modele.Troncon;

/**
 * La classe permettant le parsage XML (SAX) des fichiers de plan
 */
public class DeserialiseurPlanXML extends DefaultHandler {
    /**
     * Le nom du noeud xml reseau
     */
    private static final String RESEAU_NODE_NAME = "reseau";

    /**
     * Le nom du noeud xml noeud
     */
    private static final String NOEUD_NODE_NAME = "noeud";

    /**
     * Le nom du noeud xml troncon
     */
    private static final String TRONCON_NODE_NAME = "troncon";

    /**
     * Le nom de l'attribut xml id
     */
    private static final String ID_ATTR_NAME = "id";

    /**
     * Le nom de l'attribut xml x
     */
    private static final String X_ATTR_NAME = "x";

    /**
     * Le nom de l'attribut xml y
     */
    private static final String Y_ATTR_NAME = "y";

    /**
     * Le nom de l'attribut xml destination
     */
    private static final String DESTINATION_ATTR_NAME = "destination";

    /**
     * Le nom de l'attribut xml longueur
     */
    private static final String LONGUEUR_ATTR_NAME = "longueur";

    /**
     * Le nom de l'attribut xml nomRue
     */
    private static final String NOMRUE_ATTR_NAME = "nomRue";

    /**
     * Le nom de l'attribut xml origine
     */
    private static final String ORIGINE_ATTR_NAME = "origine";

    /**
     * Le nom de l'attribut xml vitesse
     */
    private static final String VITESSE_ATTR_NAME = "vitesse";

    /**
     * Le plan créé à la fin du parsing
     */
    private Plan plan;

    /**
     * Les intersections en fonction des ids, utilisé pendant le parsing
     */
    private Map<Integer, Intersection> intersectionParId;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
	if (qName.equalsIgnoreCase(RESEAU_NODE_NAME)) {
	    plan = new Plan();
	    intersectionParId = new HashMap<>();
	} else if (qName.equalsIgnoreCase(NOEUD_NODE_NAME)) {
	    int id = Integer.parseInt(attributes.getValue(ID_ATTR_NAME));
	    int x = Integer.parseInt(attributes.getValue(X_ATTR_NAME));
	    int y = Integer.parseInt(attributes.getValue(Y_ATTR_NAME));
	    Intersection intersection = new Intersection(id, x, y);
	    intersectionParId.put(id, intersection);
	    plan.addIntersection(intersection);
	} else if (qName.equalsIgnoreCase(TRONCON_NODE_NAME)) {
	    int destination = Integer.parseInt(attributes.getValue(DESTINATION_ATTR_NAME));
	    int longueur = Integer.parseInt(attributes.getValue(LONGUEUR_ATTR_NAME));
	    String nomRue = attributes.getValue(NOMRUE_ATTR_NAME);
	    int origine = Integer.parseInt(attributes.getValue(ORIGINE_ATTR_NAME));
	    int vitesse = Integer.parseInt(attributes.getValue(VITESSE_ATTR_NAME));

	    Intersection node1 = intersectionParId.get(origine);
	    Intersection node2 = intersectionParId.get(destination);
	    Troncon troncon = new Troncon(longueur, vitesse, nomRue, node1, node2);
	    plan.addTroncon(troncon);
	}
    }

    private DeserialiseurPlanXML() {

    }

    /**
     * Permet le chargement d'un plan, ouvre une fenêtre FileChooser dans
     * laquelle il faut choisir un xml de plan
     * 
     * @return Le plan créé à partir du xml
     * @throws SAXException
     *             Renvoyé si une erreur de parsage est détectée par SAX
     * @throws ParserConfigurationException
     * @throws IOException
     *             Renvoyé si il y a une erreur d'ouverture du fichier
     */
    public static Plan charger() throws SAXException, ParserConfigurationException, IOException {
	File xml = OuvreurDeFichierXml.getInstance().ouvre();
	return charger(xml);
    }

    /**
     * Permet le chargement d'un plan a partir d'un fichier passé en paramètre
     * 
     * @param file
     *            Le fichier duquel on charge le plan
     * @return Le plan créé à partir du xml
     * @throws SAXException
     *             Renvoyé si une erreur de parsage est détectée par SAX
     * @throws ParserConfigurationException
     * @throws IOException
     *             Renvoyé si il y a une erreur d'ouverture du fichier
     */
    public static Plan charger(File file) throws SAXException, ParserConfigurationException, IOException {
	SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

	SAXParser saxParser = saxParserFactory.newSAXParser();
	DeserialiseurPlanXML handler = new DeserialiseurPlanXML();
	saxParser.parse(file, handler);
	return handler.plan;
    }

}
