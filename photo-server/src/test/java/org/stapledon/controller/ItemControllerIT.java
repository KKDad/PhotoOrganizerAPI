package org.stapledon.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.stapledon.AbstractIntegrationTest;
import org.stapledon.StapledonAccountGivens;
import org.stapledon.entities.Item;

import javax.xml.crypto.Data;
import java.sql.Date;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class ItemControllerIT extends AbstractIntegrationTest {


    @Test
    void getFolders() throws Exception {
        var token = given().givenUser().authenticate();
        var itemContext = given().givenItems(
                List.of(StapledonAccountGivens.ItemParameters.builder()
                                .name("test")
                                .date("2021-01-01")
                                .childFolders(List.of(
                                        StapledonAccountGivens.ItemParameters.builder()
                                                .name("test2")
                                                .date("2021-02-02")
                                                .build(),
                                        StapledonAccountGivens.ItemParameters.builder()
                                                .name("test3")
                                                .date("2021-03-03")
                                                .build()))
                                .build(),
                        StapledonAccountGivens.ItemParameters.builder()
                                .name("test4")
                                .date("2021-04-04")
                                .build())
        );

        var result = mockMvc().perform(MockMvcRequestBuilders.get("/api/v1/items")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent).isNotNull();

        List<Item> items = objectMapper().readValue(responseContent, new TypeReference<List<Item>>() {});
        assertThat(items).isNotNull().hasSize(2);
        assertThat(items.get(0).getName()).isEqualTo("test");
        assertThat(items.get(0).getDate()).isEqualTo("2021-01-01");
        assertThat(items.get(0).getChildItems()).hasSize(2).containsExactlyInAnyOrder(
                Item.builder().name("test2").date(Date.valueOf("2021-02-02").toLocalDate()).build(),
                Item.builder().name("test3").date(Date.valueOf("2021-03-03").toLocalDate()).build()
        );
        assertThat(items.get(1).getName()).isEqualTo("test4");
        assertThat(items.get(1).getDate()).isEqualTo("2021-04-04");
    }
}