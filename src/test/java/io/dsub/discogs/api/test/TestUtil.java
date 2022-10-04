package io.dsub.discogs.api.test;

import io.dsub.discogs.api.artist.command.ArtistCommand;
import io.dsub.discogs.api.artist.model.Artist;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import java.util.Random;
import java.util.Set;

public class TestUtil {

    public static final Random RANDOM = new Random();

    public static int getRandomIndexValue() {
        return Math.abs(RANDOM.nextInt()) + 1;
    }

    public static Flux<Artist> getRandomArtists(int count) {
        return Flux.range(1, count).flatMap(TestUtil::getRandomArtist);
    }

    public static Mono<Artist> getRandomArtist() {
        return Mono.just(Artist.builder()
                .id(100)
                .dataQuality(getRandomString(10))
                .profile(getRandomString(100))
                .name(getRandomString(20))
                .realName(getRandomString(33))
                .build());
    }

    public static Mono<Artist> getRandomArtist(int id) {
        return Mono.just(Artist.builder()
                .id(id)
                .dataQuality(getRandomString(10))
                .profile(getRandomString(100))
                .name(getRandomString(20))
                .realName(getRandomString(33))
                .build());
    }

    public static ArtistCommand.CreateArtistCommand getCreateCommandFrom(Artist artist) {
        return new ArtistCommand.CreateArtistCommand(
                artist.getId(),
                artist.getName(),
                artist.getRealName(),
                artist.getProfile(),
                artist.getDataQuality());
    }

    public static ArtistCommand.UpdateArtistCommand getUpdateCommandFrom(Artist artist) {
        return new ArtistCommand.UpdateArtistCommand(
                artist.getName(),
                artist.getRealName(),
                artist.getProfile(),
                artist.getDataQuality());
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
