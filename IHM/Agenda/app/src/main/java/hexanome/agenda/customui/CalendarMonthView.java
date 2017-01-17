package hexanome.agenda.customui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.util.SortedList;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hexanome.agenda.activities.AddEventActivity;
import hexanome.agenda.activities.DayActivity;
import hexanome.agenda.activities.MonthActivity;
import hexanome.agenda.model.Event;
import hexanome.agenda.util.MaterialColors;
import hexanome.agenda.util.Measures;

public class CalendarMonthView extends View {

    private List<Event> events = new ArrayList<>();
    private HashMap<MonthDate, DatePosition> datePositions;
    private DateTime mCurrentMonth;
    private DateTime prevMonth;
    private DateTime nextMonth;
    private TextPaint mTextDayGreyNumberPaint;
    private TextPaint mTextEventName;
    private TextPaint mTextDayName;
    private Paint mSeparatorPaint;
    private TextPaint mTextDayNumberPaint;
    private float mPixelDayNumberSize;
    private Paint mRectEventPaint;
    private Resources mResources;
    private int mParentWidth = 0;
    private int mParentHeight = 0;

    public CalendarMonthView(Context context) {
        super(context);
        init();
    }

    public CalendarMonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setMonth(DateTime dateTime) {
        mCurrentMonth = dateTime;
        prevMonth = new DateTime(mCurrentMonth).minusMonths(1);
        nextMonth = new DateTime(mCurrentMonth).plusMonths(1);
    }

