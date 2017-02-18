package org.foomla.androidapp.activities.user;

import android.os.AsyncTask;
import android.os.Bundle;

import org.foomla.androidapp.R;
import org.foomla.androidapp.activities.BaseActivityWithNavDrawer;
import org.foomla.androidapp.activities.user.UserFragment.Callback;
import org.foomla.androidapp.utils.ProgressVisualizationUtil;
import org.foomla.api.entities.User;

public class UserActivity extends BaseActivityWithNavDrawer implements Callback {

    private UserFragment userFragment;

    public void loadUser() {
        AsyncTask<Void, Void, User> asyncTask = new AsyncTask<Void, Void, User>() {

            @Override
            protected User doInBackground(final Void... params) {
                return getFoomlaApplication().getUser();
            }

            @Override
            protected void onPostExecute(User user) {
                userFragment.onUserLoaded(user);
                ProgressVisualizationUtil.hideProgressbar(UserActivity.this);
            }

            @Override
            protected void onPreExecute() {
                ProgressVisualizationUtil.showProgressbar(UserActivity.this);
            }
        };
        asyncTask.execute();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);
        createNavDrawer();

        userFragment = new UserFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_user, userFragment).commit();

        loadUser();
    }

    @Override
    public void onDelete() {
        // TODO: implement delete account
    }

    @Override
    public void onLogout() {
        getFoomlaApplication().logout();
        finish();
    }

}
