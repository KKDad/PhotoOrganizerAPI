package org.stapledon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class PhotoOrganizerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PhotoOrganizerApplication.class, args);
    }
}