    private void init() {
        mResources = getResources();
        mCurrentMonth = new DateTime();
        prevMonth = new DateTime(mCurrentMonth).minusMonths(1);
        nextMonth = new DateTime(mCurrentMonth).plusMonths(1);

        mPixelDayNumberSize = Measures.dpToPixels(mResources, 12);

        mTextDayNumberPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextDayNumberPaint.setColor(Color.BLACK);
        mTextDayNumberPaint.setTextSize(mPixelDayNumberSize);

        mTextDayGreyNumberPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextDayGreyNumberPaint.setColor(Color.GRAY);
        mTextDayGreyNumberPaint.setTextSize(mPixelDayNumberSize);

        mTextEventName = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextEventName.setColor(MaterialColors.grey_300);
        mTextEventName.setTextSize(Measures.dpToPixels(mResources, 10));
        mTextEventName.setTypeface(Typeface.DEFAULT_BOLD);

        mTextDayName = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextDayName.setColor(MaterialColors.black);
        mTextDayName.setTextSize(Measures.dpToPixels(mResources, 14));

        mRectEventPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mSeparatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSeparatorPaint.setColor(Color.GRAY);
        mSeparatorPaint.setStrokeWidth(Measures.dpToPixels(mResources, 1));

        // REMOVE THIS
        addEvent(new Event(MaterialColors.blue, new DateTime(), new DateTime(), "IHM"));
        addEvent(new Event(MaterialColors.green, new DateTime(), new DateTime(), "OGP"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mParentWidth = MeasureSpec.getSize(widthMeasureSpec);
        mParentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(mParentWidth, mParentHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        datePositions = new HashMap<>();

        if (mParentHeight == 0 || mParentWidth == 0) {
            return;
        }

        final float dayNameHeight = Measures.dpToPixels(mResources, 20);
        float widthUnit = mParentWidth / 7f;
        float heightUnit = (mParentHeight - dayNameHeight) / 6f;

        float eventRectHeight = Measures.dpToPixels(mResources, 12);
        float rounding = Measures.dpToPixels(mResources, 3);
        float textPadding = Measures.dpToPixels(mResources, 5);

        // Draw day names
        String[] dayNames = new String[]{"L", "M", "M", "J", "V", "S", "D"};
        for (int day = 0; day < 7; day++) {
            canvas.drawText(dayNames[day], day * widthUnit + textPadding, mPixelDayNumberSize + textPadding, mTextDayName);
        }

        // Last month
        int firstDayOfMonth = getFirstWeekDayOfMonth() - 1;
        if(firstDayOfMonth == 0){
            firstDayOfMonth = 7;
        }
        int dayNumber;
        for (int day = 0; day < firstDayOfMonth; day++) {
            dayNumber = getLastDayOfPrevMonth() - (firstDayOfMonth - 1 - day);
            canvas.drawText(Integer.toString(dayNumber), day * widthUnit + textPadding, mPixelDayNumberSize + textPadding + dayNameHeight, mTextDayGreyNumberPaint);

            MonthDate monthDate = new MonthDate(prevMonth.getMonthOfYear(), dayNumber);
            datePositions.put(monthDate, new DatePosition(day, 0));
        }

        final int lastDayOfMonth = getLastDayOfMonth();
        int x = firstDayOfMonth;
        dayNumber = 1;
        int y = 0;
        currentMonthNumberLoop:
        for (; y < 6; y++) {
            float drawHeight = y * heightUnit + mPixelDayNumberSize + dayNameHeight;
            for (; x < 7; x++) {

                canvas.drawText(Integer.toString(dayNumber), x * widthUnit + textPadding, drawHeight + textPadding, mTextDayNumberPaint);
                if (dayNumber == lastDayOfMonth) {
                    break currentMonthNumberLoop;
                }
                datePositions.put(new MonthDate(mCurrentMonth.getMonthOfYear(), dayNumber), new DatePosition(x, y));
                dayNumber++;
            }
            // Draw horizontal separator
            canvas.drawLine(0, dayNameHeight + (y + 1) * heightUnit, mParentWidth, dayNameHeight + (y + 1) * heightUnit, mSeparatorPaint);
            x = 0;
        }

        dayNumber = 1;
        x = getLastWeekDayOfMonth();
        for (; y < 6; y++) {
            float drawHeight = y * heightUnit + mPixelDayNumberSize + dayNameHeight;
            for (; x < 7; x++) {
                canvas.drawText(Integer.toString(dayNumber), x * widthUnit + textPadding, drawHeight + textPadding, mTextDayGreyNumberPaint);
                datePositions.put(new MonthDate(nextMonth.getMonthOfYear(), dayNumber), new DatePosition(x, 5));
                dayNumber++;
            }
            // Draw horizontal separator
            canvas.drawLine(0, dayNameHeight + y * heightUnit, mParentWidth, dayNameHeight + y * heightUnit, mSeparatorPaint);
            x = 0;
        }

        // Draw events
        float textNumberOffset = 2 * textPadding + mPixelDayNumberSize;
        for (Event event : events) {
            DatePosition pos = datePositions.get(new MonthDate(event.startTime.getMonthOfYear(), event.startTime.dayOfMonth().get()));

            if (pos != null) {
                mRectEventPaint.setColor(event.color);
                int eventHeight = pos.availableEventPos.get(0);
                pos.availableEventPos.remove(0);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    canvas.drawRoundRect(pos.x * widthUnit, dayNameHeight + pos.y * heightUnit + textNumberOffset + eventRectHeight * eventHeight, (pos.x + 1) * widthUnit, dayNameHeight + pos.y * heightUnit + textNumberOffset + eventRectHeight * (eventHeight + 1), rounding, rounding, mRectEventPaint);
                }
                else {
                    canvas.drawRect(pos.x * widthUnit, dayNameHeight + pos.y * heightUnit + textNumberOffset + eventRectHeight * eventHeight, (pos.x + 1) * widthUnit, dayNameHeight + pos.y * heightUnit + textNumberOffset + eventRectHeight * (eventHeight + 1), mRectEventPaint);
                }
                canvas.drawText(event.title, pos.x * widthUnit + Measures.dpToPixels(mResources, 3), dayNameHeight + pos.y * heightUnit + textNumberOffset + eventRectHeight * (eventHeight + 1) - Measures.dpToPixels(mResources, 2.5f), mTextEventName);
            }
        }
    }

    private int getFirstWeekDayOfMonth() {
        return mCurrentMonth.withDayOfMonth(1).dayOfWeek().get();
    }

    private int getLastDayOfPrevMonth() {
        return prevMonth.dayOfMonth().withMaximumValue().getDayOfMonth();
    }

    private int getLastWeekDayOfMonth() {
        return mCurrentMonth.dayOfMonth().withMaximumValue().dayOfWeek().get();
    }

    private int getLastDayOfMonth() {
        return mCurrentMonth.dayOfMonth().withMaximumValue().getDayOfMonth();
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    private class MonthDate {
        int month;
        int day;

        MonthDate(int month, int day) {
            this.month = month;
            this.day = day;
        }

        @Override
        public int hashCode() {
            int result = month;
            result = 31 * result + day;
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MonthDate monthDate = (MonthDate) o;

            if (month != monthDate.month) return false;
            return day == monthDate.day;
        }
    }

    private class DatePosition {
        int x;
        int y;
        List<Integer> availableEventPos;

        DatePosition(int x, int y) {
            this.x = x;
            this.y = y;
            availableEventPos = new ArrayList<>(4);
            availableEventPos.add(0);
            availableEventPos.add(1);
            availableEventPos.add(2);
            availableEventPos.add(3);
        }
    }

    private static final int MAX_CLICK_DURATION = 200;
    private long startClickTime;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                startClickTime = System.currentTimeMillis();
                break;
            }
            case MotionEvent.ACTION_UP: {
                long clickDuration = System.currentTimeMillis() - startClickTime;
                if (clickDuration < MAX_CLICK_DURATION) {
                    triggerClick(x, y);
                }
            }
        }

        return true;
    }

    private void triggerClick(float x, float y) {
        final float dayNameHeight = Measures.dpToPixels(mResources, 20);
        float widthUnit = mParentWidth / 7f;
        float heightUnit = (mParentHeight - dayNameHeight) / 6f;

        int dayX = (int)(x / widthUnit);
        int dayY = (int)((y - dayNameHeight) / heightUnit);

        for (Map.Entry<MonthDate, DatePosition> entry : datePositions.entrySet()) {
            DatePosition datePos = entry.getValue();
            if(datePos.x == dayX && datePos.y == dayY){
                MonthDate monthDate = entry.getKey();
                Toast.makeText(getContext(), monthDate.day+"/"+monthDate.month, Toast.LENGTH_LONG).show();
            }
        }
    }
}
