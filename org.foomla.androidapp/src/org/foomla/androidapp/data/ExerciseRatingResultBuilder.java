package org.foomla.androidapp.data;

import android.database.Cursor;

public class ExerciseRatingResultBuilder extends AbstractEntityBuilder<ExerciseRatingResult> {

    @Override
    public void addRow(Cursor cursor) {
        int id = cursor.getInt(0);
        int exerciseId = cursor.getInt(1);
        int value = cursor.getInt(2);

        add(new ExerciseRatingResult(id, exerciseId, value));
    }
}
