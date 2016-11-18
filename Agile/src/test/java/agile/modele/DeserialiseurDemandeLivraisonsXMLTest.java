package agile.modele;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import agile.xml.DeserialiseurDemandeLivraisonsXML;
import agile.xml.DeserialiseurPlanXML;

public class DeserialiseurDemandeLivraisonsXMLTest {

    @Test
    public void testChargerDemandeDeLivraison()
	    throws URISyntaxException, SAXException, ParserConfigurationException, IOException {
	URL urlPlan = getClass().getResource("plan5x5.xml");
	File xmlPlan = new File(urlPlan.toURI());
	Plan plan = DeserialiseurPlanXML.charger(xmlPlan);

	URL urlDemandeLivraison = getClass().getResource("livraisons5x5-4.xml");
	File xmlDemandeLivraison = new File(urlDemandeLivraison.toURI());
	DemandeLivraisons demandeLivraison = DeserialiseurDemandeLivraisonsXML.charger(xmlDemandeLivraison, plan);

	assertEquals(demandeLivraison.getEntrepot().getIntersection().getId(), 21);
	assertTrue(demandeLivraison.getEntrepot().getHeureDepart().equals(new Temps(8, 0, 0)));

	assertEquals(demandeLivraison.getLivraisons().size(), 4);
	
	assertEquals(demandeLivraison.getLivraisons().get(0).getIntersection().getId(), 1);
	assertEquals(demandeLivraison.getLivraisons().get(0).getDuree(), 900);
	
	assertEquals(demandeLivraison.getLivraisons().get(1).getIntersection().getId(), 9);
	assertEquals(demandeLivraison.getLivraisons().get(1).getDuree(), 600);
	
	assertEquals(demandeLivraison.getLivraisons().get(2).getIntersection().getId(), 3);
	assertEquals(demandeLivraison.getLivraisons().get(2).getDuree(), 600);
	
	assertEquals(demandeLivraison.getLivraisons().get(3).getIntersection().getId(), 13);
	assertEquals(demandeLivraison.getLivraisons().get(3).getDuree(), 900);
    }
}
