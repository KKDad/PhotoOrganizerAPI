package org.stapledon.components.organizers;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.stapledon.GoogleCredentialConfig;
import org.stapledon.dto.Photo;
import org.stapledon.dto.takeout.PhotoDetails;
import org.stapledon.dto.takeout.VerboseTime;

@Slf4j
@SpringBootTest
@Import(GoogleCredentialConfig.class)
class YearMonthDayOrganizerTest {

    @Autowired
    private YearMonthDayOrganizer subject;

    @Test
    void whenValidDate_returnsPathTest() {
        var photo = Photo.builder()
                .name("IMG000001")
                .takeOutDetails(PhotoDetails.builder()
                        .creationTime(VerboseTime.builder().timestamp(1433089679L).formatted("May 31, 2015, 4:27:59 PM UTC").build())
                        .photoTakenTime(VerboseTime.builder().timestamp(1418583092L).formatted("Dec 14, 2014, 6:51:32 PM UTC").build())
                        .modificationTime(VerboseTime.builder().timestamp(1554324586L).formatted("Apr 3, 2019, 8:49:46 PM UTC").build())
                        .build())
                .build();

        var result = subject.choosePath(photo);
        log.info("result={}", result);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("U:\\Sorted\\2014-12-14", result.toString());
    }

    @Test
    void whenInvalidDate_returnsNullTest() {
        var photo = Photo.builder()
                .name("IMG000001")
                .takeOutDetails(PhotoDetails.builder().build())
                .build();

        var result = subject.choosePath(photo);
        Assertions.assertNull(result);
    }
}