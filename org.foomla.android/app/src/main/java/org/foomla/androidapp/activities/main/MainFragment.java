package org.foomla.androidapp.activities.main;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.foomla.androidapp.FoomlaApplication;
import org.foomla.androidapp.R;
import org.foomla.androidapp.async.LoadExerciseImageTask;
import org.foomla.androidapp.domain.Exercise;
import org.foomla.androidapp.utils.ImageUtil.ImageType;

public class MainFragment extends Fragment {

    public interface ActionHandler {
        void onLoadLatestExercise();

        void onOpenWebsite();

        void onShowEditTrainingActivity();

        void onShowExerciseDetailActivity(Exercise exercise);

        void onShowMyTrainingsActivity();

        void onGoPro();
    }

    private View view;

    @Override
    public View getView() {
        if (this.view != null) {
            return this.view;
        } else {
            return super.getView();
        }
    }


    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        if(getFoomlaApplication().isProVersion()) {
            view.findViewById(R.id.go_pro_layout).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.goProButton).setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActionHandler().onGoPro();
                }
            });
        }
        return view;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerButtonClickerListener(view);
        getActionHandler().onLoadLatestExercise();
    }

    public void showLatestExercise(final Exercise exercise) {
        View view = getView();

        // view sometimes is null after screen rotation (why?)
        if (view == null) {
            return;
        }

        TextView textView = (TextView) view.findViewById(R.id.latestExerciseTitle);
        if (textView != null) {
            textView.setText(exercise.getTitle());
        }

        final ImageView imageView = (ImageView) view.findViewById(R.id.latestExerciseImage);
        view.findViewById(R.id.latestExerciseContainerWrapper).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View view) {
                getActionHandler().onShowExerciseDetailActivity(exercise);
            }
        });
        new LoadExerciseImageTask(getActivity(), null, ImageType.NORMAL) {
            @Override
            protected void onCancelled() {
                getProgressBar(R.id.latestExerciseProgressBar).setVisibility(View.GONE);
            }

            @Override
            protected void onPostExecute(final Drawable drawable) {
                getProgressBar(R.id.latestExerciseProgressBar).setVisibility(View.GONE);
                if (drawable != null) {
                    imageView.setImageDrawable(drawable);
                }
            }
        }.execute(exercise);
    }

    protected View getProgressBar(final int progessBarId) {
        return getView() != null ? getView().findViewById(progessBarId) : null;
    }

    private ActionHandler getActionHandler() {
        return (ActionHandler) getActivity();
    }

    private void registerButtonClickerListener(final View view) {
        View newTrainingButton = view.findViewById(R.id.newTrainingButton);
        newTrainingButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                getActionHandler().onShowEditTrainingActivity();
            }
        });

        View exercisesButton = view.findViewById(R.id.trainingsButton);
        exercisesButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                getActionHandler().onShowMyTrainingsActivity();
            }
        });

        View openWebsiteButton = view.findViewById(R.id.openWebsiteButton);
        openWebsiteButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                getActionHandler().onOpenWebsite();
            }
        });
    }


    private FoomlaApplication getFoomlaApplication() {
        return (FoomlaApplication) this.getActivity().getApplication();
    }

}
