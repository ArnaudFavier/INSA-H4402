package agile.modele;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TronconTest {

    Intersection intersection1;
    Intersection intersection2;

    @Before
    public void setUp() {
	intersection1 = new Intersection(1, 2, 3);
	intersection2 = new Intersection(2, 4, 5);
    }

    @Test
    public void testConstructeurGet() {
	Troncon troncon = new Troncon(1, 2, "rueTest", intersection1, intersection2);
	assertEquals(troncon.getLongueur(), 1);
	assertEquals(troncon.getVitesse(), 2);
	assertEquals(troncon.getNomRue(), "rueTest");
	assertEquals(troncon.getOrigine(), intersection1);
	assertEquals(troncon.getDestination(), intersection2);
	assertNotNull(troncon.getIntersections());
	assertEquals(troncon.getIntersections()[0], intersection1);
	assertEquals(troncon.getIntersections()[1], intersection2);
    }

}
