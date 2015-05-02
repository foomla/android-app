package org.foomla.androidapp.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.foomla.androidapp.exception.FoomlaException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

public class FileUtils {

    public static final String APP_DIR = "org.foomla.androidapp";
    public static final String TRAINING_DIR = "trainings";
    public static final String ONLINE_TRAINING_DIR = "online_trainings";

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    public static File getAppDirectory(final Context context) throws IOException {
        return context.getDir(APP_DIR, Context.MODE_PRIVATE);
    }

    public static File getTrainingDir(final Context context, final String dirName) throws IOException {
        File appDir = getAppDirectory(context);
        File trainingsDir = new File(appDir, dirName);

        if (!trainingsDir.exists()) {
            if (!trainingsDir.mkdir()) {
                throw new IOException("Unable to create trainings directory '" + trainingsDir.getAbsolutePath() + "'");
            }
        }

        return trainingsDir;
    }

    public static File getTrainingDirectory(final Context context) throws IOException {
        return getTrainingDir(context, TRAINING_DIR);
    }

    public static File getOnlineTrainingDirectory(final Context context) throws IOException {
        return getTrainingDir(context, ONLINE_TRAINING_DIR);
    }

    public static File getImageDirectory(final Context context, final long trainingId) throws IOException {
        File trainingDir = getTrainingDirectory(context);
        File imageDir = new File(trainingDir, String.valueOf(trainingId));

        if (!imageDir.exists()) {
            imageDir.mkdir();
        }

        return imageDir;
    }

    public static int getNextId(final File directory) throws IOException {
        List<String> fileNames = getOrderedFileNames(directory);
        String last = fileNames != null && !fileNames.isEmpty() ? fileNames.get(fileNames.size() - 1) : "0.json";

        LOGGER.debug("last file is '" + last + "'");

        String[] components = last.split("\\.");
        try {
            return Integer.valueOf(components[0]) + 1;
        } catch (NumberFormatException e) {
            throw new FoomlaException("Unable to determine highest index.");
        }
    }

    public static String createFilename(final int id) {
        return "" + id + ".json";
    }

    public static List<String> getOrderedFileNames(final File directory) {
        String[] files = directory.list(new FilenameFilter() {
                    @Override
                    public boolean accept(final File file, final String s) {
                        return s.endsWith(".json");
                    }
                });

        List<String> fileNames = Arrays.asList(files);
        Collections.sort(fileNames);

        return fileNames;
    }
}
