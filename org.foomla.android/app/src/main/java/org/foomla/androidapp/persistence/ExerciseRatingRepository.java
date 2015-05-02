package org.foomla.androidapp.persistence;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import org.foomla.androidapp.data.ExerciseRatingResult;
import org.foomla.androidapp.data.ExerciseRatingResultBuilder;

import android.content.ContentValues;
import android.content.Context;

public class ExerciseRatingRepository implements Repository<ExerciseRatingResult> {

    private static ExerciseRatingRepository INSTANCE;

    private Context context;
    private SQLiteRepository sqLiteRepository;

    private ExerciseRatingRepository(final Context context) {
        this.context = context;
        this.sqLiteRepository = SQLiteRepository.getInstance(context);
    }

    public static ExerciseRatingRepository getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ExerciseRatingRepository(context);
        }

        return INSTANCE;
    }

    @Override
    public List<ExerciseRatingResult> getAll() {
        ExerciseRatingResultBuilder builder = new ExerciseRatingResultBuilder();
        sqLiteRepository.select(builder, SQLiteRepository.QUERY_RATINGS, null);

        return builder.build();
    }

    @Override
    public ExerciseRatingResult getById(final int id) {
        ExerciseRatingResultBuilder builder = new ExerciseRatingResultBuilder();
        sqLiteRepository.select(builder, SQLiteRepository.QUERY_RATING_BY_EXERCISE, String.valueOf(id));

        List<ExerciseRatingResult> results = builder.build();
        return results != null && !results.isEmpty() ? results.get(0) : null;
    }

    @Override
    public ExerciseRatingResult save(final ExerciseRatingResult entity) {
        ContentValues values = new ContentValues();
        values.put("exercise_id", entity.getExerciseId());
        values.put("value", entity.getValue());

        long result = sqLiteRepository.insert(SQLiteRepository.TBL_RATING, values);
        if (result > 0) {
            return new ExerciseRatingResult((int) result, entity.getExerciseId(), entity.getValue());
        }

        return null;
    }

    @Override
    public void delete(final int id) {
        throw new NotImplementedException();
    }
}
