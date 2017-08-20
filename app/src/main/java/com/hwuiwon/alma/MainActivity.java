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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hwuiwon.alma.Overviews.Overview;
import com.hwuiwon.alma.Overviews.OverviewAdapter;
import com.hwuiwon.alma.Tasks.ClassIdTask;
import com.hwuiwon.alma.Tasks.OverviewTask;
import com.hwuiwon.alma.Tasks.ProfileImageTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String barTitle = "Overview";
    private String cookie;

    private HashMap<String, String> classIDs = null;
    private ArrayList<Overview> overviews = null;
    private ListView overviewLV = null;
    private View progressView = null;
    private TextView headerTV1;
    private TextView headerTV2;
    private CircleImageView profile_image;
    private OverviewAdapter adapter;

    private SwipeRefreshLayout mSwipeRefresh;

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

        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.main_swipe_refresh);
        overviewLV = (ListView) findViewById(R.id.overviewLV);
        progressView = findViewById(R.id.main_progress);
        adapter = new OverviewAdapter(this);


        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                File file = MainActivity.this.getCacheDir();
                File delFile = new File(file, "overview");

                if(!delFile.delete()) {
                    Log.e("MainActivity", "File delete fail : overview");
                }

                showProgress(true);
                mLoadingTask = new LoadingTask(cookie);
                mLoadingTask.execute((Void) null);
            }
        });

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

        showProgress(true);
        mLoadingTask = new LoadingTask(cookie);
        mLoadingTask.execute((Void) null);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            trimCache(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        assert dir != null;
        return dir.delete();
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

            DataIO dataIO = new DataIO(MainActivity.this);
            File file = MainActivity.this.getCacheDir();

            if(new File(file, "overview").exists()){
                overviews = new ArrayList<>();
                String[] arr = dataIO.readFile("overview").split("\n#\n");
                for(String s : arr) {
                    Log.d("MainActivity", s);
                    if(s.split("\n").length>3) overviews.add(new Overview(s));
                }
            } else {
                overviews = new OverviewTask().execute(cookie);
                String str = "";
                for(Overview o : overviews) {
                    str += o.toString()+"\n#\n";
                }
                dataIO.writeFile("overview", str);
            }

            if(new File(file, "classIDs").exists()) {
                classIDs = new HashMap<>();
                String[] arr = dataIO.readFile("classIDs").replaceAll("[{}]", "").split(", ");
                for(String s : arr) {
                    classIDs.put(s.split("=")[0], s.split("=")[1]);
                }
            } else {
                classIDs = new ClassIdTask().execute(cookie);
                dataIO.writeFile("classIDs", classIDs.toString());
            }
            profilePic = new ProfileImageTask(MainActivity.this).execute(cookie);

            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            mLoadingTask = null;
            showProgress(false);
            mSwipeRefresh.setRefreshing(false);

            headerTV1.setText(classIDs.get("name"));
            headerTV2.setText(classIDs.get("role"));
            Bitmap b = new ImageIO(MainActivity.this).setFilepath(profilePic.split(":")[0]).setFileName(profilePic.split(":")[1]).load();

            profile_image.setImageBitmap(b);

            adapter = new OverviewAdapter(MainActivity.this);
            adapter.clear();
            for (Overview ov : overviews) {
                adapter.addOverview(ov);
            }

            overviewLV.setAdapter(adapter);
            overviewLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(), MoreOverviewActivity.class);
                    Overview overview = overviews.get(i);
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
            mSwipeRefresh.setRefreshing(false);
        }
    }
}