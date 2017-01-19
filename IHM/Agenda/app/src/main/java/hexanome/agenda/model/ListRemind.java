package hexanome.agenda.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hugue on 19/01/2017.
 */

public final class ListRemind {

    public static List<String> reminds = new ArrayList<String>();

    static {
        reminds.add("5 minutes before");
        reminds.add("10 minutes before");
        reminds.add("15 minutes before");
        reminds.add("30 minutes before");
        reminds.add("1 hour before");
        reminds.add("2 hours before");
        reminds.add("1 day before");
    }

    /**
     * Private constructor: no instantiation possible
     */
    private ListRemind() {
    }
}
