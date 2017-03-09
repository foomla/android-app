package org.foomla.androidapp.activities.exercisebrowser;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.foomla.androidapp.R;
import org.foomla.androidapp.domain.AgeClass;
import org.foomla.androidapp.domain.TrainingFocus;
import org.foomla.androidapp.utils.EnumTextUtil;

public class FilterDialogFragment extends DialogFragment {

    public interface FilterDialogListener {
        void onSaveFilter(ExerciseFilter exerciseFilter);
    }

    private FilterDialogListener listener;

    private ExerciseFilter exerciseFilter;

    public FilterDialogFragment() {
        this.exerciseFilter = new ExerciseFilter();
    }

    public void setExerciseFilter(ExerciseFilter exerciseFilter) {
        if(exerciseFilter != null) {
            this.exerciseFilter.getFocuses().clear();
            this.exerciseFilter.getFocuses().addAll(exerciseFilter.getFocuses());
            this.exerciseFilter.getAgeClasses().clear();
            this.exerciseFilter.getAgeClasses().addAll(exerciseFilter.getAgeClasses());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        // return inflater.inflate(R.layout.activity_exercisefilter, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = this.getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_exercisefilter, null);

        LinearLayout linearLayout = (LinearLayout) dialogView.findViewById(R.id.ageClassesSelection);

        AgeClass[] values = AgeClass.values();
        for (final AgeClass ageClass: values) {
            CheckBox checkBox = new CheckBox(this.getActivity());
            checkBox.setText(EnumTextUtil.getText(getActivity(), ageClass));
            checkBox.setChecked(exerciseFilter.getAgeClasses().contains(ageClass));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        exerciseFilter.getAgeClasses().add(ageClass);
                    } else {
                        exerciseFilter.getAgeClasses().remove(ageClass);
                    }
                }
            });

            linearLayout.addView(checkBox);
        }

        linearLayout = (LinearLayout) dialogView.findViewById(R.id.focusesSelection);

        TrainingFocus[] focusValues = TrainingFocus.values();
        for (final TrainingFocus focus: focusValues) {
            CheckBox checkBox = new CheckBox(this.getActivity());
            checkBox.setText(EnumTextUtil.getText(getActivity(), focus));
            checkBox.setChecked(exerciseFilter.getFocuses().contains(focus));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        exerciseFilter.getFocuses().add(focus);
                    } else {
                        exerciseFilter.getFocuses().remove(focus);
                    }
                }
            });

            linearLayout.addView(checkBox);
        }

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Übungsfilter");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(exerciseFilter.getAgeClasses().size() == AgeClass.values().length) {
                    exerciseFilter.getAgeClasses().clear();
                }
                if(exerciseFilter.getFocuses().size() == TrainingFocus.values().length) {
                    exerciseFilter.getFocuses().clear();
                }

                listener.onSaveFilter(exerciseFilter);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // nothing to do
            }
        });
        return dialogBuilder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (FilterDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement " + FilterDialogListener.class.getName());
        }
    }
}