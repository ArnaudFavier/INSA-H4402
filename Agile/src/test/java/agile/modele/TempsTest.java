package agile.modele;

import static org.junit.Assert.*;

import org.junit.Test;

public class TempsTest {

    @Test
    public void testConstructeurGet() {
	Temps temps = new Temps(15, 56, 32);
	assertEquals(temps.getHeure(), 15);
	assertEquals(temps.getMinute(), 56);
	assertEquals(temps.getSeconde(), 32);
    }

    @Test
    public void testCompareTo() {
	Temps t1 = new Temps(0, 0, 0);
	Temps t2 = new Temps(0, 0, 0);
	Temps t3 = new Temps(1, 0, 0);
	Temps t4 = new Temps(0, 1, 0);
	Temps t5 = new Temps(0, 0, 1);

	// Test �galit� + reflexivit� de l'�galit�
	assertEquals(t1.compareTo(t2), 0);
	assertEquals(t2.compareTo(t1), 0);

	// Test heure sup�rieure
	assertEquals(t3.compareTo(t1), 1);

	// Test heure inf�rieure
	assertEquals(t1.compareTo(t3), -1);

	// Test minute sup�rieure
	assertEquals(t4.compareTo(t1), 1);

	// Test minute inf�rieure
	assertEquals(t1.compareTo(t4), -1);

	// Test seconde sup�rieure
	assertEquals(t5.compareTo(t1), 1);

	// Test seconde inf�rieure
	assertEquals(t1.compareTo(t5), -1);
    }

}
