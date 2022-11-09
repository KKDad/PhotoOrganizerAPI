package org.stapledon;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(GoogleCredentialConfig.class)
class PhotoOrganizerApplicationTests {

	@Test
	void contextLoads() {
	}

}
