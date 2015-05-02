package org.foomla.androidapp;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.provider.Settings.Secure;

import org.foomla.android.logging.AndroidLoggerFactory;
import org.foomla.androidapp.activities.login.LoginInfoActivity;
import org.foomla.androidapp.preferences.FoomlaPreferences;
import org.foomla.androidapp.preferences.FoomlaPreferences.Preference;
import org.foomla.androidapp.service.ExerciseService;
import org.foomla.androidapp.service.ExerciseServiceImpl;
import org.foomla.api.client.AuthorizationException;
import org.foomla.api.client.FoomlaClient;
import org.foomla.api.client.FoomlaClient.ClientType;
import org.foomla.api.client.entities.UserImpl;
import org.foomla.api.client.oauth.OAuthClientCredentials;
import org.foomla.api.client.oauth.OAuthToken;
import org.foomla.api.client.service.UserServiceProvider;
import org.foomla.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FoomlaApplication extends Application {

    public static final UserImpl ANONYMOUS_USER = new UserImpl("ANONYMOUS");

    public static final String APP_PREFERENCES_KEY = "SHARED_PREFERENCES";

    public static final String FOOMLA_LOG_TAG = "[FOOMLA]";

    private static final Logger LOGGER = LoggerFactory.getLogger(FoomlaApplication.class);

    private FoomlaClient foomlaClient;

    private ExerciseService exerciseService;

    private User user = null;

    public FoomlaClient getFoomlaClient() {
        if (foomlaClient == null) {

            OAuthClientCredentials oAuthClientCredentials = new OAuthClientCredentials(getString(R.string.client_id),
                    getString(R.string.client_secret));

            String restUrl = getString(R.string.server_url);
            String tokenUrl = getString(R.string.token_url);

            String refreshToken = FoomlaPreferences.getString(this, Preference.REFRESH_TOKEN);

            if (refreshToken != null) {
                final OAuthToken oAuthToken = new OAuthToken("invalid", refreshToken, 0);
                foomlaClient = new FoomlaClient(restUrl, tokenUrl, oAuthClientCredentials, oAuthToken);
            } else {
                foomlaClient = new FoomlaClient(restUrl, tokenUrl, oAuthClientCredentials);
            }

            try {
                String uniqueClientIdentifier = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
                foomlaClient.setUniqueClientIdentifier(uniqueClientIdentifier);
            } catch (Exception ex) {

                // on some devices this might lead to an exception
                LOGGER.error("Unable to retrieve unique client id: " + ex.getMessage(), ex);
            }

            foomlaClient.setClientType(ClientType.ANDROID);
        }

        return foomlaClient;
    }

    public ExerciseService getExerciseService() throws IOException {
        if (exerciseService == null) {
            exerciseService = new ExerciseServiceImpl(getResources());
        }

        return exerciseService;
    }

    public String getOAuthAuthorizeUrl() {
        return getString(R.string.auth_url) + "?client_id=" + getString(R.string.client_id) + "&redirect_uri="
                + getString(R.string.redirect_url);
    }

    public String getOAuthRedirectUrl() {
        return getString(R.string.redirect_url);
    }

    public User getUser() {

        if (this.user != null) {
            return this.user;
        }

        if (foomlaClient != null) {

            UserServiceProvider userServiceProvider = foomlaClient.getProvider(UserServiceProvider.class);
            try {
                User user = userServiceProvider.getCurrent();
                if (user != null) {
                    this.user = user;
                } else {
                    this.user = ANONYMOUS_USER;
                }
            } catch (AuthorizationException authorizationException) {
                this.user = ANONYMOUS_USER;
            } catch (Exception ex) {
                LOGGER.error("Error while retrieving current user: " + ex.getMessage(), ex);
            }
        }

        return user;
    }

    public String getVersionName() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            return "";
        }
    }

    public boolean isLoggedIn() {
        return foomlaClient != null && foomlaClient.isAuthorized();
    }

    public void logout() {
        FoomlaPreferences.setBoolean(this, Preference.LOGIN, true);
        FoomlaPreferences.setString(this, Preference.REFRESH_TOKEN, null);

        Intent intent = new Intent(this, LoginInfoActivity.class);
        foomlaClient = null;
        user = null;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void initLogging() {
        AndroidLoggerFactory.setLogTag(FOOMLA_LOG_TAG);
    }
}
