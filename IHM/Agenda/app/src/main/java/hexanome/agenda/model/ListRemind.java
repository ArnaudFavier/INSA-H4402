package hexanome.agenda.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hugue on 19/01/2017.
 */

public final class ListRemind {

    public static List<String> reminds = new ArrayList<String>();

    static {
        reminds.add("5 minutes avant");
        reminds.add("10 minutes avant");
        reminds.add("15 minutes avant");
        reminds.add("30 minutes avant");
        reminds.add("1 heure avant");
        reminds.add("1 jour avant");
    }

    /**
     * Private constructor: no instantiation possible
     */
    private ListRemind() {
    }
}
