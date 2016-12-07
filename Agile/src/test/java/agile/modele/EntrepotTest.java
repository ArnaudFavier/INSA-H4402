package agile.modele;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class EntrepotTest {
    private Temps heureDepart;
    private Temps heureRetour;
    private Intersection intersection;

    @Before
    public void initialization() {
	heureDepart = new Temps(369);
	heureRetour = new Temps(741);
	intersection = new Intersection(8, 73, -85);
    }

    @Test
    public void testGetSet() {
	Entrepot entrepot = new Entrepot(heureDepart, intersection);
	assertEquals(heureDepart, entrepot.getHeureDepart());
	assertEquals(intersection, entrepot.getIntersection());

	entrepot.setHeureRetour(heureRetour);
	assertEquals(heureRetour, entrepot.getHeureRetour());
    }
}