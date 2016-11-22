package agile.pathfinding;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import agile.modele.Chemin;
import agile.modele.DemandeLivraisons;
import agile.modele.Livraison;
import agile.modele.Plan;
import agile.xml.DeserialiseurDemandeLivraisonsXML;
import agile.xml.DeserialiseurPlanXML;

public class DjikstraTest {

	@Test(timeout = 1000)
	public void testChargerDemandeDeLivraison()
			throws URISyntaxException, SAXException, ParserConfigurationException, IOException {
		URL urlPlan = getClass().getResource("plan25x25.xml");
		File xmlPlan = new File(urlPlan.toURI());
		Plan plan = DeserialiseurPlanXML.charger(xmlPlan);

		URL urlDemandeLivraison = getClass().getResource("livraisons25x25-19.xml");
		File xmlDemandeLivraison = new File(urlDemandeLivraison.toURI());
		DemandeLivraisons demandeLivraison = DeserialiseurDemandeLivraisonsXML.charger(xmlDemandeLivraison, plan);

		for (Livraison livraison1 : demandeLivraison.getLivraisons()) {
			for (Livraison livraison2 : demandeLivraison.getLivraisons()) {
				if (livraison1 != livraison2) {
					Chemin chemin = Djikstra.plusCourtChemin(livraison1.getIntersection(),
							livraison2.getIntersection());
				}
			}
		}
	}

}
