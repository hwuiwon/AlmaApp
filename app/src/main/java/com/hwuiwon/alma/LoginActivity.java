package com.hwuiwon.alma;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;
    private SharedPreferences.Editor loginPrefsEditor;

    private EditText usernameET;
    private EditText passwordET;
    private View progressView;
    private View loginFormView;
    private CheckBox usernameCB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = (EditText) findViewById(R.id.username);
        passwordET = (EditText) findViewById(R.id.password);
        usernameCB = (CheckBox) findViewById(R.id.usernameCB);
        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);

        SharedPreferences loginPrefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPrefs.edit();

        Boolean saveLogin = loginPrefs.getBoolean("saveLogin", false);
        if (saveLogin) {
            usernameET.setText(loginPrefs.getString("username", ""));
            usernameCB.setChecked(true);
            passwordET.requestFocus();
        }

        passwordET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
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
                if (usernameCB.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", usernameET.getText().toString());
                    loginPrefsEditor.apply();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }
                attemptLogin();
            }
        });
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

    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String username;
        private final String password;
        private boolean connected = true;
        private int status;

        private URLConnection urlConnection = null;
        private OutputStream outputStream = null;

        UserLoginTask(String id, String pass) {
            username = id;
            password = pass;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ConnectivityManager cm =
                    (ConnectivityManager)LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            connected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            String payload = "{\"username\":\""+username+"\",\"password\":\""+password+"\"}";
            if (!connected) { return false; }

            try {
                urlConnection = new URL("https://spps.getalma.com/login").openConnection();
                HttpsURLConnection https = (HttpsURLConnection) urlConnection;
                https.setDoOutput(true);
                https.setRequestMethod("POST");
                outputStream = https.getOutputStream();
                outputStream.write(payload.getBytes());
                https.connect();
                status = https.getResponseCode();
                https.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return status == 200;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("id", String.valueOf(usernameET.getText()));
                intent.putExtra("pass", String.valueOf(passwordET.getText()));
                startActivity(intent);
            } else if (status == 405) {
                Toast.makeText(LoginActivity.this, "Alma is currently undergoing maintenance", Toast.LENGTH_LONG).show();
            } else {
                passwordET.setError(getString(R.string.error_incorrect));
                passwordET.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}