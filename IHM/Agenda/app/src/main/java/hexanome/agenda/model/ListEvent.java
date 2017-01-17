package hexanome.agenda.model;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * List of events for the whole application
 */
public class ListEvent {

    /**
     * Direct access of the list of events
     */
    public static List<Event> events = new ArrayList<>();

    /* Instantiation of content */
    static {
        events.add(new Event(2, new DateTime().withTime(8,0,0,0), new DateTime().withTime(12,0,0,0), "IHM"));
        events.add(new Event(3, new DateTime().withTime(14,0,0,0), new DateTime().withTime(16,0,0,0), "LV1"));
        events.add(new Event(4, new DateTime().withTime(16,0,0,0), new DateTime().withTime(18,0,0,0), "PLD Mars"));
    }

    /**
     * Private constructor: no instantiation possible
     */
    private ListEvent() {}
}