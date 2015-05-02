package org.foomla.androidapp.activities.exercisedetail;

import java.util.List;

import org.foomla.androidapp.R;
import org.foomla.androidapp.async.LoadExerciseImageTask;
import org.foomla.androidapp.data.ExerciseRatingResult;
import org.foomla.androidapp.utils.EnumTextUtil;
import org.foomla.androidapp.utils.ExtendedTagHandler;
import org.foomla.androidapp.utils.ImageUtil;
import org.foomla.androidapp.utils.ImageUtil.ImageType;
import org.foomla.androidapp.view.ExerciseRatingView;

import org.foomla.api.entities.twizard.AgeClass;
import org.foomla.api.entities.twizard.Exercise;
import org.foomla.api.entities.twizard.ExerciseRating;
import org.foomla.api.entities.twizard.Training;
import org.foomla.api.entities.twizard.TrainingFocus;
import org.foomla.api.entities.twizard.TrainingPhase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import android.app.Activity;

import android.app.Fragment;
import android.graphics.drawable.Drawable;

import android.os.Bundle;

import android.text.Html;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class ExerciseDetailFragment extends Fragment {

    public interface ActionHandler {

        Exercise getExercise();

        Training getTraining();

        void onRatingChanged(float rating);

        void onShowHelp();

    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ExerciseDetailFragment.class);

    private ActionHandler actionHandler;

    private View view;

    public ActionHandler getActionHandler() {
        return actionHandler;
    }

    @Override
    public View getView() {
        if (this.view != null) {
            return this.view;
        } else {
            return super.getView();
        }
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Activity activity = getActivity();
        if (activity instanceof ActionHandler) {
            setActionHandler((ActionHandler) activity);
        } else {
            throw new IllegalArgumentException("Parent Activity must implement FragmentCallback interface");
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.exercisedetail, menu);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_exercisedetail, container, false);
        setupRatingBar(view);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.show_help :
                getActionHandler().onShowHelp();
                return true;
        }

        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        setExercise(getActionHandler().getExercise());
    }

    public void setActionHandler(final ActionHandler actionHandler) {
        this.actionHandler = actionHandler;
    }

    public void setExercise(final Exercise exercise) {
        if (exercise == null) {
            LOGGER.warn("Called setExercise but parameter 'exercise' is null!");
            return;
        }

        setImageView(exercise);
        setTitleText(exercise);
        setPhaseText(exercise);
        setFocusText(exercise);
        setAgeclassesText(exercise);
        setSettingsText(exercise);
        setScheduleText(exercise);
        setObjectiveText(exercise);
        setAuxiliaryText(exercise);
        setNoteText(exercise);
    }

    public void setRating(final ExerciseRatingResult rating) {
        View v = getView().findViewById(R.id.rating);
        if (v instanceof RatingBar) {
            ((RatingBar) v).setRating(rating.getValue());
        }

        RatingBar ratingBar = (RatingBar) v;
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                @Override
                public void onRatingChanged(final RatingBar ratingBar, final float rating, final boolean fromUser) {
                    if (fromUser) {
                        getActionHandler().onRatingChanged(rating);
                    }
                }
            });
    }

    public void setUserComments(final List<ExerciseRating> userComments) {
        if (userComments == null) {
            LOGGER.warn("Called setUserComments but parameter 'userComments' is null!");
            return;
        }

        LinearLayout commentsWrapper = getLinearLayout(R.id.usercomments);
        for (ExerciseRating rating : userComments) {
            ExerciseRatingView ratingView = new ExerciseRatingView(getActivity());
            ratingView.setUsername(rating.getUser().getEmail());
            ratingView.setComment(rating.getUserComment());
            ratingView.setRating(rating.getValue());
            commentsWrapper.addView(ratingView);
        }
    }

    private ImageView getImageView(final int resId) {
        View view = getView();
        if (view != null) {
            return (ImageView) view.findViewById(resId);
        }

        return null;
    }

    private LinearLayout getLinearLayout(final int resId) {
        View view = getView();
        if (view != null) {
            return (LinearLayout) view.findViewById(resId);
        }

        return null;
    }

    private TextView getTextView(final int resId) {
        View view = getView();
        if (view != null) {
            return (TextView) view.findViewById(resId);
        }

        return null;
    }

    private void setAgeclassesText(final Exercise exercise) {
        if (exercise.getAgeClasses() == null || exercise.getAgeClasses().isEmpty()) {
            LOGGER.warn("setAgeclassesText() not successful - did not find any age classes");
            return;
        }

        List<String> ageClassTexts = Lists.newArrayList();
        for (AgeClass ageClass : exercise.getAgeClasses()) {
            ageClassTexts.add(EnumTextUtil.getText(getActivity(), ageClass));
        }

        String ageClasses = Joiner.on(", ").join(ageClassTexts);
        setTextViewText(getTextView(R.id.ageclasses), ageClasses);
    }

    private void setAuxiliaryText(final Exercise exercise) {
        setTextViewText(getTextView(R.id.auxiliary), exercise.getAuxiliaryMaterial());
    }

    private void setFocusText(final Exercise exercise) {
        TrainingFocus trainingFocus = exercise.getTrainingFocus();
        if (trainingFocus != null) {
            setTextViewText(getTextView(R.id.focus), EnumTextUtil.getText(getActivity(), trainingFocus));
        }
    }

    private void setHtmlText(final TextView textView, String htmlText) {
        if (htmlText != null && textView != null) {
            htmlText = htmlText.replaceAll("<h1>", "<h6 style=\"margin-bottom: 5px; padding-bottom: 5px;\">");
            htmlText = htmlText.replaceAll("</h1>", "</h6>");
            textView.setText(Html.fromHtml(htmlText, null, new ExtendedTagHandler()));
        }
    }

    private void setImageView(final Exercise exercise) {
        final ImageView imageView = getImageView(R.id.image);

        if (imageView == null) {
            LOGGER.warn("setImageView not successful - did not find ImageView in getView()");
            return;
        }

        new LoadExerciseImageTask(getActivity(), getActionHandler().getTraining(), ImageType.NORMAL) {
            @Override
            protected void onCancelled() {
                getProgessBar().setVisibility(View.GONE);
            }

            @Override
            protected void onPostExecute(final Drawable drawable) {
                getProgessBar().setVisibility(View.GONE);
                if (drawable != null) {
                    imageView.setImageDrawable(drawable);
                    ImageUtil.calculateImageViewBounds(imageView, drawable);
                }
            }

            private View getProgessBar() {
                return getView().findViewById(R.id.image_progress_bar);
            }
        }.execute(exercise);
    }

    private void setNoteText(final Exercise exercise) {
        setHtmlText(getTextView(R.id.note), exercise.getNote());
    }

    private void setObjectiveText(final Exercise exercise) {
        setHtmlText(getTextView(R.id.objective), exercise.getObjective());
    }

    private void setPhaseText(final Exercise exercise) {
        if (exercise.getTrainingPhases() == null || exercise.getTrainingPhases().isEmpty()) {
            LOGGER.warn("setPhaseText not successful - did not find any phase");
            return;
        }

        List<String> phasesTexts = Lists.newArrayList();
        for (TrainingPhase trainingPhase : exercise.getTrainingPhases()) {
            phasesTexts.add(EnumTextUtil.getText(getActivity(), trainingPhase));
        }

        String phases = Joiner.on(", ").join(phasesTexts);
        setTextViewText(getTextView(R.id.phase), phases);
    }

    private void setScheduleText(final Exercise exercise) {
        setHtmlText(getTextView(R.id.schedule), exercise.getSchedule());
    }

    private void setSettingsText(final Exercise exercise) {
        setTextViewText(getTextView(R.id.settings), exercise.getSetting());
    }

    private void setTextViewText(final TextView textView, final String text) {
        if (textView != null) {
            textView.setText(text);
        } else {
            LOGGER.warn("setTextViewText not successful - did not find TextView in getView()");
        }
    }

    private void setTitleText(final Exercise exercise) {
        setTextViewText(getTextView(R.id.title), exercise.getTitle());
    }

    private void setupRatingBar(final View view) {
        View bar = view.findViewById(R.id.rating);
        if (bar == null || !(bar instanceof RatingBar)) {
            LOGGER.warn("Unable to setup rating bar");
            return;
        }

        RatingBar ratingBar = (RatingBar) bar;
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                @Override
                public void onRatingChanged(final RatingBar ratingBar, final float rating, final boolean fromUser) {
                    if (fromUser) {
                        getActionHandler().onRatingChanged(rating);
                    }
                }
            });
    }
}
