package com.hwuiwon.alma;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView overviewLV;
    String barTitle = "Overview";
    // Sample Overviews
    private Overview[] overviews = {new Overview("P1", "AP Calculus BC", "A-"),
                                    new Overview("P2", "AP Economics", "A"),
                                    new Overview("P3", "AP Physics 1", "A+"),
                                    new Overview("P4", "English 11", "A"),
                                    new Overview("P5", "AP Computer Science", "A+"),
                                    new Overview("P6", "HS PE", "A"),
                                    new Overview("P7", "Study Hall 1", "-"),
                                    new Overview("P8", "Study Hall 2", "-")};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO : work on parsing and putting them in OverviewAdapter (JSOUP)
        overviewLV = (ListView)findViewById(R.id.overviewLV);
        final OverviewAdapter adapter = new OverviewAdapter(this);

        for (Overview ov : overviews) {
            adapter.addOverview(ov);
        }

        overviewLV.setAdapter(adapter);
        overviewLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), MoreOverviewActivity.class);
                Overview overview = overviews[i];
                String className = overviews[i].getClassName();
                intent.putExtra("overview", overview);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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

        if (id == R.id.nav_overview) {
            barTitle = "Overview";
            getSupportActionBar().setTitle(barTitle);
        } else if (id == R.id.nav_assignments) {
            barTitle = "Assignments";
            getSupportActionBar().setTitle(barTitle);
        } else if (id == R.id.nav_grades) {
            barTitle = "Grades";
            getSupportActionBar().setTitle(barTitle);
        } else if (id == R.id.nav_classmates) {
            barTitle = "Classmates";
            getSupportActionBar().setTitle(barTitle);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}