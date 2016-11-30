package agile.pathfinding;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class IteratorCroissant implements Iterator<Integer> {

    private Integer[] candidats;
    private int nbCandidats;

    /**
     * Cree un iterateur pour iterer sur l'ensemble des sommets de nonVus
     *
     * @param nonVus
     * @param sommetCrt
     */
    public IteratorCroissant(Collection<Integer> nonVus, int sommetCrt, float[][] cout) {

        this.candidats = new Integer[nonVus.size()];
        nbCandidats = nonVus.size();

        nonVus.toArray(this.candidats);

        Arrays.sort(this.candidats, (a, b) -> Float.compare(cout[sommetCrt][b],cout[sommetCrt][a]));
    }

    @Override
    public boolean hasNext() {

        return nbCandidats > 0;
    }

    @Override
    public Integer next() {

        return candidats[--nbCandidats];
    }

    @Override
    public void remove() {
    }

}
