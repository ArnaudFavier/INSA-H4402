package agile.pathfinding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TSP3 extends TemplateTSP {

    @Override
    protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, float[][] cout, int[] duree) {

	    return new IteratorCroissant(nonVus, sommetCrt, cout, duree);
    }

    @Override
    protected float bound(Integer sommetCourant, ArrayList<Integer> nonVus, float[][] cout, int[] duree) {

        float res = 0f;
        float min;

        min = cout[sommetCourant][nonVus.get(0)] + duree[nonVus.get(0)];
        for(Integer A : nonVus)
            if(cout[sommetCourant][A] + duree[A] < min)
                min = cout[sommetCourant][A] + duree[A];

        res += min;

        min = cout[nonVus.get(0)][0];
        for(Integer A : nonVus)
            if(cout[A][0] < min)
                min = cout[A][0];

        res += min;

        List<Float> aretesCout = new ArrayList<Float>(nonVus.size()*nonVus.size());

        for(Integer A : nonVus)
            for(Integer B : nonVus)
                if(A!=B) {
                    aretesCout.add(cout[A][B] + duree[B]);
                }

        aretesCout.sort(Float::compare);

        for(int i = 0; i < nonVus.size() - 1; i++)
            res += aretesCout.get(i);

        return res;
    }
}
