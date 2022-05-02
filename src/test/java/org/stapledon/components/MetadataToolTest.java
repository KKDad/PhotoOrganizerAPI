package org.stapledon.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.stapledon.dto.Photo;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;

@ExtendWith(MockitoExtension.class)
class MetadataToolTest {

    @InjectMocks
    private MetadataTool subject;

    @Mock
    ObjectMapper objectMapper;

    @Test
    void fetchAll() {
        var results = new HashMap<String, Photo>();
        var names = Arrays.asList("metadata.json", "2014-06-27-EFFECTS-edited.jpg", "2014-06-27-EFFECTS.jpg", "2014-06-27-EFFECTS.jpg.json", "2014-06-27-EFFECTS.vision");

        names.forEach(name -> {
            var p = Path.of("base", name);
            subject.load(results, p);
        });
        Assertions.assertEquals(1, results.size());
        Assertions.assertTrue(results.containsKey("base/2014-06-27-effects"));
    }
}