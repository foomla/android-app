package org.foomla.androidapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.foomla.androidapp.FoomlaApplication;
import org.foomla.androidapp.R;
import org.foomla.androidapp.activities.edittraining.EditTrainingActivity;
import org.foomla.androidapp.activities.exercisebrowser.ExerciseBrowserActivity;
import org.foomla.androidapp.activities.info.InfoActivity;
import org.foomla.androidapp.activities.main.MainActivity;
import org.foomla.androidapp.activities.mytrainings.MyTrainingsActivity;

public abstract class BaseActivityWithNavDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        // Make sure we use vector drawables
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    protected abstract int getLayoutId();

    public FoomlaApplication getFoomlaApplication() {
        return (FoomlaApplication) this.getApplication();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:
                this.startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.new_training:
                this.startActivity(new Intent(this, EditTrainingActivity.class));
                break;

            case R.id.trainings:
                this.startActivity(new Intent(this, MyTrainingsActivity.class));
                break;

            case R.id.exercise_browser:
                this.startActivity(new Intent(this, ExerciseBrowserActivity.class));
                break;

            case R.id.info:
                this.startActivity(new Intent(this, InfoActivity.class));
                break;

            default:
                break;
        }
        return false;
    }

    public DrawerLayout getDrawer() {
        return drawer;
    }
}
