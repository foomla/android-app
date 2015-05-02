package org.foomla.androidapp.activities.user;

import org.foomla.androidapp.FoomlaApplication;
import org.foomla.androidapp.R;
import org.foomla.api.entities.User;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class UserFragment extends SherlockFragment {
    public interface Callback {
        void onDelete();

        void onLogout();
    }

    private Callback callback;
    private User user;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);

        menuInflater.inflate(R.menu.user, menu);
        MenuItem logoutMenuItem = menu.findItem(R.id.logout);
        if (user != null) {
            if (!isLoggedIn()) {
                logoutMenuItem.setTitle(R.string.login);
            } else {
                logoutMenuItem.setTitle(R.string.logout);
            }
            logoutMenuItem.setVisible(true);
        } else {
            logoutMenuItem.setVisible(false);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        switch (menuItem.getItemId()) {

        case R.id.logout:
            callback.onLogout();
            return true;
        }

        return false;
    }

    @Override
    public void onStart() {
        super.onStart();

        Activity owner = getActivity();

        if (owner instanceof Callback) {
            callback = (Callback) owner;
        }
    }

    public void onUserLoaded(User user) {
        this.user = user;
        getActivity().invalidateOptionsMenu();
        TextView usernameTextView = (TextView) getView().findViewById(R.id.username);
        TextView emailTextView = (TextView) getView().findViewById(R.id.email);
        Button deleteAccountButton = (Button) getView().findViewById(R.id.deleteAccounttButton);
        Button logoutButton = (Button) getView().findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                callback.onLogout();
            }
        });

        if (isLoggedIn()) {
            String lastname = user.getLastname() != null ? " " + user.getLastname() : "";
            String username = user.getFirstname() + lastname;
            usernameTextView.setText(username);
            emailTextView.setText(user.getEmail());
            emailTextView.setVisibility(View.VISIBLE);
            deleteAccountButton.setVisibility(View.VISIBLE);
            logoutButton.setText(R.string.logout);
        } else {
            usernameTextView.setText(R.string.not_logged_in);
            emailTextView.setVisibility(View.GONE);
            deleteAccountButton.setVisibility(View.GONE);
            logoutButton.setText(R.string.login);
        }
    }

    private boolean isLoggedIn() {
        return this.user != null && !this.user.equals(FoomlaApplication.ANONYMOUS_USER);
    }

}
