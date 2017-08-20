package com.hwuiwon.alma;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hwuiwon.alma.Assignments.Assignment;
import com.hwuiwon.alma.Assignments.AssignmentAdapter;
import com.hwuiwon.alma.Grades.Grade;
import com.hwuiwon.alma.Grades.GradeAdapter;
import com.hwuiwon.alma.Overviews.Overview;
import com.hwuiwon.alma.Tasks.AssignmentTask;
import com.hwuiwon.alma.Tasks.GradeTask;

import java.io.File;

public class MoreOverviewActivity extends AppCompatActivity {

    private TextView currentMenuTV;
    private ListView moreOverviewLV;

    private String cookie;
    private String classID = null;
    private String className;

    private Assignment[] assignments;
    private Grade[] grades = null;

    private AssignmentGradeTask task = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            showProgress(true);
            switch (item.getItemId()) {
                case R.id.nav_grades:
                    currentMenuTV.setText(getString(R.string.title_home));
                    task = new AssignmentGradeTask();
                    task.execute(SET_GRADES);
//                    moreOverviewLV.setAdapter(makeGradeAdapter());
                    return true;
                case R.id.nav_assignments:
                    currentMenuTV.setText(getString(R.string.title_assignments));
                    task = new AssignmentGradeTask();
                    task.execute(SET_ASSGINMENTS);
//                    moreOverviewLV.setAdapter(makeAssignmentAdapter());
                    return true;
            }
            return false;
        }
    };
    private ProgressBar progressView;
    private Integer SET_GRADES = 0;
    private Integer SET_ASSGINMENTS = 1;

    private SwipeRefreshLayout mSwipeRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_overview);

        Overview overview = getIntent().getParcelableExtra("overview");
        cookie = getIntent().getStringExtra("cookie");
        classID = getIntent().getStringExtra("classID");
        className = overview.getClassName();

        TextView classNameTV = (TextView) findViewById(R.id.classNameTV);
        TextView gradeTV = (TextView) findViewById(R.id.gradeTV);
        currentMenuTV = (TextView) findViewById(R.id.currentMenuTV);
        moreOverviewLV = (ListView) findViewById(R.id.moreOverviewLV);
        progressView = (ProgressBar) findViewById(R.id.more_progress);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.more_swipe_refresh);

        currentMenuTV.setText(getString(R.string.title_home));

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String currentMenu = (String) currentMenuTV.getText();

                int mode = SET_GRADES;
                String filename = className+"-grades";

                if(currentMenu.equals(getString(R.string.title_assignments))) {
                    mode = SET_ASSGINMENTS;
                    filename = className+"-assignments";
                    assignments = null;
                } else if(currentMenu.equals(getString(R.string.title_home))) {
                    grades = null;
                }

                File file = MoreOverviewActivity.this.getCacheDir();
                File delFile = new File(file, filename);

                if(!delFile.delete()) {
                    Log.e("MoreOverviewActivity", "File delete fail : " + filename);
                }
                    showProgress(true);
                    task = new AssignmentGradeTask();
                    task.execute(mode);
            }
        });

        classNameTV.setText(className);
        gradeTV.setText(overview.getAlphabetGrade());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Set List view to default
        showProgress(true);
        task = new AssignmentGradeTask();
        task.execute(SET_GRADES);
//        moreOverviewLV.setAdapter(makeGradeAdapter());
    }

//    public GradeAdapter makeGradeAdapter() {
//        final GradeAdapter adapter = new GradeAdapter(this);
//        if(grades==null) {
//            try {
////                showProgress(true);
//                grades = new GradeTask().execute(classID, cookie).get();
////                showProgress(false);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        for (Grade grade : grades) {
//            adapter.addGrade(grade);
//        }
//
//        return adapter;
//    }
//
//    public AssignmentAdapter makeAssignmentAdapter() {
//        final AssignmentAdapter adapter = new AssignmentAdapter(this);
//
//        if (assignments == null) {
//            try {
////                showProgress(true);
//                assignments = new AssignmentTask().execute(classID, cookie).get();
////                showProgress(false);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        for (Assignment assignment : assignments) {
//            adapter.addAssignment(assignment);
//        }
//
//        return adapter;
//    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mSwipeRefresh.setVisibility(show ? View.GONE : View.VISIBLE);
        mSwipeRefresh.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                moreOverviewLV.setVisibility(show ? View.GONE : View.VISIBLE);
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

    private class AssignmentGradeTask extends AsyncTask<Integer, Void, ListAdapter> {

        @Override
        protected ListAdapter doInBackground(Integer... integers) {
            DataIO dataIO = new DataIO(MoreOverviewActivity.this);
            File file = MoreOverviewActivity.this.getCacheDir();

            if(integers[0].equals(SET_GRADES)){
                final GradeAdapter adapter = new GradeAdapter(MoreOverviewActivity.this);
                if(grades==null) {
                    if(new File(file, className+"-grades").exists()){
                        String[] arr = dataIO.readFile(className+"-grades").split("##");
                        grades = new Grade[arr.length-1];
                        for(int i=0; i<arr.length-1; i++) {
                            if(!arr[i].isEmpty()) grades[i] = new Grade(arr[i]);
                        }
                    } else {
                        try {
                            grades = new GradeTask().execute(classID, cookie);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String str = "";
                        for(Grade g : grades) {
                            Log.d("Tag", g.toString());
                            str += g.toString()+"##";
                        }
                        dataIO.writeFile(className+"-grades", str);
                    }
                }

                for (Grade grade : grades) {
                    adapter.addGrade(grade);
                }
                return adapter;
            } else if(integers[0].equals(SET_ASSGINMENTS)) {
                AssignmentAdapter adapter = new AssignmentAdapter(MoreOverviewActivity.this);

                if (assignments == null) {
                    if(new File(file, className+"-assignments").exists()){
                        String[] arr = dataIO.readFile(className+"-assignments").split("##");
                        assignments = new Assignment[arr.length-1];
                        for(int i=0; i<arr.length-1; i++) {
                            Log.d("Tag", arr[i]);
                            assignments[i] = new Assignment(arr[i]);
                        }
                    } else {
                        try {
                            assignments = new AssignmentTask().execute(classID, cookie);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String str = "";
                        for(Assignment a : assignments) {
                            Log.d("Tag", a.toString());
                            str += a.toString()+"##";
                        }
                        dataIO.writeFile(className+"-assignments", str);
                    }
                }

                for (Assignment assignment : assignments) {
                    adapter.addAssignment(assignment);
                }
                return adapter;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ListAdapter adapter) {
            task = null;
            showProgress(false);
            mSwipeRefresh.setRefreshing(false);
            moreOverviewLV.setAdapter(adapter);
        }

        @Override
        protected void onCancelled() {
            task = null;
            showProgress(false);
            mSwipeRefresh.setRefreshing(false);
        }
    }
}