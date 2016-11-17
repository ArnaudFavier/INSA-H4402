package agile.modele;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PlanTest {

    Intersection intersection1;
    Intersection intersection2;
    Troncon troncon;

    @Before
    public void setUp() {
	intersection1 = new Intersection(1, 2, 3);
	intersection2 = new Intersection(2, 4, 5);
	troncon = new Troncon(1, 2, "rueTest", intersection1, intersection2);
    }

    @Test
    public void testConstructeurGetAdd() {
	Plan plan = new Plan();
	assertNotNull(plan.getIntersections());
	assertNotNull(plan.getTroncons());

	assertTrue(plan.getIntersections().isEmpty());
	assertTrue(plan.getTroncons().isEmpty());
	
	plan.addIntersection(intersection1);
	plan.addIntersection(intersection2);
	plan.addTroncon(troncon);
	
	assertEquals(plan.getIntersections().size(), 2);
	assertEquals(plan.getTroncons().size(), 1);
    }

}
