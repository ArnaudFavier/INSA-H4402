package agile.modele;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class LivraisonTest {

    private Intersection intersection;

    private Temps tempsDebut;
    private Temps tempsFin;

    @Before
    public void intialisation() {
	intersection = new Intersection(3, -45, 58);
	tempsDebut = new Temps(789);
	tempsFin = new Temps(357);
    }

    @Test
    public void testGet() {
	Livraison livraison = new Livraison(500, intersection);

	assertEquals(livraison.getDuree(), 500);
	assertEquals(livraison.getIntersection(), intersection);

	livraison.setDebutPlage(tempsDebut);
	livraison.setFinPlage(tempsFin);

	assertEquals(livraison.getDebutPlage(), tempsDebut);
	assertEquals(livraison.getFinPlage(), tempsFin);
    }

}
