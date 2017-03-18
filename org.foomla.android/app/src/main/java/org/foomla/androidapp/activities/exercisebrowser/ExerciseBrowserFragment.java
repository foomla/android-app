package org.foomla.androidapp.activities.exercisebrowser;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.collect.Lists;

import org.foomla.androidapp.FoomlaApplication;
import org.foomla.androidapp.R;
import org.foomla.androidapp.domain.AgeClass;
import org.foomla.androidapp.domain.Exercise;
import org.foomla.androidapp.domain.TrainingFocus;
import org.foomla.androidapp.utils.EnumTextUtil;

import java.util.List;


public class ExerciseBrowserFragment extends Fragment implements ExerciseListAdapter.ItemListener {

    private LinearLayout filterLayout;
    private TextView noExercisesTextView;
    private LinearLayout filterContainer;
    private ImageButton clearFilterButton;

    public interface FragmentCallback {

        ExerciseFilter getExerciseFilter();

        List<Exercise> getExercises();

        boolean isShowSelectButton();

        void onSelectExercise(Exercise exercise);

        void onShowExerciseDetails(Exercise exercise);

        void onClearFilter();

        void onGoPro();
    }

    private ExerciseListAdapter exerciseListAdapter;

    private FragmentCallback fragmentCallback;
    private GridView gridView;

    public void notifyDataChanged() {
        ExerciseFilter exerciseFilter = fragmentCallback.getExerciseFilter();
        if(exerciseFilter != null && exerciseFilter.shouldFilter()) {
            showCurrentFilter(exerciseFilter);
            filterContainer.setVisibility(View.VISIBLE);
        } else {
            filterContainer.setVisibility(View.GONE);
        }

        List<Exercise> exercises = fragmentCallback.getExercises();
        if (exercises.isEmpty()) {
            noExercisesTextView.setVisibility(View.VISIBLE);
        } else {
            noExercisesTextView.setVisibility(View.GONE);
        }

        exerciseListAdapter.setExercises(exercises);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceBundle) {
        super.onActivityCreated(savedInstanceBundle);
        Activity activity = getActivity();
        if (!(activity instanceof FragmentCallback)) {
            throw new IllegalArgumentException("Parent Activity must implement FragmentCallback!");
        }

        fragmentCallback = (FragmentCallback) activity;
        exerciseListAdapter.setShowSelectButton(fragmentCallback.isShowSelectButton());
        setInfoTextVisibility(getView());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Exercise exercise = (Exercise) exerciseListAdapter.getItem(item.getItemId());
        if (exercise != null) {
            if (item.getTitle().equals(getActivity().getString(R.string.choose))) {
                onSelectItem(exercise);
            } else {
                onDisplayExerciseDetails(exercise);
            }
        }
        return true;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        exerciseListAdapter = new ExerciseListAdapter(getActivity(), this, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_grid, container, false);
        if(getFoomlaApplication().isProVersion()) {
            view.findViewById(R.id.more_exercises).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.goProButton).setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentCallback.onGoPro();
                }
            });
        }
        this.gridView = (GridView) view.findViewById(R.id.exercise_grid);
        gridView.setAdapter(exerciseListAdapter);

        filterLayout = (LinearLayout) view.findViewById(R.id.filter_layout);
        noExercisesTextView = (TextView) view.findViewById(R.id.no_exercises_text);
        clearFilterButton = (ImageButton) view.findViewById(R.id.clearFilterButton);
        clearFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentCallback != null) {
                    fragmentCallback.onClearFilter();
                }
            }
        });
        filterContainer = (LinearLayout) view.findViewById(R.id.filterContainer);

        return view;
    }

    private void showCurrentFilter(ExerciseFilter exerciseFilter) {
        filterLayout.removeAllViews();
        if (exerciseFilter != null) {
            List<View> views = Lists.newArrayList();
            List<AgeClass> ageClasses = exerciseFilter.getAgeClasses();
            for (AgeClass ageClass : ageClasses) {
                TextView textView = new TextView(this.getActivity());
                textView.setText(EnumTextUtil.getText(getActivity(), ageClass));
                textView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape));
                textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(5, 5, 5, 5);
                textView.setLayoutParams(params);
                views.add(textView);
            }
            List<TrainingFocus> focuses = exerciseFilter.getFocuses();
            for (TrainingFocus focus : focuses) {
                TextView textView = new TextView(this.getActivity());
                textView.setText(EnumTextUtil.getText(getActivity(), focus));
                textView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape2));
                textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(5, 5, 5, 5);
                textView.setLayoutParams(params);
                views.add(textView);
            }
            populateViews(this.filterLayout, views, getActivity(), clearFilterButton);

        }
    }


    private void populateViews(LinearLayout linearLayout, List<View> views, Context context, View extraView) {
        extraView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        linearLayout.removeAllViews();
        int maxWidth = display.getWidth() - extraView.getMeasuredWidth() - 25;

        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params;
        LinearLayout newLL = new LinearLayout(context);
        newLL.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        newLL.setGravity(Gravity.LEFT);
        newLL.setOrientation(LinearLayout.HORIZONTAL);

        int widthSoFar = 0;

        for (int i = 0; i < views.size(); i++) {
            LinearLayout LL = new LinearLayout(context);
            LL.setOrientation(LinearLayout.HORIZONTAL);
            LL.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            LL.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            views.get(i).measure(0, 0);
            params = new LinearLayout.LayoutParams(views.get(i).getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);

            LL.addView(views.get(i), params);
            LL.measure(0, 0);
            widthSoFar += views.get(i).getMeasuredWidth();
            if (widthSoFar >= maxWidth) {
                linearLayout.addView(newLL);

                newLL = new LinearLayout(context);
                newLL.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                newLL.setOrientation(LinearLayout.HORIZONTAL);
                newLL.setGravity(Gravity.LEFT);
                params = new LinearLayout.LayoutParams(LL.getMeasuredWidth(), LL.getMeasuredHeight());
                newLL.addView(LL, params);
                widthSoFar = LL.getMeasuredWidth();
            } else {
                newLL.addView(LL);
            }
        }
        linearLayout.addView(newLL);
    }

    @Override
    public void onDisplayExerciseDetails(Exercise exercise) {
        fragmentCallback.onShowExerciseDetails(exercise);
    }

    @Override
    public void onSelectItem(final Exercise exercise) {
        fragmentCallback.onSelectExercise(exercise);
    }

    private void setInfoTextVisibility(View view) {
        View infoText = view.findViewById(R.id.info_text);
        if (((FragmentCallback) getActivity()).isShowSelectButton()) {
            infoText.setVisibility(View.VISIBLE);
            this.gridView.setPadding(this.gridView.getPaddingLeft(), 0, this.gridView.getPaddingRight(),
                    this.gridView.getPaddingRight());
        } else {
            infoText.setVisibility(View.GONE);
        }
    }

    private FoomlaApplication getFoomlaApplication() {
        return (FoomlaApplication) this.getActivity().getApplication();
    }

}
