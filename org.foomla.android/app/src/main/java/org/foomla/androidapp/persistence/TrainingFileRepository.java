package org.foomla.androidapp.persistence;

import android.content.Context;

import org.foomla.androidapp.data.UserMode;
import org.foomla.androidapp.domain.Exercise;
import org.foomla.androidapp.domain.Training;
import org.foomla.androidapp.exception.FoomlaException;
import org.foomla.androidapp.utils.FileUtils;
import org.foomla.androidapp.utils.ImageUtil;
import org.foomla.androidapp.utils.ImageUtil.ImageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TrainingFileRepository extends AbstractFileRepository<Training> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingFileRepository.class);

    /**
     * A repository where trainings are stored on the device for an unauthorized user.
     */
    private static TrainingFileRepository UNAUTHORIZED_INSTANCE;

    /**
     * A repository where trainings are stored on the device for an authorized user.
     */
    private static TrainingFileRepository AUTHORIZED_INSTANCE;

    protected static TrainingFileRepository getInstance(final Context context, final UserMode mode) {
        try {
            switch (mode) {

                case AUTHORIZED :
                    return getInstanceForAuthorizedUser(context);

                case UNAUTHORIZED :
                    return getInstanceForUnauthorizedUser(context);

                default :
                    throw new IllegalArgumentException("Unknown UserMode: " + mode);
            }
        } catch (IOException e) {
            LOGGER.error("Unable to create instance for UserMode: " + mode);
            throw new FoomlaException("Unable to create instance for UserMode: " + mode, e);
        }
    }

    private static TrainingFileRepository getInstanceForUnauthorizedUser(final Context context) throws IOException {
        if (UNAUTHORIZED_INSTANCE == null) {
            UNAUTHORIZED_INSTANCE = new TrainingFileRepository(context, FileUtils.getTrainingDirectory(context));
        }

        return UNAUTHORIZED_INSTANCE;
    }

    private static TrainingFileRepository getInstanceForAuthorizedUser(final Context context) throws IOException {
        if (AUTHORIZED_INSTANCE == null) {
            AUTHORIZED_INSTANCE = new TrainingFileRepository(context, FileUtils.getOnlineTrainingDirectory(context));
        }

        return AUTHORIZED_INSTANCE;
    }

    private final Context context;
    private final File trainingDir;

    private TrainingFileRepository(final Context context, final File trainingDir) {
        this.context = context;
        this.trainingDir = trainingDir;
    }

    @Override
    public void delete(final int id) {
        File trainingFile = new File(trainingDir, FileUtils.createFilename(id));

        Training training = getById(id);
        if (training != null) {
            trainingFile.delete();
        }

        try {
            deleteImages(id);
        } catch (FoomlaException e) {
            LOGGER.error("Deletion of image folder for training " + id + " failed.", e);
        }
    }

    @Override
    public List<Training> getAll() {
        List<String> fileNames = FileUtils.getOrderedFileNames(trainingDir);

        if (fileNames == null || fileNames.isEmpty()) {
            return new ArrayList<Training>(0);
        }

        List<Training> trainings = new ArrayList<Training>(fileNames.size());

        for (String fileName : fileNames) {
            try {
                Object obj = convert(new File(trainingDir, fileName), Training.class);
                if (obj instanceof Training) {
                    trainings.add((Training) obj);
                }
            } catch (IOException ioe) {
                LOGGER.error("Unable to convert training: " + fileName, ioe);
            }
        }

        return trainings;
    }

    @Override
    public Training getById(final int id) {
        try {
            File trainingFile = new File(trainingDir, FileUtils.createFilename(id));

            if (!trainingFile.exists() || !trainingFile.canRead()) {
                return null;
            }

            return (Training) convert(trainingFile, Training.class);
        } catch (IOException e) {
            throw new FoomlaException("Unable to parse Training from disk", e);
        } catch (Exception e) {
            throw new FoomlaException("Unable to read Training", e);
        }
    }

    @Override
    public Training save(final Training entity) {
        try {
            Integer entityId = entity.getId();

            int id = entityId != null && entityId > 0 ? entityId : FileUtils.getNextId(trainingDir);

            entity.setId(id);

            File trainingDst = new File(trainingDir, FileUtils.createFilename(id));
            write(trainingDst, entity);

            try {
                saveImages(entity);
            } catch (FoomlaException e) {
                LOGGER.error("Error while saving training images on local disk", e);
            }

            return entity;
        } catch (IOException e) {
            throw new FoomlaException("Unable to write Training to disk", e);
        } catch (Exception e) {
            throw new FoomlaException("Unable to save Training", e);
        }
    }

    private void deleteImages(final int id) throws FoomlaException {
        try {
            File imageDirectory = FileUtils.getImageDirectory(context, id);
            org.apache.commons.io.FileUtils.deleteDirectory(imageDirectory);
        } catch (IOException e) {
            throw new FoomlaException("Unable to delete images of training " + id, e);
        }
    }

    private void saveImage(final File imageDir, final Training training, final Exercise exercise, final int fileId)
        throws IOException {
        String url = ImageUtil.getImageUrl(context, training, exercise);
        File image = new File(imageDir, ImageType.NORMAL.getImageFileName(fileId));

        LOGGER.info("Download exercise image: " + url);
        org.apache.commons.io.FileUtils.copyURLToFile(new URL(url), image);

    }

    private void saveImages(final Training training) {
        try {
            List<Exercise> exercises = training.getExercises();
            File imageDir = FileUtils.getImageDirectory(context, training.getId());

            for (Exercise exercise : exercises) {
                try {
                    saveImage(imageDir, training, exercise, exercise.getId());
                    saveThumb(imageDir, training, exercise, exercise.getId());
                } catch (Exception e) {
                    LOGGER.warn("Unable to save image for training " + training.getId() + " | exercise "
                            + exercise.getId(), e);
                }
            }
        } catch (Exception e) {
            throw new FoomlaException("Unable to save training images for Training " + training.getId(), e);
        }
    }

    private void saveThumb(final File imageDir, final Training training, final Exercise exercise, final int fileId)
        throws IOException {
        String url = ImageUtil.getImageUrl(context, training, exercise, ImageType.THUMBNAIL);
        File image = new File(imageDir, ImageType.THUMBNAIL.getImageFileName(fileId));

        LOGGER.info("Download exercise thumb: " + url);
        org.apache.commons.io.FileUtils.copyURLToFile(new URL(url), image);
    }
}
