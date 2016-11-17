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

public class DeserialiseurPlanXML extends DefaultHandler {
    private static final String RESEAU_NODE_NAME = "reseau";
    private static final String NOEUD_NODE_NAME = "noeud";
    private static final String TRONCON_NODE_NAME = "troncon";

    private static final String ID_ATTR_NAME = "id";
    private static final String X_ATTR_NAME = "x";
    private static final String Y_ATTR_NAME = "y";
    private static final String DESTINATION_ATTR_NAME = "destination";
    private static final String LONGUEUR_ATTR_NAME = "longueur";
    private static final String NOMRUE_ATTR_NAME = "nomRue";
    private static final String ORIGINE_ATTR_NAME = "origine";
    private static final String VITESSE_ATTR_NAME = "vitesse";

    private Plan plan;

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

    public static Plan charger() throws SAXException, ParserConfigurationException, IOException {
	File xml = OuvreurDeFichierXml.getInstance().ouvre();
	return charger(xml);
    }

    public static Plan charger(File file) throws SAXException, ParserConfigurationException, IOException {
	SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

	SAXParser saxParser = saxParserFactory.newSAXParser();
	DeserialiseurPlanXML handler = new DeserialiseurPlanXML();
	saxParser.parse(file, handler);
	return handler.plan;
    }

}
