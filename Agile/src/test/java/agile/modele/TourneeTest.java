package agile.modele;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import agile.xml.DeserialiseurDemandeLivraisonsXML;
import agile.xml.DeserialiseurPlanXML;

public class TourneeTest {

    DemandeLivraisons demandeLivraison;

    @Before
    public void setUp() throws URISyntaxException, SAXException, ParserConfigurationException, IOException {
	URL urlPlan = getClass().getResource("plan5x5.xml");
	File xmlPlan = new File(urlPlan.toURI());
	Plan plan = DeserialiseurPlanXML.charger(xmlPlan);

	URL urlDemandeLivraison = getClass().getResource("livraisons5x5-4.xml");
	File xmlDemandeLivraison = new File(urlDemandeLivraison.toURI());
	demandeLivraison = DeserialiseurDemandeLivraisonsXML.charger(xmlDemandeLivraison, plan);
    }

    @Test(timeout = 10000)
    public void testCalculTournee() throws Exception {
	Tournee tournee = new Tournee(demandeLivraison);
	tournee.calculerTSP();

	List<Intersection> intersections = tournee.getListeIntersectionsTSP();
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

    @Test(timeout = 10000)
    public void testClone() throws Exception {
	Tournee tournee = new Tournee(demandeLivraison);
	tournee.calculerTSP();
	Tournee copy = tournee.clone();

	assertNotNull(copy);
	assertNotNull(copy.getCheminsTSP());

	int indexChemin = 0;
	for (Chemin c : tournee.getCheminsTSP()) {
	    int indexIntersection = 0;
	    for (Intersection i : c.getIntersections())
		assertEquals(i, copy.getCheminsTSP().get(indexChemin).getIntersections().get(indexIntersection++));
	    int indexTroncon = 0;
	    for (Troncon t : c.getTroncons())
		assertEquals(t, copy.getCheminsTSP().get(indexChemin).getTroncons().get(indexTroncon++));
	    ++indexChemin;
	}

	assertNotNull(copy.getLivraisonsTSP());
	assertEquals(tournee.getLivraisonsTSP(), copy.getLivraisonsTSP());

	assertNotNull(copy.matriceChemin);
	Arrays.deepEquals(tournee.matriceChemin, copy.matriceChemin);

	assertNotNull(copy.matriceCout);
	Arrays.deepEquals(tournee.matriceCout, copy.matriceCout);
    }

    @Test(timeout = 10000)
    public void testSupprimerLivraison() throws Exception {
	Tournee tournee1 = new Tournee(demandeLivraison);
	tournee1.calculerTSP();
	Tournee tournee2 = tournee1.clone();
	Tournee tournee3 = tournee1.clone();

	Livraison premiereLivraison = tournee1.getLivraisonsTSP().get(0);
	Livraison derniereLivraison = tournee1.getLivraisonsTSP().get(tournee1.getLivraisonsTSP().size() - 1);
	Livraison autreLivraison = tournee1.getLivraisonsTSP().get(1);

	// Test suppression de la premiere livraison
	tournee1.supprimerLivraison(premiereLivraison);

	List<Intersection> intersections1 = tournee1.getListeIntersectionsTSP();
	List<Integer> intersectionsId1 = new ArrayList<>();
	for (Intersection intersection : intersections1) {
	    intersectionsId1.add(intersection.getId());
	}

	List<Integer> expectedIntersectionsId1 = new ArrayList<>();
	expectedIntersectionsId1.add(21);
	expectedIntersectionsId1.add(16);
	expectedIntersectionsId1.add(11);
	expectedIntersectionsId1.add(12);
	expectedIntersectionsId1.add(7);
	expectedIntersectionsId1.add(2);
	expectedIntersectionsId1.add(3);
	expectedIntersectionsId1.add(4);
	expectedIntersectionsId1.add(9);
	expectedIntersectionsId1.add(4);
	expectedIntersectionsId1.add(3);
	expectedIntersectionsId1.add(2);
	expectedIntersectionsId1.add(1);
	expectedIntersectionsId1.add(0);
	expectedIntersectionsId1.add(5);
	expectedIntersectionsId1.add(10);
	expectedIntersectionsId1.add(11);
	expectedIntersectionsId1.add(16);
	expectedIntersectionsId1.add(21);
	assertEquals(expectedIntersectionsId1, intersectionsId1);

	// Test suppression de la derniere livraison

	tournee2.supprimerLivraison(derniereLivraison);

	List<Intersection> intersections2 = tournee2.getListeIntersectionsTSP();
	List<Integer> intersectionsId2 = new ArrayList<>();
	for (Intersection intersection : intersections2) {
	    intersectionsId2.add(intersection.getId());
	}

	List<Integer> expectedIntersectionsId2 = new ArrayList<>();
	expectedIntersectionsId2.add(21);
	expectedIntersectionsId2.add(16);
	expectedIntersectionsId2.add(11);
	expectedIntersectionsId2.add(12);
	expectedIntersectionsId2.add(13);
	expectedIntersectionsId2.add(8);
	expectedIntersectionsId2.add(7);
	expectedIntersectionsId2.add(2);
	expectedIntersectionsId2.add(3);
	expectedIntersectionsId2.add(4);
	expectedIntersectionsId2.add(9);
	expectedIntersectionsId2.add(4);
	expectedIntersectionsId2.add(3);
	expectedIntersectionsId2.add(2);
	expectedIntersectionsId2.add(1);
	expectedIntersectionsId2.add(0);
	expectedIntersectionsId2.add(5);
	expectedIntersectionsId2.add(10);
	expectedIntersectionsId2.add(11);
	expectedIntersectionsId2.add(16);
	expectedIntersectionsId2.add(21);
	assertEquals(expectedIntersectionsId2, intersectionsId2);

	// Test suppression d'une autre livraison
	tournee3.supprimerLivraison(autreLivraison);

	List<Intersection> intersections3 = tournee3.getListeIntersectionsTSP();
	List<Integer> intersectionsId3 = new ArrayList<>();
	for (Intersection intersection : intersections3) {
	    intersectionsId3.add(intersection.getId());
	}

	List<Integer> expectedIntersectionsId3 = new ArrayList<>();
	expectedIntersectionsId3.add(21);
	expectedIntersectionsId3.add(16);
	expectedIntersectionsId3.add(11);
	expectedIntersectionsId3.add(12);
	expectedIntersectionsId3.add(13);
	expectedIntersectionsId3.add(8);
	expectedIntersectionsId3.add(7);
	expectedIntersectionsId3.add(2);
	expectedIntersectionsId3.add(3);
	expectedIntersectionsId3.add(2);
	expectedIntersectionsId3.add(1);
	expectedIntersectionsId3.add(0);
	expectedIntersectionsId3.add(5);
	expectedIntersectionsId3.add(10);
	expectedIntersectionsId3.add(11);
	expectedIntersectionsId3.add(16);
	expectedIntersectionsId3.add(21);
	assertEquals(expectedIntersectionsId3, intersectionsId3);
    }
}
