package org.stapledon.components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.stapledon.components.organizers.YearMonthDayOrganizer;
import org.stapledon.dto.Photo;
import org.stapledon.dto.takeout.PhotoDetails;
import org.stapledon.dto.takeout.VerboseTime;

@SpringBootTest
class YearMonthDayOrganizerTest {

    @Autowired
    private YearMonthDayOrganizer subject;

    @Test
    void chooseTest() {
        var photo = Photo.builder()
                .name("IMG000001")
                .takeOutDetails(PhotoDetails.builder()
                        .creationTime(VerboseTime.builder().timestamp(1572795292L).formatted("Nov 3, 2019, 11:22:43 PM UTC").build())
                        .photoTakenTime(VerboseTime.builder().timestamp(1572823363L).formatted("Nov 3, 2019, 11:22:43 PM UTC").build())
                        .modificationTime(VerboseTime.builder().timestamp(1572823363L).formatted("Nov 3, 2019, 11:22:43 PM UTC").build())
                        .build()
                ).build();

        var result = subject.choose(photo);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("R:\\Photos\\Sorted\\2019-11-03", result.toString());
    }


}