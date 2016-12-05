package agile.modele;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class LivraisonTest {
    private int duree = 500;
    private Intersection intersection;
    private Temps tempsDebut;
    private Temps tempsFin;
    private int heureArrivee;
    private float tempsAttente;

    @Before
    public void initialization() {
	intersection = new Intersection(3, -45, 58);
	tempsDebut = new Temps(789);
	tempsFin = new Temps(357);
	heureArrivee = 654;
	tempsAttente = 951.0f;
    }

    @Test
    public void testGetSet() {
	Livraison livraison = new Livraison(duree, intersection);
	assertEquals(duree, livraison.getDuree());
	assertEquals(intersection, livraison.getIntersection());
	assertEquals(false, livraison.ContrainteDeTemps());

	livraison.setDebutPlage(tempsDebut);
	livraison.setFinPlage(tempsFin);
	assertEquals(tempsDebut, livraison.getDebutPlage());
	assertEquals(tempsFin, livraison.getFinPlage());

	livraison.setHeureArrivee(heureArrivee);
	assertEquals(new Temps(heureArrivee), livraison.getHeureArrivee());

	livraison.setTempsAttente(tempsAttente);
	assertEquals(tempsAttente, livraison.getTempsAttente(), 0.01);

	livraison = new Livraison(159, intersection, tempsDebut, tempsFin);
	assertEquals(159, livraison.getDuree());
	assertEquals(intersection, livraison.getIntersection());
	assertEquals(tempsDebut, livraison.getDebutPlage());
	assertEquals(tempsFin, livraison.getFinPlage());
	assertEquals(true, livraison.ContrainteDeTemps());
    }

    @Test
    public void testMethods() {
	Livraison livraison1 = new Livraison(duree, intersection);
	Livraison livraison2 = livraison1.clone();
	assertEquals(livraison1.getDuree(), livraison2.getDuree());
	assertEquals(livraison1.getIntersection(), livraison2.getIntersection());
	assertEquals(livraison1.getDebutPlage(), livraison2.getDebutPlage());
	assertEquals(livraison1.getFinPlage(), livraison2.getFinPlage());
	assertEquals(livraison1.ContrainteDeTemps(), livraison2.ContrainteDeTemps());
	assertEquals(livraison1.getHeureArrivee(), livraison2.getHeureArrivee());
	assertEquals(livraison1.getTempsAttente(), livraison2.getTempsAttente(), 0.1);
    }
}