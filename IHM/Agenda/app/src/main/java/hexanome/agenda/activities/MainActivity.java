package hexanome.agenda.activities;

import android.content.Intent;
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

public class MainActivity extends ActionBarActivity implements NavigationView.OnNavigationItemSelectedListener {

    private MonthFragment mMonthFragment;
    private WeekFragment mWeekFragment;
    private DayFragment mDayFragment;
    private FragmentManager mFragmentManager;

    private static final int ADD_EVENT_INTENT_CODE = 120;

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

        // Insert the fragment by replacing any existing fragment
        mFragmentManager = getSupportFragmentManager();
        mMonthFragment = new MonthFragment();
        changeContentFragment(mMonthFragment);

        mWeekFragment = new WeekFragment();
        mDayFragment = new DayFragment();
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
            changeContentFragment(mDayFragment);
        }
        else if (id == R.id.menu_week) {
            changeContentFragment(mWeekFragment);
        }
        else if (id == R.id.menu_month) {
            changeContentFragment(mMonthFragment);
        }
        else if (id == R.id.nav_share) {
            startActivity(new Intent(this, OptionActivity.class));
            overridePendingTransition(R.anim.left_to_right_anim, R.anim.right_to_left_anim);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_EVENT_INTENT_CODE) {
            mWeekFragment.refresh();
            mMonthFragment.refresh();
        }
    }

    private void changeContentFragment(Fragment newFragment){
        mFragmentManager.beginTransaction()
                .replace(R.id.content_frame, newFragment)
                .commit();
    }
}