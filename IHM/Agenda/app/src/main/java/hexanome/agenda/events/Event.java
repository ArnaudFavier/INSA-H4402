package hexanome.agenda.events;

import org.joda.time.DateTime;

public class Event {
    public int color;
    public DateTime startTime;
    public DateTime endTime;
    public String title;

    public Event(int color, DateTime startTime, DateTime endTime, String title) {
        this.color = color;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
    }
}
