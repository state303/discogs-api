package io.dsub.discogs.api.test.util;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestUtil {

    public static final Random RANDOM = new Random();

    public static <T> T getInstanceOf(Class<T> clazz) {
        return instantiate(clazz);
    }

    @SuppressWarnings("unchecked")
    private static <T> T instantiate(Class<T> clazz) {
        T instance = null;
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (!constructor.trySetAccessible()) {
                continue;
            }
            try {
                var initArgs = getInitArgs(constructor);
                instance = (T) constructor.newInstance(initArgs);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("failed to instantiate " + clazz.getName());
            }
        }
        return instance;
    }

    private static Object[] getInitArgs(Constructor<?> constructor) {
        return Stream.of(constructor.getParameters())
                .map(Parameter::getType)
                .map(TestUtil::getValueOfType)
                .toArray();
    }

    public static Object getValueOfType(Class<?> clazz) {
        if (clazz.isAssignableFrom(Long.class)) {
            return (long) getRandomIndexValue();
        } else if (clazz.isAssignableFrom(Integer.class)) {
            return getRandomIndexValue();
        } else if (clazz.isAssignableFrom(String.class)) {
            return getRandomString();
        }
        return null;
    }

    private static void populateWithRandomValues(Object item) {
        for (Field field : item.getClass().getDeclaredFields()) {
            if (isLongField(field)) {
                assign(item, field, (long)getRandomIndexValue());
            } else if (isIntegerField(field)) {
                assign(item, field, getRandomIndexValue());
            } else if (isStringField(field)) {
                assign(item, field, getRandomString());
            }
        }
    }

    private static boolean isIntegerField(Field field) {
        return field.getType().isAssignableFrom(Integer.TYPE);
    }

    private static boolean isLongField(Field field) {
        return field.getType().isAssignableFrom(Long.TYPE);
    }

    private static boolean isStringField(Field field) {
        return field.getType().isAssignableFrom(String.class);
    }

    private static <T> void assign(Object subject, Field field, Object value) {
        try {
            if (!field.trySetAccessible()) {
                return;
            }
            field.set(subject, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getRandomIndexValue() {
        return Math.abs(RANDOM.nextInt()) + 1;
    }

    public static String getRandomString() {
        return getRandomString(10);
    }

    public static String getRandomString(int size) {
        int min = 48;
        int max = 122;
        return RANDOM.ints(min, max)
                .filter(i -> (i == 32) || (48 <= i && i <= 57) || (65 <= i && i <= 90) || (97 <= i && i <= 122))
                .limit(size)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static <T> ConstraintViolationException getConstraintViolationException(String... messages) {
        Set<ConstraintViolation<T>> violations = new HashSet<>();
        if (messages == null || messages.length == 0) {
            return new ConstraintViolationException(violations);
        }
        violations = Arrays.stream(messages)
                .map(TestUtil::<T>getConstraintViolation)
                .collect(Collectors.toSet());
        return new ConstraintViolationException(violations);
    }

    public static <T> ConstraintViolation<T> getConstraintViolation(String message) {
        return new ConstraintViolation<T>() {
            @Override
            public String getMessage() {
                return message;
            }

            @Override
            public String getMessageTemplate() {
                return null;
            }

            @Override
            public T getRootBean() {
                return null;
            }

            @Override
            public Class<T> getRootBeanClass() {
                return null;
            }

            @Override
            public Object getLeafBean() {
                return null;
            }

            @Override
            public Object[] getExecutableParameters() {
                return new Object[0];
            }

            @Override
            public Object getExecutableReturnValue() {
                return null;
            }

            @Override
            public Path getPropertyPath() {
                return null;
            }

            @Override
            public Object getInvalidValue() {
                return null;
            }

            @Override
            public ConstraintDescriptor<?> getConstraintDescriptor() {
                return null;
            }

            @Override
            public <U> U unwrap(Class<U> type) {
                return null;
            }
        };
    }

    public static Validator getNoOpValidator() {
        return new Validator() {
            @Override
            public <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
                return Set.of();
            }

            @Override
            public <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName, Class<?>... groups) {
                return Set.of();
            }

            @Override
            public <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups) {
                return Set.of();
            }

            @Override
            public BeanDescriptor getConstraintsForClass(Class<?> clazz) {
                throw new UnsupportedOperationException("not implemented");
            }

            @Override
            public <T> T unwrap(Class<T> type) {
                throw new UnsupportedOperationException("not implemented");
            }

            @Override
            public ExecutableValidator forExecutables() {
                throw new UnsupportedOperationException("not implemented");
            }
        };
    }
}
