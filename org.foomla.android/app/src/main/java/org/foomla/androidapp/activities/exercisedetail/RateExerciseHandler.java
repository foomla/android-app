package org.foomla.androidapp.activities.exercisedetail;

import org.foomla.androidapp.activities.login.LoginActivity;
import org.foomla.androidapp.data.Comment;
import org.foomla.androidapp.data.ExerciseRatingResult;
import org.foomla.androidapp.persistence.ExerciseRatingRepository;
import org.foomla.androidapp.utils.ExerciseCommentHandler;
import org.foomla.androidapp.widgets.CommentDialog;
import org.foomla.androidapp.widgets.PleaseLoginDialog;
import org.foomla.api.client.FoomlaClient;
import org.foomla.api.entities.twizard.Exercise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class RateExerciseHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateExerciseHandler.class);

    private final FoomlaClient foomlaClient;
    private final Context context;

    public RateExerciseHandler(final Context context, final FoomlaClient foomlaClient) {
        this.context = context;
        this.foomlaClient = foomlaClient;
    }

    public void handle(final Exercise exercise, final float rating) {
        if (isLoggedIn()) {
            showCommentDialog(exercise, rating);
        } else {
            showLoginInfoDialog();
        }
    }

    private boolean isLoggedIn() {

        // TODO evaluate if the user is logged in
        return false;
    }

    private void showCommentDialog(final Exercise exercise, final float rating) {
        LOGGER.info("User is already logged in");

        new CommentDialog(context).build(new CommentDialog.SaveListener() {
                @Override
                public void save(final Comment comment) {
                    new ExerciseCommentHandler(context, foomlaClient).handle(exercise, comment, rating);

                    new AsyncTask<ExerciseRatingResult, Void, Void>() {
                        @Override
                        protected Void doInBackground(final ExerciseRatingResult... params) {
                            ExerciseRatingRepository repo = ExerciseRatingRepository.getInstance(context);
                            repo.save(params[0]);

                            return null;
                        }
                    }.execute(new ExerciseRatingResult(-1, exercise.getId(), (int) rating));
                }
            });
    }

    private void showLoginInfoDialog() {
        LOGGER.info("User is not logged in");

        new PleaseLoginDialog(context, new PleaseLoginDialog.Listener() {
                @Override
                public void login() {

                    // TODO start with result
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
            }).build();
    }
}
