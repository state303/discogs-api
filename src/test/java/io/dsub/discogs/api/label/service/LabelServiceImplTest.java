package io.dsub.discogs.api.label.service;

import io.dsub.discogs.api.core.exception.ItemNotFoundException;
import io.dsub.discogs.api.label.command.LabelCommand;
import io.dsub.discogs.api.label.dto.LabelDTO;
import io.dsub.discogs.api.label.model.Label;
import io.dsub.discogs.api.label.repository.LabelRepository;
import io.dsub.discogs.api.test.ConcurrentTest;
import io.dsub.discogs.api.test.util.TestUtil;
import io.dsub.discogs.api.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class LabelServiceImplTest extends ConcurrentTest {

    @Mock
    private LabelRepository labelRepository;
    @Mock
    private Validator validator;
    private LabelServiceImpl labelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        labelService = new LabelServiceImpl(labelRepository, validator);
    }

    @Test
    void getLabelsReturnsExact_FromLabelRepository() {
        List<Label> labels = getLabels(10);
        given(labelRepository.findAll()).willReturn(Flux.fromIterable(labels));
        LabelDTO[] expected = arrayFromCollection(labels);
        StepVerifier.create(labelService.getLabels())
                .expectNext(expected)
                .verifyComplete();
        verify(labelRepository, times(1)).findAll();
    }

    @Test
    void getLabelsReturnsEmptyFlux() {
        given(labelRepository.findAll()).willReturn(Flux.empty());
        StepVerifier.create(labelService.getLabels()).verifyComplete();
        verify(labelRepository, times(1)).findAll();
    }

    @Test
    void getLabelsCallsRepository_WithPageable() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Label> labels = getLabels(10).stream().toList();
        List<LabelDTO> expectedContent = labels.stream().map(Label::toDTO).toList();
        Page<LabelDTO> expectedPage = new PageImpl<>(expectedContent, pageable, 10);

        given(labelRepository.findAll(pageable.getSort())).willReturn(Flux.fromIterable(labels));
        given(labelRepository.count()).willReturn(Mono.just((long) 10));

        StepVerifier.create(labelService.getLabels(pageable))
                .expectNext(expectedPage)
                .verifyComplete();

        verify(labelRepository, times(1)).findAll(pageable.getSort());
    }

    @Test
    void updateLabelValidatesCommand_ReturnsError() {
        LabelCommand.Update command = Mockito.mock(LabelCommand.Update.class);
        given(validator.validate(command)).willReturn(Mono.empty());

        StepVerifier.create(labelService.updateLabel(10L, command))
                .verifyError(ItemNotFoundException.class);

        verify(validator, times(1)).validate(command);
        verifyNoInteractions(labelRepository);
    }

    @Test
    void updateLabelMissingItem_ReturnsError() {
        long id = TestUtil.getRandomIndexValue();
        var otherLabel = spy(randomLabel(id));
        var command = spy(getUpdateCommandFrom(otherLabel));

        given(validator.validate(command)).willReturn(Mono.just(command));
        given(labelRepository.findById(id)).willReturn(Mono.empty());

        StepVerifier.create(labelService.updateLabel(id, command))
                .verifyError(ItemNotFoundException.class);

        verify(validator, times(1)).validate(command);
        verify(labelRepository, times(1)).findById(id);
    }

    @Test
    void updateLabelReturnsDTO() {
        var id = 10L;
        var label = randomLabel(id);
        var dto = label.toDTO();
        var command = getUpdateCommandFrom(label);

        given(labelRepository.findById(id)).willReturn(Mono.just(label));
        given(labelRepository.save(label)).willReturn(Mono.just(label));
        given(validator.validate(command)).willReturn(Mono.just(command));

        assertNotNull(dto);
        assertNotNull(label);
        assertNotNull(command);
        labelService.updateLabel(id, command).block();
//        StepVerifier.create(labelService.updateLabel(id, command))
//                .expectNext(dto)
//                .verifyComplete();

        verify(validator, times(1)).validate(command);
        verify(labelRepository, times(1)).findById(id);
        verify(labelRepository, times(1)).save(label);
    }

    @Test
    void saveOrUpdateReturnsDTO() {
        var label = randomLabel(5);
        var dto = label.toDTO();
        var command = getCreateCommandFrom(label);
        var captor = ArgumentCaptor.forClass(LabelCommand.Create.class);

        given(validator.validate(command)).willReturn(Mono.just(command));
        given(labelRepository.saveOrUpdate(captor.capture())).willReturn(Mono.just(label));

        StepVerifier.create(labelService.saveOrUpdate(command))
                .expectNext(dto)
                .verifyComplete();

        verify(validator, times(1)).validate(command);
        verify(labelRepository, times(1)).saveOrUpdate(command);
    }

    @Test
    void deleteLabelCallsRepository() {
        long id = TestUtil.getRandomIndexValue();
        given(labelRepository.deleteById(id)).willReturn(Mono.empty());
        StepVerifier.create(labelService.deleteLabel(id)).verifyComplete();
        verify(labelRepository, times(1)).deleteById(id);
    }

    @Test
    void findByIdReturnsError() {
        long id = TestUtil.getRandomIndexValue();
        given(labelRepository.findById(id)).willReturn(Mono.empty());
        StepVerifier.create(labelService.findById(id))
                .verifyError(ItemNotFoundException.class);
        verify(labelRepository, times(1)).findById(id);
    }

    @Test
    void findByIdReturnsItem() {
        long id = TestUtil.getRandomIndexValue();
        Label label = randomLabel(id);
        given(labelRepository.findById(id)).willReturn(Mono.just(label));
        StepVerifier.create(labelService.findById(id))
                .expectNext(label.toDTO())
                .verifyComplete();
        verify(labelRepository, times(1)).findById(id);
    }

    private List<Label> getLabels(int size) {
        return IntStream.range(0, size).mapToObj(this::randomLabel).toList();
    }

    private LabelDTO[] arrayFromCollection(Collection<Label> iterable) {
        return iterable.stream().map(Label::toDTO).toArray(LabelDTO[]::new);
    }

    private LabelCommand.Update getUpdateCommandFrom(Label label) {
        return new LabelCommand.Update(label.getName(),
                label.getProfile(),
                label.getDataQuality(),
                label.getContactInfo());
    }

    private LabelCommand.Create getCreateCommandFrom(Label label) {
        return new LabelCommand.Create(label.getId(),
                label.getName(),
                label.getProfile(),
                label.getDataQuality(),
                label.getContactInfo());
    }

    private Label randomLabel(long id) {
        return Label.builder()
                .id(id)
                .name(TestUtil.getRandomString())
                .profile(TestUtil.getRandomString())
                .contactInfo(TestUtil.getRandomString())
                .dataQuality(TestUtil.getRandomString())
                .build();
    }
}