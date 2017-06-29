package com.hwuiwon.alma;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView overviewLV;
    private String barTitle = "Overview";
    private String username;
    private String password;

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

        username = getIntent().getStringExtra("id");
        password = getIntent().getStringExtra("pass");

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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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