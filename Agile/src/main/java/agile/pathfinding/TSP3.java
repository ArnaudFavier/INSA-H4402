package agile.pathfinding;

import java.util.ArrayList;
import java.util.Iterator;

public class TSP3 extends TemplateTSP {

    @Override
    protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, float[][] cout, int[] duree) {

	    return new IteratorCroissant(nonVus, sommetCrt, cout);
    }

    @Override
    protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus, float[][] cout, int[] duree) {

        return 0;
    }
}
