package pl.adambaranowski.notesapp.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import pl.adambaranowski.notesapp.NotesAppApplication;
import pl.adambaranowski.notesapp.repository.NoteRepository;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        //value = SpringBootTest.WebEnvironment.MOCK,
        classes = NotesAppApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties"
)
class NoteControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    NoteRepository noteRepository;

    @Test
    void addNoteShouldReturnOkStatusAndJsonWithId() throws Exception {

        JSONObject newJNote = new JSONObject();
        newJNote.put("title", "title");
        newJNote.put("content", "content");

        mvc.perform(post("/notes")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(newJNote.toString())).andExpect(
                        status().isCreated()
        ).andExpect(content().string(containsString("\"id\":1")));

    }


}