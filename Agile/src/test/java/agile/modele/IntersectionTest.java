package agile.modele;

import static org.junit.Assert.*;

import org.junit.Test;

public class IntersectionTest {

    @Test
    public void testConstructeurGet() {
	Intersection intersection = new Intersection(1, 2, 3);
	assertEquals(1, intersection.getId());
	assertEquals(2, intersection.getX());
	assertEquals(3, intersection.getY());
    }

}
