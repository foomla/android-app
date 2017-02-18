package org.foomla.androidapp.activities.exercisebrowser;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.foomla.androidapp.R;
import org.foomla.androidapp.domain.Exercise;

import java.util.List;


public class ExerciseBrowserFragment extends Fragment implements ExerciseListAdapter.ItemListener {

    public interface FragmentCallback {
        List<Exercise> getExercises();

        boolean isShowSelectButton();

        void onSelectExercise(Exercise exercise);

        void onShowExerciseDetails(Exercise exercise);
    }

    private static final String LOGTAG = "[foomla] ExerciseBrowserFragment";

    private ExerciseListAdapter exerciseListAdapter;

    private FragmentCallback fragmentCallback;
    private GridView gridView;

    public void notifyDataChanged() {
        exerciseListAdapter.setExercises(fragmentCallback.getExercises());
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
        this.gridView = (GridView) view.findViewById(R.id.exercise_grid);
        gridView.setAdapter(exerciseListAdapter);
        return view;
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
        if (((FragmentCallback)getActivity()).isShowSelectButton()) {
            infoText.setVisibility(View.VISIBLE);
            this.gridView.setPadding(this.gridView.getPaddingLeft(), 0, this.gridView.getPaddingRight(),
                    this.gridView.getPaddingRight());
        }
        else {
            infoText.setVisibility(View.GONE);
        }
    }

}
