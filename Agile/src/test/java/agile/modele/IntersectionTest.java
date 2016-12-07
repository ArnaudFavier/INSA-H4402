package agile.modele;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class IntersectionTest {

    @Test
    public void testConstructeurGet() {
	Intersection intersection = new Intersection(1, 2, 3);
	assertEquals(1, intersection.getId());
	assertEquals(2, intersection.getX());
	assertEquals(3, intersection.getY());
    }

    @Test
    public void testToString() {
	Intersection intersection = new Intersection(1, 2, 3);
	String result = "{id=1, x=2, y=3}";
	assertNotNull(intersection.toString());
	assertEquals(intersection.toString(), result);
    }

}
