package hexanome.agenda.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.joda.time.DateTime;

import hexanome.agenda.R;
import hexanome.agenda.model.Event;

public class MainActivity extends ActionBarActivity implements NavigationView.OnNavigationItemSelectedListener {

    public MonthFragment mMonthFragment;
    public WeekFragment mWeekFragment;
    public DayFragment mDayFragment;
    private NavigationView navigationView;
    FragmentManager mFragmentManager;

    private static final int ADD_EVENT_INTENT_CODE = 120;
    private static final int OPTIONS_INTENT_CODE = 121;
    static final int CONSULT_EVENT_ACTIVITY = 122;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, AddEventActivity.class), ADD_EVENT_INTENT_CODE);
            }
        });

        updateTitle();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Insert the fragment by replacing any existing fragment
        mFragmentManager = getSupportFragmentManager();
        mMonthFragment = new MonthFragment();
        mMonthFragment.setFragmentManager(mFragmentManager);
        changeContentFragment(mMonthFragment);

        mWeekFragment = new WeekFragment();
        mDayFragment = new DayFragment();
    }

    public void setNavigationViewSelectedItem(int item) {
        navigationView.getMenu().getItem(item).setChecked(true);
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
        TextView dateToday = (TextView) findViewById(R.id.expanded_menu_date);
        dateToday.setText(new DateTime().toString("EEEE d MMMM"));

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_day) {
            changeContentFragment(mDayFragment);
        }
        else if (id == R.id.menu_week) {
            changeContentFragment(mWeekFragment);
        }
        else if (id == R.id.menu_month) {
            changeContentFragment(mMonthFragment);
            updateTitle();
        }
        else if (id == R.id.nav_share) {
            Intent intent = new Intent(this, OptionActivity.class);
            startActivityForResult(intent, OPTIONS_INTENT_CODE);
            overridePendingTransition(R.anim.left_to_right_anim, R.anim.right_to_left_anim);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_EVENT_INTENT_CODE || requestCode == OPTIONS_INTENT_CODE || requestCode == CONSULT_EVENT_ACTIVITY) {
            mWeekFragment.refresh();
            mMonthFragment.refresh();
            mDayFragment.refresh();
        }
    }

    private void changeContentFragment(Fragment newFragment) {
        mFragmentManager.beginTransaction()
                .replace(R.id.content_frame, newFragment)
                .commit();
    }

    private void updateTitle() {
        String month = new DateTime().monthOfYear().getAsText();
        month = month.substring(0, 1).toUpperCase() + month.substring(1);
        setTitle(month + " " + new DateTime().year().get());
    }
}
