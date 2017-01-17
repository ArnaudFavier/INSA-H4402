package hexanome.agenda.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.DateTime;

import hexanome.agenda.R;
import hexanome.agenda.customui.CalendarMonthView;

public class MonthFragment extends Fragment {

    DateTime currentMonth;
    int fragmentPosition;

    public MonthFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MonthFragment newInstance(DateTime currentMonth, int fragmentPosition) {
        MonthFragment fragment = new MonthFragment();
        fragment.currentMonth = currentMonth;
        fragment.fragmentPosition = fragmentPosition;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_month, container, false);
        CalendarMonthView monthView = (CalendarMonthView)fragmentView.findViewById(R.id.calendar_month_view);
        monthView.setMonth(currentMonth);
        return fragmentView;
    }
}
