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

    public Event(int color, DateTime startTime, DateTime endTime, String title) {
        this.color = color;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.id = NEXT_ID_TO_GIVE++;
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
}
