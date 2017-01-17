package hexanome.agenda.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thehayro.view.InfinitePagerAdapter;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hexanome.agenda.R;
import hexanome.agenda.customui.CalendarMonthView;
import hexanome.agenda.model.ListEvent;

public class MonthActivity extends ActionBarActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager mPager;
    private MyInfinitePagerAdapter mPagerAdapter;

    private static final int ADD_EVENT_INTENT_CODE = 120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MonthActivity.this, AddEventActivity.class), ADD_EVENT_INTENT_CODE);
            }
        });

        String month = new DateTime().monthOfYear().getAsText();
        month = month.substring(0, 1).toUpperCase() + month.substring(1);
        setTitle(month + " " + new DateTime().year().get());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mPager = (ViewPager)findViewById(R.id.month_pager);
       // mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPagerAdapter = new MyInfinitePagerAdapter(0);
        mPager.setAdapter(mPagerAdapter);

        mPager.setCurrentItem(1);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int pageSelected ) {
                DateTime currentMonth = new DateTime();
                DateTime chosenMonth = currentMonth.plusMonths(mPagerAdapter.getIndicator());
                String month = chosenMonth.monthOfYear().getAsText();
                month = month.substring(0, 1).toUpperCase() + month.substring(1);
                setTitle(month+" "+chosenMonth.year().get());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        TextView dateToday = (TextView) findViewById(R.id.expanded_menu_date);
        dateToday.setText(new DateTime().toString("EEEE d MMMM"));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_day) {
            startActivity(new Intent(this, DayActivity.class));
            finish();
        }
        else if (id == R.id.menu_week) {
            startActivity(new Intent(this, WeekActivity.class));
            finish();
        }
        else if (id == R.id.menu_month) {

        }
        else if (id == R.id.nav_share) {
            startActivity(new Intent(this, OptionActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ADD_EVENT_INTENT_CODE) {
            List<ViewGroup> pages = mPagerAdapter.getViewGroups();
            for(ViewGroup viewGroup : pages){
                CalendarMonthView calendarMonthView = (CalendarMonthView)viewGroup.findViewById(R.id.calendar_month_view);
                calendarMonthView.setEvents(ListEvent.events);
            }
        }
    }

    private class MyInfinitePagerAdapter extends InfinitePagerAdapter<Integer> {

        public MyInfinitePagerAdapter(Integer initValue) {
            super(initValue);
        }

        public Integer getIndicator(){
            return getCurrentIndicator();
        }

        public Integer getNextIndicator() {
            return getCurrentIndicator() + 1;
        }

        public Integer getPreviousIndicator() {
            return getCurrentIndicator() - 1;
        }

        @Override
        public ViewGroup instantiateItem(Integer indicator) {
            final FrameLayout layout = (FrameLayout) ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.fragment_month, null);
            layout.setTag(indicator);
            DateTime selectedMonth = new DateTime().plusMonths(indicator);
            CalendarMonthView calendarMonthView = (CalendarMonthView)layout.findViewById(R.id.calendar_month_view);
            calendarMonthView.setMonth(selectedMonth);
            calendarMonthView.setEvents(ListEvent.events);

            return layout;
        }

        @Override
        public String getStringRepresentation(final Integer currentIndicator) {
            return String.valueOf(currentIndicator);
        }

        @Override
        public Integer convertToIndicator(final String representation) {
            return Integer.valueOf(representation);
        }
    }
        }
