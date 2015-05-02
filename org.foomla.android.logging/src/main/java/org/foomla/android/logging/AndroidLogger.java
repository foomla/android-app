package org.foomla.android.logging;

import org.slf4j.Marker;

import org.slf4j.helpers.MessageFormatter;

import android.util.Log;

public class AndroidLogger implements org.slf4j.Logger {

    private static final String MESSAGE_TEMPLATE = "%s -> %s";

    private final String logTag;
    private final String name;

    protected AndroidLogger(final String logTag, final String name) {
        this.logTag = logTag;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isTraceEnabled() {
        return Log.isLoggable(logTag, Log.VERBOSE);
    }

    @Override
    public void trace(final String msg) {
        Log.v(logTag, buildMessage(msg));
    }

    @Override
    public void trace(final String format, final Object arg) {
        Log.v(logTag, buildMessage(format, arg));
    }

    @Override
    public void trace(final String format, final Object arg1, final Object arg2) {
        Log.v(logTag, buildMessage(format, arg1, arg2));
    }

    @Override
    public void trace(final String format, final Object... args) {
        Log.v(logTag, buildMessage(format, args));
    }

    @Override
    public void trace(final String msg, final Throwable t) {
        Log.v(logTag, buildMessage(msg), t);
    }

    @Override
    public boolean isTraceEnabled(final Marker marker) {
        return isTraceEnabled();
    }

    @Override
    public void trace(final Marker marker, final String msg) {
        trace(msg);
    }

    @Override
    public void trace(final Marker marker, final String format, final Object arg) {
        trace(format, arg);
    }

    @Override
    public void trace(final Marker marker, final String format, final Object arg1, final Object arg2) {
        trace(format, arg1, arg2);
    }

    @Override
    public void trace(final Marker marker, final String format, final Object... args) {
        trace(format, args);
    }

    @Override
    public void trace(final Marker marker, final String msg, final Throwable t) {
        trace(msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return Log.isLoggable(logTag, Log.DEBUG);
    }

    @Override
    public void debug(final String msg) {
        Log.d(logTag, buildMessage(msg));
    }

    @Override
    public void debug(final String format, final Object arg) {
        Log.d(logTag, buildMessage(format, arg));
    }

    @Override
    public void debug(final String format, final Object arg1, final Object arg2) {
        Log.d(logTag, buildMessage(format, arg1, arg2));
    }

    @Override
    public void debug(final String format, final Object... args) {
        Log.d(logTag, buildMessage(format, args));
    }

    @Override
    public void debug(final String msg, final Throwable t) {
        Log.d(logTag, buildMessage(msg), t);
    }

    @Override
    public boolean isDebugEnabled(final Marker marker) {
        return isDebugEnabled();
    }

    @Override
    public void debug(final Marker marker, final String msg) {
        debug(msg);
    }

    @Override
    public void debug(final Marker marker, final String format, final Object arg) {
        debug(format, arg);
    }

    @Override
    public void debug(final Marker marker, final String format, final Object arg1, final Object arg2) {
        debug(format, arg1, arg2);
    }

    @Override
    public void debug(final Marker marker, final String format, final Object... args) {
        debug(format, args);
    }

    @Override
    public void debug(final Marker marker, final String msg, final Throwable t) {
        debug(msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return Log.isLoggable(logTag, Log.INFO);
    }

    @Override
    public void info(final String msg) {
        Log.i(logTag, buildMessage(msg));
    }

    @Override
    public void info(final String format, final Object arg) {
        Log.i(logTag, buildMessage(format, arg));
    }

    @Override
    public void info(final String format, final Object arg1, final Object arg2) {
        Log.i(logTag, buildMessage(format, arg1, arg2));
    }

    @Override
    public void info(final String format, final Object... args) {
        Log.i(logTag, buildMessage(format, args));
    }

    @Override
    public void info(final String msg, final Throwable t) {
        Log.i(logTag, buildMessage(msg), t);
    }

    @Override
    public boolean isInfoEnabled(final Marker marker) {
        return isInfoEnabled();
    }

    @Override
    public void info(final Marker marker, final String msg) {
        info(msg);
    }

    @Override
    public void info(final Marker marker, final String format, final Object arg) {
        info(format, arg);
    }

    @Override
    public void info(final Marker marker, final String format, final Object arg1, final Object arg2) {
        info(format, arg1, arg2);
    }

    @Override
    public void info(final Marker marker, final String format, final Object... args) {
        info(format, args);
    }

    @Override
    public void info(final Marker marker, final String msg, final Throwable t) {
        info(msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return Log.isLoggable(logTag, Log.WARN);
    }

    @Override
    public void warn(final String msg) {
        Log.w(logTag, buildMessage(msg));
    }

    @Override
    public void warn(final String format, final Object arg) {
        Log.w(logTag, buildMessage(format, arg));
    }

    @Override
    public void warn(final String format, final Object... args) {
        Log.w(logTag, buildMessage(format, args));
    }

    @Override
    public void warn(final String format, final Object arg1, final Object arg2) {
        Log.w(logTag, buildMessage(format, arg1, arg2));
    }

    @Override
    public void warn(final String msg, final Throwable t) {
        Log.w(logTag, buildMessage(msg), t);
    }

    @Override
    public boolean isWarnEnabled(final Marker marker) {
        return isWarnEnabled();
    }

    @Override
    public void warn(final Marker marker, final String msg) {
        warn(msg);
    }

    @Override
    public void warn(final Marker marker, final String format, final Object arg) {
        warn(format, arg);
    }

    @Override
    public void warn(final Marker marker, final String format, final Object arg1, final Object arg2) {
        warn(format, arg1, arg2);
    }

    @Override
    public void warn(final Marker marker, final String format, final Object... args) {
        warn(format, args);
    }

    @Override
    public void warn(final Marker marker, final String msg, final Throwable t) {
        warn(msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return Log.isLoggable(logTag, Log.ERROR);
    }

    @Override
    public void error(final String msg) {
        Log.e(logTag, buildMessage(msg));
    }

    @Override
    public void error(final String format, final Object arg) {
        Log.e(logTag, buildMessage(format, arg));
    }

    @Override
    public void error(final String format, final Object arg1, final Object arg2) {
        Log.e(logTag, buildMessage(format, arg1, arg2));
    }

    @Override
    public void error(final String format, final Object... args) {
        Log.e(logTag, buildMessage(format, args));
    }

    @Override
    public void error(final String msg, final Throwable t) {
        Log.e(logTag, buildMessage(msg), t);
    }

    @Override
    public boolean isErrorEnabled(final Marker marker) {
        return isErrorEnabled();
    }

    @Override
    public void error(final Marker marker, final String msg) {
        error(msg);
    }

    @Override
    public void error(final Marker marker, final String format, final Object arg) {
        error(format, arg);
    }

    @Override
    public void error(final Marker marker, final String format, final Object arg1, final Object arg2) {
        error(format, arg1, arg2);
    }

    @Override
    public void error(final Marker marker, final String format, final Object... args) {
        error(format, args);
    }

    @Override
    public void error(final Marker marker, final String msg, final Throwable t) {
        error(msg, t);
    }

    private String buildMessage(final String message, final Object... args) {
        if (args != null && args.length > 0) {
            final String compoundMessage = MessageFormatter.arrayFormat(message, args).getMessage();
            return String.format(MESSAGE_TEMPLATE, name, compoundMessage);
        } else {
            return String.format(MESSAGE_TEMPLATE, name, message);
        }
    }
}
