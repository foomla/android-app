package org.foomla.api.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Path;

import org.foomla.api.entities.UserRole;

public class AnnotationsUtil {

    public static String getRestPath(Class<?> resourceClass) {
        Path pathAnnotation = resourceClass.getAnnotation(Path.class);
        if (pathAnnotation != null) {
            return pathAnnotation.value();
        }
        return null;
    }

    public static List<UserRole> getUserRoles(Class<?> resourceClass) {
        Secured securedMethodAnnotation = findAnnotationRecursive(resourceClass, Secured.class);
        if (securedMethodAnnotation != null) {
            return Arrays.asList(securedMethodAnnotation.value());
        }
        else {
            return new ArrayList<UserRole>();
        }
    }

    public static List<UserRole> getUserRoles(Class<?> resourceClass, Method resourceMethod) {

        Method implementedMethod = findImplementedMethod(resourceMethod);
        if (implementedMethod != null) {
            Secured securedMethodAnnotation = implementedMethod.getAnnotation(Secured.class);
            if (securedMethodAnnotation != null) {
                return Arrays.asList(securedMethodAnnotation.value());
            }
        }

        Class<?>[] declaredClasses = resourceClass.getDeclaredClasses();

        if (declaredClasses != null) {
            for (Class<?> declaredClass : declaredClasses) {
                Secured securedAnnotation = declaredClass.getAnnotation(Secured.class);
                if (securedAnnotation != null) {
                    return Arrays.asList(securedAnnotation.value());
                }
            }
        }

        return new ArrayList<UserRole>();
    }

    public static boolean isSecured(Class<?> resourceClass) {

        Class<?>[] interfaces = resourceClass.getInterfaces();

        if (interfaces != null) {
            for (Class<?> declaredClass : interfaces) {
                if (declaredClass.getAnnotation(Secured.class) != null) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isSecured(Method resourceMethod) {
        Method implementedMethod = findImplementedMethod(resourceMethod);
        if (implementedMethod != null) {
            return implementedMethod.getAnnotation(Secured.class) != null;
        }
        else {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Annotation > T findAnnotationRecursive(Class<?> resourceClass, Class<T> annotationClass) {
        Annotation annotation = resourceClass.getAnnotation(annotationClass);
        if (annotation != null) {
            return (T) annotation;
        }

        Class<?>[] interfaces = resourceClass.getInterfaces();
        for (Class<?> interfaceze : interfaces) {
            T annotationFromInterface = findAnnotationRecursive(interfaceze, annotationClass);
            if(annotationFromInterface != null) {
                return annotationFromInterface;
            }
        }

        return null;
    }

    private static Method findImplementedMethod(Method resourceMethod) {
        Class<?>[] interfaces = resourceMethod.getDeclaringClass().getInterfaces();
        for (Class<?> interfaze : interfaces) {
            Method[] methods = interfaze.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(resourceMethod.getName())) {

                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Class<?>[] resourceMethodParameterTypes = resourceMethod.getParameterTypes();

                    if (parameterTypes.length == resourceMethodParameterTypes.length) {

                        boolean allParameterTypesEqual = true;
                        for (int i = 0; i < parameterTypes.length; i++) {
                            Class<?> parameterType = parameterTypes[i];
                            Class<?> resourceMethodParameterType = resourceMethodParameterTypes[i];
                            if (!parameterType.equals(resourceMethodParameterType)) {
                                allParameterTypesEqual = false;
                                break;
                            }
                        }
                        if (allParameterTypesEqual) {
                            return method;
                        }
                    }
                }
            }
        }
        return null;
    }
}
