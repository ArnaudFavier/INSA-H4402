package hexanome.agenda.model;

import com.alamkanak.weekview.WeekViewEvent;

import org.joda.time.DateTime;

public class Event {
    private static long NEXT_ID_TO_GIVE = 0;

    private long id;
    public int color;
    public DateTime startTime;
    public DateTime endTime;
    public String title;
    public String lieu;
    public String description;
    public int remind;
    public EventGroup group;

    public enum EventGroup {
        IF3,
        IF4,
        IF5,
        GI3,
        GI4,
        GI5,
        Rotonde,
        Ragda,
        BDE,
        KFete,
        URL,
        UNASSIGNED,
    }

    public Event(int color, DateTime startTime, DateTime endTime, String title) {
        this.color = color;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.id = NEXT_ID_TO_GIVE++;
        this.group = EventGroup.UNASSIGNED;
    }

    public Event(int color, DateTime startTime, DateTime endTime, String title, String lieu) {
        this(color, startTime, endTime, title);
        this.lieu = lieu;
    }

    public Event(int color, DateTime startTime, DateTime endTime, String title, String lieu, String description) {
        this(color, startTime, endTime, title, lieu);
        this.description = description;
    }

    public Event(int color, DateTime startTime, DateTime endTime, String title, String lieu, String description, int remind) {
        this(color, startTime, endTime, title, lieu, description);
        this.remind = remind;
    }

    public Event setGroup(EventGroup group){
        this.group = group;
        return this;
    }

    public WeekViewEvent parseToWeekEvent() {
        WeekViewEvent weekViewEvent = new WeekViewEvent(
                this.id,
                title,
                startTime.getYear(),
                startTime.getMonthOfYear(),
                startTime.getDayOfMonth(),
                startTime.getHourOfDay(),
                startTime.getMinuteOfHour(),
                endTime.getYear(),
                endTime.getMonthOfYear(),
                endTime.getDayOfMonth(),
                endTime.getHourOfDay(),
                endTime.getMinuteOfHour()
        );
        weekViewEvent.setColor(color);
        return weekViewEvent;
    }

    public long getId() {
        return id;
    }
}
