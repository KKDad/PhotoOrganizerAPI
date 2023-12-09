package org.stapledon;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.stapledon.security.repository.RoleRepository;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Clock;

@Slf4j
@Getter
@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@Accessors(fluent = true)
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StapledonAccountGivens given;

    @Autowired
    private RoleRepository roleRepository;


    @MockBean(name = "clock")
    private Clock clock;

    @BeforeEach
    public void setup() {
        given().useDefaultClock();
        given().clearAccounts();
        given().addDefaultRoles();
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }
}