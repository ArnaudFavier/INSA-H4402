package agile.controlleur;

import static org.junit.Assert.assertEquals;

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

public class HistoriqueTest {

    Tournee tournee;
    Livraison livraison;
    Controlleur controlleur;
    DemandeLivraisons demandeLivraisons;
    CommandeAjouterLivraison cAjoutLivraison;
    CommandeModifierLivraison cModifLivrasion;
    CommandeSupprimerLivraison cSuppLivraison;

    @Before
    public void setUp() throws Exception {
	URL urlPlan = getClass().getResource("plan5x5.xml");
	File xmlPlan = new File(urlPlan.toURI());
	Plan plan = DeserialiseurPlanXML.charger(xmlPlan);

	URL urlDemandeLivraison = getClass().getResource("livraisons5x5-4.xml");
	File xmlDemandeLivraison = new File(urlDemandeLivraison.toURI());
	demandeLivraisons = DeserialiseurDemandeLivraisonsXML.charger(xmlDemandeLivraison, plan);

	Temps debutPlage = new Temps(8, 0, 0);
	Temps finPlage = new Temps(11, 0, 0);

	tournee = new Tournee(demandeLivraisons);
	tournee.calculerTSP();
	livraison = tournee.getLivraisonsTSP().get(0);
	controlleur = new Controlleur();

	cAjoutLivraison = new CommandeAjouterLivraison(tournee, livraison);
	cModifLivrasion = new CommandeModifierLivraison(tournee, livraison, debutPlage, finPlage);
	cSuppLivraison = new CommandeSupprimerLivraison(tournee, livraison);
    }

    @Test
    public void testHistorique() {
	Historique historique = new Historique();
	assertEquals(-1, historique.indiceCrt);
	assertEquals(0, historique.liste.size());
    }

    @Test
    public void testAjoute() {
	Historique historique = new Historique();
	assertEquals(0, historique.liste.size());
	assertEquals(-1, historique.indiceCrt);
	historique.ajoute(cAjoutLivraison, controlleur);
	assertEquals(0, historique.indiceCrt);
	assertEquals(1, historique.liste.size());
	historique.ajoute(cModifLivrasion, controlleur);
	assertEquals(1, historique.indiceCrt);
	assertEquals(2, historique.liste.size());
	historique.ajoute(cSuppLivraison, controlleur);
	assertEquals(2, historique.indiceCrt);
	assertEquals(3, historique.liste.size());
    }

    @Test
    public void testUndo() {
	Historique historique = new Historique();
	assertEquals(-1, historique.indiceCrt);
	historique.undo(controlleur);
	assertEquals(-1, historique.indiceCrt);
	historique.ajoute(cAjoutLivraison, controlleur);
	assertEquals(0, historique.indiceCrt);
	historique.undo(controlleur);
	assertEquals(-1, historique.indiceCrt);
	historique.ajoute(cModifLivrasion, controlleur);
	assertEquals(0, historique.indiceCrt);
	historique.ajoute(cSuppLivraison, controlleur);
	assertEquals(1, historique.indiceCrt);
	historique.undo(controlleur);
	assertEquals(0, historique.indiceCrt);
	historique.undo(controlleur);
	assertEquals(-1, historique.indiceCrt);
    }

    @Test
    public void testRedo() {
	Historique historique = new Historique();
	assertEquals(-1, historique.indiceCrt);
	historique.redo(controlleur);
	assertEquals(-1, historique.indiceCrt);

	historique.ajoute(cAjoutLivraison, controlleur);
	historique.redo(controlleur);
	assertEquals(0, historique.indiceCrt);
	historique.redo(controlleur);
	assertEquals(0, historique.indiceCrt);
	historique.undo(controlleur);
	historique.redo(controlleur);
	assertEquals(0, historique.indiceCrt);
    }

    @Test
    public void testReset() {
	Historique historique = new Historique();
	assertEquals(-1, historique.indiceCrt);
	assertEquals(0, historique.liste.size());

	historique.ajoute(cAjoutLivraison, controlleur);
	historique.ajoute(cModifLivrasion, controlleur);
	historique.ajoute(cSuppLivraison, controlleur);

	assertEquals(2, historique.indiceCrt);
	assertEquals(3, historique.liste.size());

	historique.reset();

	assertEquals(-1, historique.indiceCrt);
	assertEquals(0, historique.liste.size());
    }

}
