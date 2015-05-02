package org.foomla.androidapp.activities.login;

import org.foomla.androidapp.FoomlaApplication;
import org.foomla.androidapp.R;
import org.foomla.androidapp.activities.BaseActivity;
import org.foomla.androidapp.activities.main.MainActivity;
import org.foomla.androidapp.preferences.FoomlaPreferences;
import org.foomla.androidapp.preferences.FoomlaPreferences.Preference;
import org.foomla.api.client.FoomlaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginInfoActivity extends BaseActivity {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginInfoActivity.class);
    private static final int REQUEST_CODE = 100;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            final String authCode = data.getStringExtra(LoginActivity.CODE);
            authorizeAndStartMainActivity(authCode);
        } else if (requestCode == REQUEST_CODE && resultCode == RESULT_CANCELED) {
            if (data != null) {
                final String error = data.getStringExtra(LoginActivity.ERROR);
                LOGGER.warn("Login failed: " + error);
            } else {
                LOGGER.info("Login canceled.");
            }
            // TODO: define what to do
        } else {
            // TODO: define what to do
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_info);
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showLoginActivity(false);
            }
        });
        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showLoginActivity(true);
            }
        });
        Button noLoginButton = (Button) findViewById(R.id.no_login_button);
        noLoginButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                FoomlaPreferences.setBoolean(LoginInfoActivity.this, Preference.LOGIN, false);
                FoomlaPreferences.setString(LoginInfoActivity.this, Preference.REFRESH_TOKEN, null);
                startMainActivity();
            }
        });
    }

    private void authorizeAndStartMainActivity(final String authCode) {
        final FoomlaClient foomlaClient = ((FoomlaApplication) getApplication()).getFoomlaClient();
        AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {

            private ProgressDialog progressDialog;

            @Override
            protected String doInBackground(Void... params) {
                foomlaClient.authorize(authCode);
                return foomlaClient.getRestConnectionSettings().oAuthToken.getRefreshToken();
            }

            @Override
            protected void onPostExecute(String refreshToken) {
                FoomlaPreferences.setString(LoginInfoActivity.this, Preference.REFRESH_TOKEN, refreshToken);
                progressDialog.dismiss();
                startMainActivity();
            }

            @Override
            protected void onPreExecute() {
                progressDialog = ProgressDialog.show(LoginInfoActivity.this, "Login", "Anmelden...");
            }
        };
        asyncTask.execute();
    }

    private void showLoginActivity(boolean register) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("register", register);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
