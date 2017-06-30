package com.hwuiwon.alma;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class MoreOverviewActivity extends AppCompatActivity {

    private TextView currentMenuTV;
    private TextView gradeTV;
    private TextView classNameTV;
    private ListView moreOverviewLV;

    private Assignment[] assignments = {
            new Assignment("5/30/17", "Final Draft of DOAS Essay Due", "Final Exam",
                            "1 hour to work on it QUIETLY in class. \nSubmit to turnitin.com\n" +
                            "Remaining time to be used preparing for upcoming exams."),
            new Assignment("5/25/17", "Participation Days 31-40", "Participation", ""),
            new Assignment("5/23/17", "Notebook/Folder Check #4", "Participation", ""),
            new Assignment("5/19/17", "Topic + Thesis + Outline", "Homework",
                            "Have a look at the attached document and choose a topic. Create a " +
                            "\"working thesis\" and sketch out an outline. We will keep working " +
                            "on your outline in class.")
    };

    private Grade[] grades = {
            new Grade("Final Draft of DOAS Essay Due", "A", "95%", "6/3/17", "10%"),
            new Grade("Participation Days 31-40", "A", "94%", "5/29/17", "3.1%"),
            new Grade("Notebook/Folder Check #4", "A+", "98%", "5/25/17", "3.1%"),
            new Grade("Topic + Thesis + Outline", "A+", "100%", "5/19/17", "1.9%"),
            new Grade("Death of a Salesman Test", "A", "95.5%", "5/19/17", "7.5%")
    };

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

    public AssignmentAdapter makeAssignmentAdapter() {
        final AssignmentAdapter adapter = new AssignmentAdapter(this);
        for (Assignment assignment : assignments) {
            adapter.addAssignment(assignment);
        }

        return adapter;
    }

    public GradeAdapter makeGradeAdapter() {
        final GradeAdapter adapter = new GradeAdapter(this);
        for (Grade grade : grades) {
            adapter.addGrade(grade);
        }

        return adapter;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}