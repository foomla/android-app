package org.foomla.android.logging;

import java.lang.reflect.Method;

import org.easymock.internal.ReflectionUtils;

import org.junit.Assert;
import org.junit.Test;

public class AndroidLoggerTest {

    @Test
    public void testBuildMessage() throws Exception {
        AndroidLogger logger = new AndroidLogger("--test--", "AndroidLoggerTest");

        Method buildMsg = ReflectionUtils.getDeclaredMethod(AndroidLogger.class, "buildMessage",
                new Class[] {String.class, Object[].class});
        buildMsg.setAccessible(true);

        String message = (String) buildMsg.invoke(logger, "Foo bar", null);

        Assert.assertEquals("AndroidLoggerTest -> Foo bar", message);
    }

    @Test
    public void testBuildMessageWithArgs() throws Exception {
        AndroidLogger logger = new AndroidLogger("--test--", "AndroidLoggerTest");

        Method buildMsg = ReflectionUtils.getDeclaredMethod(AndroidLogger.class, "buildMessage",
                new Class[] {String.class, Object[].class});
        buildMsg.setAccessible(true);

        String message = (String) buildMsg.invoke(logger, "Foo {} with {}", new Object[] {"bar", "args"});

        Assert.assertEquals("AndroidLoggerTest -> Foo bar with args", message);
    }
}
