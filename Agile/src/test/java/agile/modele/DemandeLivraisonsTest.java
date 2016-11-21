package agile.modele;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import agile.xml.DeserialiseurDemandeLivraisonsXML;
import agile.xml.DeserialiseurPlanXML;

public class DemandeLivraisonsTest {

    @Test(timeout = 10000)
    public void testChargerDemandeDeLivraison()
	    throws URISyntaxException, SAXException, ParserConfigurationException, IOException {
	URL urlPlan = getClass().getResource("plan5x5.xml");
	File xmlPlan = new File(urlPlan.toURI());
	Plan plan = DeserialiseurPlanXML.charger(xmlPlan);

	URL urlDemandeLivraison = getClass().getResource("livraisons5x5-4.xml");
	File xmlDemandeLivraison = new File(urlDemandeLivraison.toURI());
	DemandeLivraisons demandeLivraison = DeserialiseurDemandeLivraisonsXML.charger(xmlDemandeLivraison, plan);

	DemandeLivraisonsTraitee demandeLivraisonsTraitee = new DemandeLivraisonsTraitee(demandeLivraison);
	demandeLivraisonsTraitee.calculerTSP();

	List<Intersection> intersections = demandeLivraisonsTraitee.getListeIntersectionsTSP();
	List<Integer> intersectionsId = new ArrayList<>();
	for (Intersection intersection : intersections) {
	    intersectionsId.add(intersection.getId());
	}
	
	List<Integer> expectedIntersectionsId = new ArrayList<>();
	expectedIntersectionsId.add(21);
	expectedIntersectionsId.add(16);
	expectedIntersectionsId.add(11);
	expectedIntersectionsId.add(12);
	expectedIntersectionsId.add(13);
	expectedIntersectionsId.add(8);
	expectedIntersectionsId.add(7);
	expectedIntersectionsId.add(2);
	expectedIntersectionsId.add(3);
	expectedIntersectionsId.add(4);
	expectedIntersectionsId.add(9);
	expectedIntersectionsId.add(4);
	expectedIntersectionsId.add(3);
	expectedIntersectionsId.add(2);
	expectedIntersectionsId.add(1);
	expectedIntersectionsId.add(0);
	expectedIntersectionsId.add(5);
	expectedIntersectionsId.add(10);
	expectedIntersectionsId.add(11);
	expectedIntersectionsId.add(16);
	expectedIntersectionsId.add(21);
	
	assertEquals(expectedIntersectionsId, intersectionsId);
    }
}
