package agile.modele;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

public class CheminTest {

    @Test
    public void testConstructeurGet() {
	Intersection it0 = new Intersection(0, 0, 0);
	Intersection it1 = new Intersection(1, 10, 0);
	Intersection it2 = new Intersection(2, 10, 10);
	Intersection it3 = new Intersection(3, 0, 10);
	ArrayList<Intersection> listeIntersections = new ArrayList<Intersection>(4);
	listeIntersections.add(it0);
	listeIntersections.add(it1);
	listeIntersections.add(it2);
	listeIntersections.add(it3);

	Troncon tr0 = new Troncon(2, 40, "rue0", it0, it1);
	Troncon tr1 = new Troncon(1, 42, "rue1", it1, it2);
	Troncon tr2 = new Troncon(2, 45, "rue2", it2, it3);
	ArrayList<Troncon> listeTroncons = new ArrayList<Troncon>(3);
	listeTroncons.add(tr0);
	listeTroncons.add(tr1);
	listeTroncons.add(tr2);

	Chemin chemin = new Chemin(listeTroncons, listeIntersections, 5);

	Assert.assertEquals(chemin.getCout(), 5f, 0f);

	assertEquals(chemin.getIntersections().size(), 4);
	assertEquals(chemin.getIntersections().get(0), it0);
	assertEquals(chemin.getIntersections().get(1), it1);
	assertEquals(chemin.getIntersections().get(2), it2);
	assertEquals(chemin.getIntersections().get(3), it3);

	assertEquals(chemin.getTroncons().size(), 3);
	assertEquals(chemin.getTroncons().get(0), tr0);
	assertEquals(chemin.getTroncons().get(1), tr1);
	assertEquals(chemin.getTroncons().get(2), tr2);
    }

}
