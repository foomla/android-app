package org.foomla.androidapp.view;

import org.foomla.androidapp.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class ExerciseRatingView extends LinearLayout {

    public ExerciseRatingView(final Context context) {
        super(context);
        initLayout();
        requestLayout();
    }

    public ExerciseRatingView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        initLayout();

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ExerciseRatingView, 0, 0);
        applyAttributes(a);

        requestLayout();
    }

    private void initLayout() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.exerciseratingview, this, true);
    }

    private void applyAttributes(final TypedArray a) {
        String username;
        String comment;
        int rating;

        try {
            username = a.getString(R.styleable.ExerciseRatingView_username);
            comment = a.getString(R.styleable.ExerciseRatingView_comment);
            rating = a.getInteger(R.styleable.ExerciseRatingView_rating, 0);

            setUsername(username);
            setRating(rating);
            setComment(comment);
        } finally {
            a.recycle();
        }
    }

    public void setUsername(final String username) {
        View viewById = getRootView().findViewById(R.id.username);
        if (viewById instanceof TextView) {
            ((TextView) viewById).setText(username);
        }
    }

    public void setRating(final int rating) {
        View viewById = getRootView().findViewById(R.id.rating);
        if (viewById instanceof RatingBar) {
            ((RatingBar) viewById).setRating(rating);
        }
    }

    public void setComment(final String comment) {
        View viewById = getRootView().findViewById(R.id.comment);
        if (viewById instanceof TextView) {
            ((TextView) viewById).setText(comment);
        }
    }
}
