package com.hwuiwon.alma;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;
    private SharedPreferences.Editor loginPrefsEditor;

    private EditText usernameET;
    private EditText passwordET;
    private View progressView;
    private View loginFormView;
    private CheckBox usernameCB;
    private CheckBox autoLoginCB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = (EditText) findViewById(R.id.username);
        passwordET = (EditText) findViewById(R.id.password);
        usernameCB = (CheckBox) findViewById(R.id.usernameCB);
        autoLoginCB = (CheckBox) findViewById(R.id.autoLoginCB);
        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);

        SharedPreferences loginPrefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPrefs.edit();

        Boolean saveUsername = loginPrefs.getBoolean("saveUsername", false);
        final Boolean autoLogin = loginPrefs.getBoolean("autoLogin", false);
        if (saveUsername) {
            usernameET.setText(loginPrefs.getString("username", ""));
            usernameCB.setChecked(true);
            passwordET.requestFocus();
        }
        if (autoLogin) {
            passwordET.setText(loginPrefs.getString("password", ""));
            autoLoginCB.setChecked(true);
            attemptLogin();
        }

        usernameCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b) {
                    autoLoginCB.setChecked(false);
                }
            }
        });

        autoLoginCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    usernameCB.setChecked(true);
                }
            }
        });

        passwordET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getApplicationWindowToken(),0);
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);
                attemptLogin();
            }
        });
        loginPrefsEditor.apply();
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        usernameET.setError(null);
        passwordET.setError(null);
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            usernameET.setError(getString(R.string.error_field_required));
            focusView = usernameET;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            passwordET.setError(getString(R.string.error_field_required));
            focusView = passwordET;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        loginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
    public void onBackPressed() {
        if(mAuthTask != null){
            mAuthTask.cancel(true);
        } else {
            super.onBackPressed();
        }
    }

    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String username;
        private final String password;
        private String cookie = "";
        private int status;
//        private ArrayList<String> schoolYears = new ArrayList<>();

        UserLoginTask(String id, String pass) {
            username = id;
            password = pass;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                Connection.Response response = Jsoup.connect("https://spps.getalma.com/login")
                        .data("username", username).data("password", password)
                        .method(Connection.Method.POST).execute();
                status = response.statusCode();
                cookie = response.cookies().keySet().toArray()[0] + "=" + response.cookies().values().toArray()[0];
//                Document document = Jsoup.connect("https://spps.getalma.com/home").header("Cookie", cookie).get();
//                Log.d("tag", document.select(".subnav > ul").get(0).html());
//                Elements elements = document.select(".subnav > ul > li");
//                for(Element e : elements) {
//                    schoolYears.add(e.select("a").get(0).attr("href"));
//                }
//                for(String s : schoolYears){
//                    Log.d("Tag", s);
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return status == 200;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            loginPrefsEditor.putBoolean("saveUsername", usernameCB.isChecked());
            loginPrefsEditor.putString("username", usernameET.getText().toString());
            loginPrefsEditor.putString("password", passwordET.getText().toString());

            if (success) {
                loginPrefsEditor.putBoolean("autoLogin", autoLoginCB.isChecked());
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("cookie", cookie);
//                intent.putStringArrayListExtra("schoolYears", schoolYears);
                startActivity(intent);
            } else if (status == 405) {
                Toast.makeText(LoginActivity.this, "Alma is currently undergoing maintenance", Toast.LENGTH_LONG).show();
            } else {
                passwordET.setError(getString(R.string.error_incorrect));
                passwordET.setText("");
                passwordET.requestFocus();
                loginPrefsEditor.putBoolean("autoLogin", false);
                loginPrefsEditor.apply();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(passwordET,0);
            }
            loginPrefsEditor.apply();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}