package org.stapledon.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.stapledon.AbstractIntegrationTest;
import org.stapledon.StapledonAccountGivens;
import org.stapledon.entities.Folder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class FolderControllerIT extends AbstractIntegrationTest {


    @Test
    void getFolders() throws Exception {
        var token = given().givenUser().authenticate();
        var folderContext = given().givenFolders(
                List.of(StapledonAccountGivens.FolderParameters.builder()
                                .name("test")
                                .date("2021-01-01")
                                .childFolders(List.of(
                                        StapledonAccountGivens.FolderParameters.builder()
                                                .name("test2")
                                                .date("2021-01-01")
                                                .build(),
                                        StapledonAccountGivens.FolderParameters.builder()
                                                .name("test3")
                                                .date("2021-01-01")
                                                .build()))
                                .build(),
                        StapledonAccountGivens.FolderParameters.builder()
                                .name("test4")
                                .date("2021-01-01")
                                .build())
        );

        var result = mockMvc().perform(MockMvcRequestBuilders.get("/api/v1/folders")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent).isNotNull();

        List<Folder> folders = objectMapper().readValue(responseContent, new TypeReference<List<Folder>>() {});
        assertThat(folders).isNotNull().hasSize(2);
        assertThat(folders.get(0).getName()).isEqualTo("test");
//        assertThat(folders.get(0).getChildFolders()).hasSize(2).containsExactlyInAnyOrder(
//                Folder.builder().name("test2").build(),
//                Folder.builder().name("test3").build()
//        );
    }
}