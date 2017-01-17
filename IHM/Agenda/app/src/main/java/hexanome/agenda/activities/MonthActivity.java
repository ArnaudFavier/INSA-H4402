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

import com.thehayro.view.InfinitePagerAdapter;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;

import hexanome.agenda.R;
import hexanome.agenda.customui.CalendarMonthView;

public class MonthActivity extends ActionBarActivity implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<Integer> positions = new ArrayList<Integer>();
    private ViewPager mPager;
    private MyInfinitePagerAdapter mPagerAdapter;

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
                startActivity(new Intent(MonthActivity.this, AddEventActivity.class));
            }
        });

        setTitle(new DateTime().monthOfYear().getAsText() + " " + new DateTime().year().get());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        positions.add(-1);
        positions.add(0);
        positions.add(1);

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
                setTitle(chosenMonth.monthOfYear().getAsText()+" "+chosenMonth.year().get());
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_day) {
            startActivity(new Intent(this, DayActivity.class));
        }
        else if (id == R.id.menu_week) {

        }
        else if (id == R.id.menu_month) {

        }
        else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
            ((CalendarMonthView)layout.findViewById(R.id.calendar_month_view)).setMonth(selectedMonth);
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

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private FragmentTransaction mCurTransaction = null;

        private long[] mItemIds = new long[] {};
        private ArrayList<Fragment.SavedState> mSavedState = new ArrayList<Fragment.SavedState>();
        private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
        private Fragment mCurrentPrimaryItem = null;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public int getCount() {
            return 3;
        }

        /**
         * Return the Fragment associated with a specified position.
         */
        public Fragment getItem(int position){
            DateTime currentMonth = new DateTime();
            int relativePos = positions.get(position);
            return MonthFragment.newInstance(currentMonth.plusMonths(relativePos), relativePos);
        }

        /**
         * Return a unique identifier for the item at the given position.
         */
        public int getItemId(int position) {
            return positions.get(position);
        }
    }
}
