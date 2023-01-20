package org.stapledon.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.stapledon.components.MetadataTool;
import org.stapledon.components.duplicates.IDupDetector;
import org.stapledon.components.organizers.IOrganizer;
import org.stapledon.configuration.properties.OrganizerProperties;

import java.nio.file.Path;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class TidyUpServiceTest {

    @InjectMocks
    private TidyUpService subject;

    @Mock
    OrganizerProperties config;

    @Mock
    Map<String, IOrganizer> organizers;

    @Mock
    Map<String, IDupDetector> dupDetector;

    @Mock
    MetadataTool photos;


    @Test
    void doOrganize() {
    }

    @Test
    void whenGivenBadPath_NoExceptionThrow() {
        Assertions.assertDoesNotThrow(() ->
                subject.removeIfEmpty(Path.of("./fooo")));
    }

    @Test
    void createDirectory() {
    }

    @Test
    void copyMove() {
    }
}