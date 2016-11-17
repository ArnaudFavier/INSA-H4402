package agile.modele;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import agile.xml.DeserialiseurPlanXML;

public class DeserialiseurPlanXMLTest {

    @Test
    public void testChargerPlan() throws URISyntaxException, SAXException, ParserConfigurationException, IOException {
	URL url = getClass().getResource("microPlan.xml");
	File xml = new File(url.toURI());
	Plan plan = DeserialiseurPlanXML.charger(xml);

	assertEquals(plan.getTroncons().size(), 1);
	assertEquals(plan.getIntersections().size(), 2);

	assertEquals(plan.getIntersections().get(0).getId(), 0);
	assertEquals(plan.getIntersections().get(0).getX(), 134);
	assertEquals(plan.getIntersections().get(0).getY(), 193);
	
	assertEquals(plan.getIntersections().get(1).getId(), 1);
	assertEquals(plan.getIntersections().get(1).getX(), 195);
	assertEquals(plan.getIntersections().get(1).getY(), 291);
	
	assertEquals(plan.getTroncons().get(0).getLongueur(), 9234);
	assertEquals(plan.getTroncons().get(0).getNomRue(), "v0");
	assertEquals(plan.getTroncons().get(0).getVitesse(), 41);
	assertEquals(plan.getTroncons().get(0).getOrigine().getId(), 0);
	assertEquals(plan.getTroncons().get(0).getDestination().getId(), 1);
    }

}
