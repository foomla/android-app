package org.foomla.androidapp.activities;

import org.foomla.androidapp.FoomlaApplication;
import org.foomla.androidapp.R;
import org.foomla.androidapp.activities.exercisedetail.RateExerciseHandler;

import org.foomla.api.client.FoomlaClient;
import org.foomla.api.client.entities.ExerciseImpl;
import org.foomla.api.entities.twizard.Exercise;

import android.app.Activity;

import android.os.Bundle;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_view);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final FoomlaClient foomlaClient = ((FoomlaApplication) getApplication()).getFoomlaClient();
        final Exercise exercise = new ExerciseImpl();
        exercise.setId(1);
        exercise.setTitle("Test Exercise");

        new RateExerciseHandler(this, foomlaClient).handle(exercise, 4);
    }
}
