package io.dsub.discogs.api.validator;

import io.dsub.discogs.api.test.ConcurrentTest;
import io.dsub.discogs.api.test.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.test.StepVerifier;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class DelegatingReactiveValidatorImplTest extends ConcurrentTest {

    @Mock
    javax.validation.Validator validator;

    Validator reactiveValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.reactiveValidator = new ValidatorImpl(validator);
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
        var expected = new Object();
        given(validator.validate(any())).willReturn(Set.of());
        StepVerifier.create(reactiveValidator.validate(expected))
                .expectNext(expected)
                .verifyComplete();
    }
}
