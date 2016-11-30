package agile.pathfinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.PriorityQueue;

public class TSP3 extends TemplateTSP {

    @Override
    protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, float[][] cout, int[] duree) {

	    return new IteratorCroissant(nonVus, sommetCrt, cout);
    }

    @Override
    protected float bound(Integer sommetCourant, ArrayList<Integer> nonVus, float[][] cout, int[] duree) {

        Float res = 0f;

        Float min = Float.MAX_VALUE;

        for ( Integer A : nonVus )
        {
            if(cout[sommetCourant][A] < min)
                min = cout[sommetCourant][A];
        }

        res += min;

        min = Float.MAX_VALUE;

        for ( Integer A : nonVus )
        {
            if(cout[A][sommetCourant] < min)
                min = cout[sommetCourant][A];
        }

        res += min;

        PriorityQueue<Float> aretesCoutMinimum = new PriorityQueue<>(nonVus.size()*nonVus.size());

        for ( Integer A : nonVus )
        {
            for ( Integer B : nonVus )
            {
                if ( A.intValue() != B.intValue() )
                    aretesCoutMinimum.add(cout[A][B]);
            }
        }

        for ( int i = 0; i < nonVus.size() - 1; i++ )
        {
            res += aretesCoutMinimum.remove();
        }

        return res;
    }
}
