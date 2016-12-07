package agile.controlleur;

import java.io.File;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import agile.modele.DemandeLivraisons;
import agile.modele.Livraison;
import agile.modele.Plan;
import agile.modele.Tournee;
import agile.xml.DeserialiseurDemandeLivraisonsXML;
import agile.xml.DeserialiseurPlanXML;

public class CommandeModifierLivraisonTest {

    Tournee tournee;
    DemandeLivraisons demandeLivraisons;
    Livraison livraison;
    Controlleur controlleur;

    @Before
    public void setUp() throws Exception {

	URL urlPlan = getClass().getResource("plan5x5.xml");
	File xmlPlan = new File(urlPlan.toURI());
	Plan plan = DeserialiseurPlanXML.charger(xmlPlan);

	URL urlDemandeLivraison = getClass().getResource("livraisons5x5-4.xml");
	File xmlDemandeLivraison = new File(urlDemandeLivraison.toURI());
	demandeLivraisons = DeserialiseurDemandeLivraisonsXML.charger(xmlDemandeLivraison, plan);

	tournee = new Tournee(demandeLivraisons);

	tournee.calculerTSP();
	livraison = tournee.getLivraisonsTSP().get(0);
	controlleur = new Controlleur();
    }

    @Test
    public void testCommandeModifierLivraison() {
    }

    @Test
    public void testDoCde() {

    }

    @Test
    public void testUndoCde() {

    }

}
