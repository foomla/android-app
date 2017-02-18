package org.foomla.androidapp.persistence;

import android.content.Context;

import org.foomla.androidapp.domain.Exercise;
import org.foomla.androidapp.exception.FoomlaException;
import org.foomla.androidapp.utils.FileUtils;
import org.foomla.api.client.ClientEntityRestMapping;
import org.foomla.api.converters.CustomJacksonConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExerciseFileRepository implements Repository<Exercise> {

    private static ExerciseFileRepository instance;

    private final Context context;

    private ExerciseFileRepository(final Context context) {
        this.context = context;
    }

    public static ExerciseFileRepository getInstance(final Context context) {
        if (instance == null) {
            instance = new ExerciseFileRepository(context);
        }

        return instance;
    }

    @Override
    public List<Exercise> getAll() {
        try {
            File trainingsDir = FileUtils.getTrainingDirectory(context);
            List<String> fileNames = FileUtils.getOrderedFileNames(trainingsDir);
            List<Exercise> exercises = new ArrayList<Exercise>(fileNames.size());

            for (String fileName : fileNames) {
                try {
                    exercises.add(convert(new File(trainingsDir, fileName)));
                } catch (IOException ioe) {
                    // warning
                }
            }

            return exercises;

        } catch (IOException e) {
            throw new FoomlaException("Unable to access training directory", e);
        }
    }

    @Override
    public Exercise getById(final int id) {
        try {
            File trainingsDir = FileUtils.getTrainingDirectory(context);
            File exerciseFile = new File(trainingsDir, FileUtils.createFilename(id));

            if (!exerciseFile.exists() || !exerciseFile.canRead()) {
                return null;
            }

            return convert(exerciseFile);
        } catch (IOException e) {
            throw new FoomlaException("Unable to parse Exercise from disk", e);
        } catch (Exception e) {
            throw new FoomlaException("Unable to read Exercise", e);
        }
    }

    @Override
    public Exercise save(final Exercise exercise) {
        try {
            File trainingsDir = FileUtils.getTrainingDirectory(context);
            int newId = FileUtils.getNextId(trainingsDir);

            exercise.setId(newId);

            File exerciseDst = new File(trainingsDir, FileUtils.createFilename(newId));
            CustomJacksonConverter converter = new CustomJacksonConverter(new ClientEntityRestMapping());
            converter.getObjectMapper().writeValue(exerciseDst, exercise);

            return exercise;
        } catch (IOException e) {
            throw new FoomlaException("Unable to write Exercise to disk", e);
        } catch (Exception e) {
            throw new FoomlaException("Unable to save Exercise", e);
        }
    }

    @Override
    public void delete(final int id) {
        try {
            File trainingDir = FileUtils.getTrainingDirectory(context);
            File exerciseFile = new File(trainingDir, FileUtils.createFilename(id));

            Exercise exercise = getById(id);
            if (exercise != null) {
                exerciseFile.delete();
            }

        } catch (IOException e) {
            throw new FoomlaException("Unable to delete Exercise " + id, e);
        }
    }

    private Exercise convert(final File file) throws IOException {
        CustomJacksonConverter converter = new CustomJacksonConverter(new ClientEntityRestMapping());
        return converter.getObjectMapper().readValue(new FileInputStream(file), ExerciseImpl.class);
    }
}
