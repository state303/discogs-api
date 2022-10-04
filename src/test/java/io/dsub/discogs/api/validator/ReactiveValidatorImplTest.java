package io.dsub.discogs.api.validator;

import io.dsub.discogs.api.artist.model.Artist;
import io.dsub.discogs.api.test.ConcurrentTest;
import io.dsub.discogs.api.test.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class ReactiveValidatorImplTest extends ConcurrentTest {
    @Mock
    Validator validator;

    ReactiveValidator reactiveValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.reactiveValidator = new ReactiveValidatorImpl(validator);
    }

    @Test
    void validateReturnsValidationError() {
        String error = "test constraint validation";
        ConstraintViolation<String> validation = TestUtil.getConstraintViolation(error);
        Set<ConstraintViolation<String>> violations = Set.of(validation);
        given(validator.validate(any(String.class))).willReturn(violations);
        StepVerifier.create(reactiveValidator.validate("test"))
                .expectErrorMatches(err -> err.getMessage().contains(error))
                .verify();
    }

    @Test
    void validateDelegatesReturnFromValidator() {
        Mono<Artist> artistMono = TestUtil.getRandomArtistMono(1);
        assertNotNull(artistMono);
        Artist expected = artistMono.block();
        assertNotNull(expected);

        given(validator.validate(any())).willReturn(Set.of());

        StepVerifier.create(reactiveValidator.validate(expected))
                .expectNext(expected)
                .verifyComplete();
    }
}
