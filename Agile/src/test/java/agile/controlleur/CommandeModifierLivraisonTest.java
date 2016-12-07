package agile.controlleur;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import agile.modele.DemandeLivraisons;
import agile.modele.Livraison;
import agile.modele.Plan;
import agile.modele.Temps;
import agile.modele.Tournee;
import agile.xml.DeserialiseurDemandeLivraisonsXML;
import agile.xml.DeserialiseurPlanXML;

public class CommandeModifierLivraisonTest {

    Tournee tournee;
    DemandeLivraisons demandeLivraisons;
    Livraison livraison;
    Controlleur controlleur;
    Temps debutPlage, finPlage;

    @Before
    public void setUp() throws Exception {

	URL urlPlan = getClass().getResource("plan5x5.xml");
	File xmlPlan = new File(urlPlan.toURI());
	Plan plan = DeserialiseurPlanXML.charger(xmlPlan);

	URL urlDemandeLivraison = getClass().getResource("livraisons5x5-4.xml");
	File xmlDemandeLivraison = new File(urlDemandeLivraison.toURI());
	demandeLivraisons = DeserialiseurDemandeLivraisonsXML.charger(xmlDemandeLivraison, plan);

	tournee = new Tournee(demandeLivraisons);

	debutPlage = new Temps(8, 0, 0);
	finPlage = new Temps(11, 0, 0);

	tournee.calculerTSP();
	livraison = tournee.getLivraisonsTSP().get(0);
	controlleur = new Controlleur();
    }

    @Test
    public void testCommandeModifierLivraison() {
	CommandeModifierLivraison cModifLivraison = new CommandeModifierLivraison(tournee, livraison, debutPlage,
		finPlage);
	assertNotNull("Tournee n'est pas nulle", cModifLivraison.tournee);
	assertNotNull("Livraison n'est pas nulle", cModifLivraison.livraison);
	assertNotNull("DebutPlage n'est pas nulle", cModifLivraison.debutPlage);
	assertNotNull("FinPlage n'est pas nulle", cModifLivraison.finPlage);
	assertEquals(livraison.getDebutPlage(), cModifLivraison.oldDebutPlage);
	assertEquals(livraison.getFinPlage(), cModifLivraison.oldFinPlage);
    }

    @Test
    public void testDoCde() {
	CommandeModifierLivraison cModifLivraison = new CommandeModifierLivraison(tournee, livraison, debutPlage,
		finPlage);
	int idLivraison = tournee.getLivraisonsTSP().indexOf(livraison);

	cModifLivraison.doCde(controlleur);
	assertEquals(debutPlage, tournee.getLivraisonsTSP().get(idLivraison).getDebutPlage());
	assertEquals(finPlage, tournee.getLivraisonsTSP().get(idLivraison).getFinPlage());

    }

    @Test
    public void testUndoCde() {
	CommandeModifierLivraison cModifLivraison = new CommandeModifierLivraison(tournee, livraison, debutPlage,
		finPlage);
	int idLivraison = tournee.getLivraisonsTSP().indexOf(livraison);

	cModifLivraison.doCde(controlleur);
	cModifLivraison.undoCde(controlleur);
	assertEquals(null, tournee.getLivraisonsTSP().get(idLivraison).getDebutPlage());
	assertEquals(null, tournee.getLivraisonsTSP().get(idLivraison).getFinPlage());
    }

}
