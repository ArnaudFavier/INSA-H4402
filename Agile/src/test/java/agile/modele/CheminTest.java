package agile.modele;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class CheminTest {

    private Intersection it0;
    private Intersection it1;
    private Intersection it2;
    private Intersection it3;
    private ArrayList<Intersection> listeIntersections;
    private Troncon tr0;
    private Troncon tr1;
    private Troncon tr2;
    private ArrayList<Troncon> listeTroncons;
    private Chemin chemin;
    private Chemin cheminVide;

    @Before
    public void initialisation() {

	// Initialisation des intersections
	this.it0 = new Intersection(0, 0, 0);
	this.it1 = new Intersection(1, 10, 0);
	this.it2 = new Intersection(2, 10, 10);
	this.it3 = new Intersection(3, 0, 10);
	this.listeIntersections = new ArrayList<Intersection>(4);
	this.listeIntersections.add(it0);
	this.listeIntersections.add(it1);
	this.listeIntersections.add(it2);
	this.listeIntersections.add(it3);

	// Initialisation des tronçons
	this.tr0 = new Troncon(2, 40, "rue0", it0, it1);
	this.tr1 = new Troncon(1, 42, "rue1", it1, it2);
	this.tr2 = new Troncon(2, 45, "rue2", it2, it3);
	this.listeTroncons = new ArrayList<Troncon>(3);
	listeTroncons.add(tr0);
	listeTroncons.add(tr1);
	listeTroncons.add(tr2);

	// Initialisation des chemin
	this.chemin = new Chemin(listeTroncons, listeIntersections, 5);
	this.cheminVide = new Chemin(new ArrayList<Troncon>(), new ArrayList<Intersection>(), 0);
    }

    @Test
    public void testConstructeurGet() {

	assertEquals(cheminVide.getCout(), 0f, 0.01f);
	assertEquals(chemin.getCout(), 5f, 0.01f);

	assertEquals(cheminVide.getIntersections().size(), 0);
	assertEquals(chemin.getIntersections().size(), 4);
	assertEquals(chemin.getIntersections().get(0), it0);
	assertEquals(chemin.getIntersections().get(1), it1);
	assertEquals(chemin.getIntersections().get(2), it2);
	assertEquals(chemin.getIntersections().get(3), it3);

	assertEquals(cheminVide.getTroncons().size(), 0);
	assertEquals(chemin.getTroncons().size(), 3);
	assertEquals(chemin.getTroncons().get(0), tr0);
	assertEquals(chemin.getTroncons().get(1), tr1);
	assertEquals(chemin.getTroncons().get(2), tr2);
    }

    @Test
    public void testMethods() {
	Chemin cheminClone = this.chemin.clone();
	assertEquals(cheminClone.getCout(), chemin.getCout(), 0.01f);
	assertEquals(cheminClone.getIntersections(), chemin.getIntersections());
	assertEquals(cheminClone.getTroncons(), chemin.getTroncons());
    }

}
