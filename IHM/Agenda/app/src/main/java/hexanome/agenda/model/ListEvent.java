package hexanome.agenda.model;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import hexanome.agenda.util.MaterialColors;

/**
 * List of events for the whole application
 */
public final class ListEvent {

    /**
     * Direct access of the list of events
     */
    public static List<Event> events = new ArrayList<>();

    /* Instantiation of content */
    static {
        events.add(new Event(MaterialColors.blue, new DateTime(2017, 1, 16, 8, 0), new DateTime(2017, 1, 16, 12, 0), "OGP", "501:210", "B. Ergin\nA. Legait"));
        events.add(new Event(MaterialColors.blue, new DateTime(2017, 1, 16, 14, 0), new DateTime(2017, 1, 16, 16, 0), "LV2"));
        events.add(new Event(MaterialColors.blue, new DateTime(2017, 1, 17, 8, 0), new DateTime(2017, 1, 17, 12, 0), "IHM", "501:213", "L. Laporte\nJ. Rouzaud-Cornabas"));
        events.add(new Event(MaterialColors.blue, new DateTime(2017, 1, 18, 8, 0), new DateTime(2017, 1, 18, 12, 0), "OGP", "501:321", "B. Ergin\nA. Legait"));
        events.add(new Event(MaterialColors.blue, new DateTime(2017, 1, 18, 14, 0), new DateTime(2017, 1, 18, 16, 0), "Anglais"));
        events.add(new Event(MaterialColors.blue, new DateTime(2017, 1, 19, 9, 0), new DateTime(2017, 1, 19, 12, 0), "Matinée thématique AEDI"));
        events.add(new Event(MaterialColors.orange, new DateTime(2017, 1, 19, 13, 30), new DateTime(2017, 1, 19, 16, 0), "TOEIC"));
        events.add(new Event(MaterialColors.blue, new DateTime(2017, 1, 20, 8, 0), new DateTime(2017, 1, 20, 12, 0), "IHM", "501:213", "L. Laporte\nJ. Rouzaud-Cornabas"));
    }

    /**
     * Private constructor: no instantiation possible
     */
    private ListEvent() {
    }
}
