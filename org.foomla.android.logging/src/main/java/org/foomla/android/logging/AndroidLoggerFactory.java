package org.foomla.android.logging;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class AndroidLoggerFactory implements ILoggerFactory {

    public static final String DEFAULT_LOG_TAG = "[unset]";
    public static final String PACKAGE_SEPARATOR = "\\.";

    private static AndroidLoggerFactory INSTANCE;

    private final Map<String, AndroidLogger> loggers;
    private String logTag;

    private AndroidLoggerFactory(final String logTag) {
        this.loggers = new HashMap<String, AndroidLogger>();
        this.logTag = logTag;
    }

    @Override
    public Logger getLogger(final String name) {
        AndroidLogger logger = loggers.get(name);
        if (logger == null) {
            String loggerName = stripName(name);
            logger = new AndroidLogger(logTag, loggerName);

            loggers.put(name, logger);
        }

        return logger;
    }

    public static AndroidLoggerFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AndroidLoggerFactory(DEFAULT_LOG_TAG);
        }

        return INSTANCE;
    }

    public static void setLogTag(final String logTag) {
        if (INSTANCE == null) {
            INSTANCE = new AndroidLoggerFactory(logTag);
        } else {
            INSTANCE.logTag = logTag;
        }
    }

    private String stripName(final String className) {
        String[] classParts = className.split(PACKAGE_SEPARATOR);
        return classParts[classParts.length - 1];
    }
}
