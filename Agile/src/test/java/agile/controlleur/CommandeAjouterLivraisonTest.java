package agile.controlleur;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

public class CommandeAjouterLivraisonTest {

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
    public void testCommandeAjouterLivraison() {
	CommandeAjouterLivraison cAjoutLivraison = new CommandeAjouterLivraison(tournee, livraison);
	assertNotNull("Tournee n'est pas nulle", cAjoutLivraison.tournee);
	assertNotNull("Livraison n'est pas nulle", cAjoutLivraison.livraison);
	assertNotNull("PrevTournee n'est pas nulle", cAjoutLivraison.prevTournee);
	assertEquals(false, cAjoutLivraison.success);
	assertEquals(false, cAjoutLivraison.alreadyAdd);

    }

    @Test
    public void testDoCde() {
	CommandeAjouterLivraison cAjoutLivraison = new CommandeAjouterLivraison(tournee, livraison);
	assertNull(controlleur.getTournee());
	cAjoutLivraison.doCde(controlleur);
	assertEquals(tournee, controlleur.getTournee());
	assertEquals(true, cAjoutLivraison.success);
	assertEquals(true, cAjoutLivraison.alreadyAdd);
    }

    @Test
    public void testUndoCde() {
	CommandeAjouterLivraison cAjoutLivraison = new CommandeAjouterLivraison(tournee, livraison);
	cAjoutLivraison.doCde(controlleur);
	cAjoutLivraison.undoCde(controlleur);
	assertEquals(cAjoutLivraison.prevTournee, controlleur.getTournee());
    }

    @Test
    public void testIsSuccess() {
	CommandeAjouterLivraison cAjoutLivraison = new CommandeAjouterLivraison(tournee, livraison);
	assertEquals(false, cAjoutLivraison.isSuccess());
	cAjoutLivraison.doCde(controlleur);
	assertEquals(true, cAjoutLivraison.isSuccess());
    }

}
