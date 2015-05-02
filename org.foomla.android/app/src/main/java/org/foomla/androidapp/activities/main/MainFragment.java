package org.foomla.androidapp.activities.main;

import org.foomla.androidapp.R;
import org.foomla.androidapp.async.LoadExerciseImageTask;
import org.foomla.androidapp.utils.ImageUtil.ImageType;
import org.foomla.androidapp.utils.UiUtils;
import org.foomla.api.entities.twizard.Exercise;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;

public class MainFragment extends Fragment {

    public interface ActionHandler {
        void onLoadLatestExercise();

        void onLoadNewsFeed();

        void onShowAccountActivity();

        void onOpenWebsite();

        void onShowEditTrainingActivity();

        void onShowExerciseDetailActivity(Exercise exercise);

        void onShowMyTrainingsActivity();

        void onShowNewsDetailActivity(SyndEntry feedEntry);
    }

    private static final String ADVERTISE_KEY_WORDS = "sporting goods";

    private View view;

    @Override
    public View getView() {
        if (this.view != null) {
            return this.view;
        } else {
            return super.getView();
        }
    }

    public void hideProgressBar(final int progessBarId, final int containerId) {
        View progressBar = getProgressBar(progessBarId);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        View container = getContainer(containerId);
        if (container != null) {
            container.setVisibility(View.VISIBLE);
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
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.main, menu);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdView(view);
        registerButtonClickerListener(view);
        getActionHandler().onLoadLatestExercise();
        getActionHandler().onLoadNewsFeed();
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

    public void showLatestNewsEntry(final SyndEntry feedEntry) {
        View view = getView();

        // view sometimes is null after screen rotation (why?)
        if (view == null) {
            return;
        }

        view.findViewById(R.id.latestNewsEntryContainerWrapper).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View view) {
                getActionHandler().onShowNewsDetailActivity(feedEntry);
            }
        });

        UiUtils.setSafeText(view, R.id.latestNewsEntryTitle, feedEntry.getTitle());
        UiUtils.setSafeText(view, R.id.latestNewsEntrySummary, feedEntry.getDescription().getValue());
    }

    public void showProgressBar(final int progessBarId, final int containerId) {
        View progressBar = getProgressBar(progessBarId);
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        View container = getContainer(containerId);
        if (container != null) {
            container.setVisibility(View.GONE);
        }
    }

    protected View getProgressBar(final int progessBarId) {
        return getView() != null ? getView().findViewById(progessBarId) : null;
    }

    private ActionHandler getActionHandler() {
        return (ActionHandler) getActivity();
    }

    private View getContainer(final int containerId) {
        return getView() != null ? getView().findViewById(containerId) : null;
    }

    private void initAdView(final View view) {
        /*
        AdView adView = (AdView) view.findViewById(R.id.ad);
        AdRequest adRequest = new AdRequest();
        adRequest.addKeyword(ADVERTISE_KEY_WORDS);
        adRequest.addTestDevice("A6CF3D5EB79673020CAEBE2861E66E7E");
        adView.loadAd(adRequest);
        */
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
}
