package org.stapledon.components.organizers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.stapledon.dto.Photo;
import org.stapledon.dto.takeout.PhotoDetails;
import org.stapledon.dto.takeout.VerboseTime;

@SpringBootTest
class YearMonthOrganizerTest {

    @Autowired
    private YearMonthOrganizer subject;

    @Test
    void whenValidDate_returnsPathTest() {
        var photo = Photo.builder()
                .name("IMG000001")
                .takeOutDetails(PhotoDetails.builder()
                        .creationTime(VerboseTime.builder().timestamp(1572795292L).formatted("Nov 3, 2019, 11:22:43 PM UTC").build())
                        .photoTakenTime(VerboseTime.builder().timestamp(1572823363L).formatted("Nov 3, 2019, 11:22:43 PM UTC").build())
                        .modificationTime(VerboseTime.builder().timestamp(1572823363L).formatted("Nov 3, 2019, 11:22:43 PM UTC").build())
                        .build())
                .build();

        var result = subject.choose(photo);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("R:\\Photos\\Sorted\\2019-11", result.toString());
    }

    @Test
    void whenInvalidDate_returnsNullTest() {
        var photo = Photo.builder()
                .name("IMG000001")
                .takeOutDetails(PhotoDetails.builder().build())
                .build();

        var result = subject.choose(photo);
        Assertions.assertNull(result);
    }
}