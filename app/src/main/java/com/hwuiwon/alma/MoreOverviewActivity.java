package com.hwuiwon.alma;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hwuiwon.alma.Assignments.Assignment;
import com.hwuiwon.alma.Assignments.AssignmentAdapter;
import com.hwuiwon.alma.Grades.Grade;
import com.hwuiwon.alma.Grades.GradeAdapter;
import com.hwuiwon.alma.Overviews.Overview;

public class MoreOverviewActivity extends AppCompatActivity {

    private TextView currentMenuTV;
    private ListView moreOverviewLV;

    private String cookie;
    private String classID = null;

    private Assignment[] assignments;
    private Grade[] grades = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_grades:
                    currentMenuTV.setText(getString(R.string.title_home));
                    moreOverviewLV.setAdapter(makeGradeAdapter());
                    return true;
                case R.id.nav_assignments:
                    currentMenuTV.setText(getString(R.string.title_assignments));
                    moreOverviewLV.setAdapter(makeAssignmentAdapter());
                    return true;
            }
            return false;
        }
    };
    private View progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_overview);

        Overview overview = getIntent().getParcelableExtra("overview");
        cookie = getIntent().getStringExtra("cookie");
        classID = getIntent().getStringExtra("classID");

        TextView classNameTV = (TextView) findViewById(R.id.classNameTV);
        TextView gradeTV = (TextView) findViewById(R.id.gradeTV);
        currentMenuTV = (TextView) findViewById(R.id.currentMenuTV);
        moreOverviewLV = (ListView) findViewById(R.id.moreOverviewLV);
        progressView = findViewById(R.id.more_progress);

        classNameTV.setText(overview.getClassName());
        gradeTV.setText(overview.getAlphabetGrade());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Set List view to default
        moreOverviewLV.setAdapter(makeGradeAdapter());
    }

    public GradeAdapter makeGradeAdapter() {
        final GradeAdapter adapter = new GradeAdapter(this);
        try {
//            showProgress(true);
            grades = new GradeTask().execute(classID, cookie).get();
//            showProgress(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Grade grade : grades) {
            adapter.addGrade(grade);
        }

        return adapter;
    }

    public AssignmentAdapter makeAssignmentAdapter() {
        final AssignmentAdapter adapter = new AssignmentAdapter(this);
        try {
//            showProgress(true);
            assignments = new AssignmentTask().execute(classID, cookie).get();
//            showProgress(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Assignment assignment : assignments) {
            adapter.addAssignment(assignment);
        }

        return adapter;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        moreOverviewLV.setVisibility(show ? View.GONE : View.VISIBLE);
        moreOverviewLV.animate().setDuration(shortAnimTime).alpha(
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
}