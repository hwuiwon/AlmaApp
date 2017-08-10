package com.hwuiwon.alma;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hwuiwon.alma.Overviews.Overview;
import com.hwuiwon.alma.Overviews.OverviewAdapter;
import com.hwuiwon.alma.Tasks.ClassIdTask;
import com.hwuiwon.alma.Tasks.OverviewTask;
import com.hwuiwon.alma.Tasks.ProfileImageTask;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String barTitle = "Overview";
    private String cookie;

    private HashMap<String, String> classIDs = null;
    private Overview[] overviews = null;
    private ListView overviewLV = null;
    private View progressView = null;
    private TextView headerTV1;
    private TextView headerTV2;
    private CircleImageView profile_image;
    private OverviewAdapter adapter;

//    private ArrayList<String> schoolYears;

    private String profilePic = null;

    private LoadingTask mLoadingTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cookie = getIntent().getStringExtra("cookie");
//        schoolYears = getIntent().getStringArrayListExtra("schoolYears");

        overviewLV = (ListView) findViewById(R.id.overviewLV);
        progressView = findViewById(R.id.main_progress);
        adapter = new OverviewAdapter(this);



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        headerView = getLayoutInflater().inflate(R.layout.nav_header_main, null);
        View headerView = navigationView.getHeaderView(0);
        headerTV1 = headerView.findViewById(R.id.headerTV1);
        headerTV2 = headerView.findViewById(R.id.headerTV2);
        profile_image = headerView.findViewById(R.id.profile_image);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        try {
            showProgress(true);
            mLoadingTask = new LoadingTask(cookie);
            mLoadingTask.execute((Void) null);
//            showProgress(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.nav_overview:
                barTitle = "Overview";
                break;
            case R.id.nav_directory:
                Intent i = new Intent(getApplicationContext(), DirectoryActivity.class);
                i.putExtra("cookie", cookie);
                startActivity(i);
                break;
        }

        getSupportActionBar().setTitle(barTitle);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        overviewLV.setVisibility(show ? View.GONE : View.VISIBLE);
        overviewLV.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                overviewLV.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });
        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        progressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private class LoadingTask extends AsyncTask<Void, Void, Void> {

        private String cookie = "";

        LoadingTask(String cookie) {
            this.cookie = cookie;
        }

        @Override
        protected Void doInBackground(Void... params) {

//            try {
//                Document document = Jsoup.connect("https://spps.getalma.com"+schoolYears.get(1)).header("Cookie", cookie).get();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            overviews = new OverviewTask().execute(cookie);
            classIDs = new ClassIdTask().execute(cookie);
            profilePic = new ProfileImageTask(MainActivity.this).execute(cookie);

            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            mLoadingTask = null;
            showProgress(false);

            headerTV1.setText(classIDs.get("name"));
            headerTV2.setText(classIDs.get("role"));
            Bitmap b = new ImageIO(MainActivity.this).setFilepath(profilePic.split(":")[0]).setFileName(profilePic.split(":")[1]).load();

            profile_image.setImageBitmap(b);

            for (Overview ov : overviews) {
                adapter.addOverview(ov);
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
        }

        @Override
        protected void onCancelled() {
            mLoadingTask = null;
            showProgress(false);
        }
    }
}