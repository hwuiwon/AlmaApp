package com.hwuiwon.alma;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.hwuiwon.alma.Overviews.Overview;
import com.hwuiwon.alma.Overviews.OverviewAdapter;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView overviewLV;
    private String barTitle = "Overview";
    private String cookie;
    private HashMap<String, String> classIDs = null;

    private Overview[] overviews = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cookie = getIntent().getStringExtra("cookie");

        overviewLV = (ListView)findViewById(R.id.overviewLV);
        final OverviewAdapter adapter = new OverviewAdapter(this);

        try {
            overviews = new OverviewTask().execute(cookie).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Overview ov : overviews) {
            adapter.addOverview(ov);
        }

        try {
            classIDs = new ClassIdTask().execute(cookie).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        overviewLV.setAdapter(adapter);
        overviewLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), MoreOverviewActivity.class);
                Overview overview = overviews[i];
                intent.putExtra("classID", classIDs.get(overview.getOriginalClassName()));
                intent.putExtra("overview", overview);
                intent.putExtra("cookie", cookie);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.nav_assignments:
                barTitle = "Assignments";
                break;
            case R.id.nav_grades:
                barTitle = "Grades";
                break;
            case R.id.nav_classmates:
                barTitle = "Classmates";
                break;
            case R.id.nav_overview:
                barTitle = "Overview";
                break;
        }

        getSupportActionBar().setTitle(barTitle);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}