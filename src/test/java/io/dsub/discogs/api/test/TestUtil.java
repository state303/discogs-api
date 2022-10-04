package io.dsub.discogs.api.test;

import io.dsub.discogs.api.artist.model.Artist;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Random;

public class TestUtil {

    public static final Random RANDOM = new Random();

    public static Flux<Artist> getRandomArtists(int count) {
        return Flux.range(1, count).flatMap(TestUtil::getRandomArtistWithID);
    }

    public static Mono<Artist> getRandomArtistWithID(int id) {
        return Mono.just(Artist.builder()
                .id(id)
                .dataQuality(getRandomStringOfSize(10))
                .profile(getRandomStringOfSize(100))
                .name(getRandomStringOfSize(20))
                .realName(getRandomStringOfSize(33))
                .build());
    }

    private static String getRandomStringOfSize(int size) {
        int min = 48;
        int max = 122;
        return RANDOM.ints(min, max)
                .filter(i -> (i == 32) || (48 <= i && i <= 57) || (65 <= i && i <= 90) || (97 <= i && i <= 122))
                .limit(size)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
