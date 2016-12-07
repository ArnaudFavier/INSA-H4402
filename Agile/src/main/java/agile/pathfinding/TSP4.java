//package agile.pathfinding;

import java.util.*;

/*
* Calcul d'un arbre couvrant minimal pour le bound
*
* Add to pom.xml
*
* +		<!-- JGraphT -->
+		<dependency>
+			<groupId>org.jgrapht</groupId>
+			<artifactId>jgrapht-core</artifactId>
+			<version>1.0.0</version>
+		</dependency>
*
* */
/*
import org.jgrapht.alg.util.*;

public class TSP4 extends TemplateTSP {

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

        Set<Arete> ar = kruskal (nonVus, cout, duree);

        for(Arete a : ar)
        {
            res+= a.cout;
        }

        return res;
    }

    class Arete {
        public Float cout;
        public Integer debut;
        public Integer fin;

        Arete(Float cout, Integer debut, Integer fin){
            this.cout = cout;
            this.debut = debut;
            this.fin = fin;
        }
    }

    private Set<Arete> kruskal(List<Integer> nonVus, float[][] cout, int[] duree) {

        Set<Arete> res = new HashSet<>();

        List<Arete> aretesCout = new ArrayList<Arete>(nonVus.size()*nonVus.size());

        for(Integer A : nonVus)
            for(Integer B : nonVus)
                if(A!=B) {
                    aretesCout.add(new Arete(cout[A][B] + duree[B], A, B));
                }

        aretesCout.sort(Comparator.comparing(a -> a.cout));

        UnionFind<Integer> us = new UnionFind(new HashSet(nonVus));

        for(Arete a : aretesCout)
        {
            if(us.find(a.debut).intValue() != us.find(a.fin).intValue())
            {
                res.add(a);
                us.union(a.debut,a.fin);
            }
        }

        return res;
    }
}
*/