package org.slf4j.impl;

import org.foomla.android.logging.AndroidLoggerFactory;

import org.slf4j.ILoggerFactory;

import org.slf4j.spi.LoggerFactoryBinder;

public class StaticLoggerBinder implements LoggerFactoryBinder {

    private static StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

    public static StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }

    @Override
    public ILoggerFactory getLoggerFactory() {
        return AndroidLoggerFactory.getInstance();
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return AndroidLoggerFactory.class.getName();
    }
}
