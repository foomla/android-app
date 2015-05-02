package org.foomla.androidapp.activities;

import org.foomla.androidapp.FoomlaApplication;
import org.foomla.androidapp.R;
import org.foomla.androidapp.activities.NavigationListAdapter.NavigationItem;
import org.foomla.androidapp.activities.edittraining.EditTrainingActivity;
import org.foomla.androidapp.activities.exercisebrowser.ExerciseBrowserActivity;
import org.foomla.androidapp.activities.info.InfoActivity;
import org.foomla.androidapp.activities.main.MainActivity;
import org.foomla.androidapp.activities.mytrainings.MyTrainingsActivity;
import org.foomla.androidapp.activities.news.NewsActivity;
import org.foomla.androidapp.activities.user.UserActivity;
import org.foomla.api.entities.User;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ActivityNavDrawer {

    private final ActionBar actionBar;

    private final Activity activity;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ViewGroup drawerListLayout;
    private ActionBarDrawerToggle drawerToggle;

    public ActivityNavDrawer(final Activity activity, final ActionBar actionBar) {
        this.activity = activity;
        this.actionBar = actionBar;
    }

    public void create() {
        setAppVersionInBottomTextView();

        drawerListLayout = (ViewGroup) activity.findViewById(R.id.left_drawer);
        drawerList = (ListView) activity.findViewById(R.id.left_drawer_list);
        drawerList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int pos,
                    final long rowId) {
                drawerLayout.closeDrawer(drawerListLayout);

                NavigationItem navigationItem = NavigationListAdapter.getItemByIndex(pos);
                switch (navigationItem) {

                case HOME :
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    break;

                case NEW_TRAINING :
                    activity.startActivity(new Intent(activity, EditTrainingActivity.class));
                    break;

                case TRAININGS :
                    activity.startActivity(new Intent(activity, MyTrainingsActivity.class));
                    break;

                case EXERCISE_CATALOG :
                    activity.startActivity(new Intent(activity, ExerciseBrowserActivity.class));
                    break;

                case INFO :
                    activity.startActivity(new Intent(activity, InfoActivity.class));
                    break;

                default :
                    break;
                }
            }
        });
        drawerList.setAdapter(new NavigationListAdapter(activity));

        drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {

            @Override
            public void onDrawerClosed(final View view) { }

            @Override
            public void onDrawerOpened(final View drawerView) { }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    public void syncState() {
        drawerToggle.syncState();
    }

    public void toggleNavDrawer() {

        if (drawerLayout.isDrawerOpen(drawerListLayout)) {
            drawerLayout.closeDrawer(drawerListLayout);
        } else {
            drawerLayout.openDrawer(drawerListLayout);
        }

    }

    private void setAppVersionInBottomTextView() {
        String textWithAppVersion = String.format(activity.getString(R.string.menu_app_info_bottom),
                ((FoomlaApplication) activity.getApplication()).getVersionName());

        ((TextView) activity.findViewById(R.id.app_info_bottom)).setText(textWithAppVersion);
    }
}
