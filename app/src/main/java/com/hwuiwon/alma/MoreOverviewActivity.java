package com.hwuiwon.alma;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.hwuiwon.alma.Assignments.Assignment;
import com.hwuiwon.alma.Assignments.AssignmentAdapter;
import com.hwuiwon.alma.Grades.Grade;
import com.hwuiwon.alma.Grades.GradeAdapter;
import com.hwuiwon.alma.Overviews.Overview;

public class MoreOverviewActivity extends AppCompatActivity {

    private TextView currentMenuTV;
    private TextView gradeTV;
    private TextView classNameTV;
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
                    currentMenuTV.setText("Grades");
                    moreOverviewLV.setAdapter(makeGradeAdapter());
                    return true;
                case R.id.nav_assignments:
                    currentMenuTV.setText("Assignments");
                    moreOverviewLV.setAdapter(makeAssignmentAdapter());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_overview);

        Overview overview = getIntent().getParcelableExtra("overview");
        cookie = getIntent().getStringExtra("cookie");
        classID = getIntent().getStringExtra("classID");

        currentMenuTV = (TextView) findViewById(R.id.currentMenuTV);
        classNameTV = (TextView) findViewById(R.id.classNameTV);
        gradeTV = (TextView) findViewById(R.id.gradeTV);
        moreOverviewLV = (ListView) findViewById(R.id.moreOverviewLV);

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
            grades = new GradeTask().execute(classID, cookie).get();
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
            assignments = new AssignmentTask().execute(classID, cookie).get();
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
}