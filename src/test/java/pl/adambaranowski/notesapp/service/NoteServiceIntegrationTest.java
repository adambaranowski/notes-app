package pl.adambaranowski.notesapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.adambaranowski.notesapp.exception.NoteAlreadyExistException;
import pl.adambaranowski.notesapp.exception.NoteNotFoundException;
import pl.adambaranowski.notesapp.model.NoteRequestModel;
import pl.adambaranowski.notesapp.model.NoteResponseModel;
import pl.adambaranowski.notesapp.repository.NoteRepository;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class NoteServiceIntegrationTest {

    @Autowired
    NoteService noteService;

    @BeforeEach
    public void cleanRepository(){

        //noteRepository.deleteAll();
        System.out.println("=========CLEANING========");
    }


    @TestConfiguration
    static class NoteServiceTestContextConfiguration{


        @Autowired
        NoteRepository noteRepository;

        @BeforeEach
        public void cleanRepository(){

            noteRepository.deleteAll();
            System.out.println("=========CLEANING========");
        }

        @Bean
        public NoteService noteService(){
            return new NoteService(noteRepository);
        }
    }



    @Test
    void createNewNoteShouldCreateNewNote() {
        // given
        NoteRequestModel noteRequestModel = new NoteRequestModel("title", "content");

        // when
        noteService.createNewNote(noteRequestModel);
        NoteResponseModel responseNote = noteService.getByTitle(noteRequestModel.getTitle());


        // then
        assertThat(responseNote.getContent(), equalTo(noteRequestModel.getContent()));
        assertThat(responseNote.getTitle(), equalTo(noteRequestModel.getTitle()));
    }

    @Test
    void createNewNoteShouldThrowExceptionWhenNoteExist() {
        // given
        NoteRequestModel noteRequestModel = new NoteRequestModel("title", "content");

        // when
        noteService.createNewNote(noteRequestModel);

        // then
        assertThrows(NoteAlreadyExistException.class, ()->  noteService.createNewNote(noteRequestModel));
}

    @Test
    void getAllNotesShouldReturnAllNotes() {
        // given
        NoteRequestModel noteRequestModel1 = new NoteRequestModel("first", "first note");
        NoteRequestModel noteRequestModel2 = new NoteRequestModel("second", "second note");

        // when
        noteService.createNewNote(noteRequestModel1);
        noteService.createNewNote(noteRequestModel2);


        List<NoteResponseModel> allNotes = noteService.getAllNotes();

        NoteResponseModel noteResponseModel1 = allNotes.get(0);
        NoteResponseModel noteResponseModel2 = allNotes.get(1);

        // then
        assertThat(noteResponseModel1.getContent(), equalTo(noteRequestModel1.getContent()));
        assertThat(noteResponseModel1.getTitle(), equalTo(noteRequestModel1.getTitle()));

        assertThat(noteResponseModel2.getContent(), equalTo(noteRequestModel2.getContent()));
        assertThat(noteResponseModel2.getTitle(), equalTo(noteRequestModel2.getTitle()));
    }

    @Test
    void getAllNotesShouldReturnEmptyListWhenNoNotes() {
        // given
        // when
        List<NoteResponseModel> allNotes = noteService.getAllNotes();
        // then
        assertThat(allNotes, is(empty()));
    }

    @Test
    void modifyNoteShouldModifyNote() {
        // given
        NoteRequestModel created = new NoteRequestModel("first", "first note");
        NoteRequestModel modified = new NoteRequestModel("first", "modified");

        // when
        noteService.createNewNote(created);

        noteService.modifyNote(modified);

        NoteResponseModel response = noteService.getByTitle(created.getTitle());

        // then
        assertThat(response.getContent(), equalTo(modified.getContent()));
        assertThat(response.getTitle(), equalTo(modified.getTitle()));

    }

    @Test
    void modifyNoteShouldThrowAnExceptionWhenTryToModifyNonExistingNote() {
        // given
        NoteRequestModel notExistingNote = new NoteRequestModel("not existing", "first note");

        // when
        // then
        assertThrows(NoteNotFoundException.class, ()->  noteService.modifyNote(notExistingNote));
    }


    @Test
    void removeNoteByTitleShouldRemoveNote() {
        // given
        NoteRequestModel created = new NoteRequestModel("first", "first note");

        // when
        noteService.createNewNote(created);

        noteService.removeNoteByTitle(created.getTitle());

        // then
        assertThrows(NoteNotFoundException.class, ()->  noteService.getByTitle(created.getTitle()));
    }

    @Test
    void removeNoteByIdShouldRemoveNote() {
        // given
        NoteRequestModel created = new NoteRequestModel("first", "first note");

        // when
        noteService.createNewNote(created);
        NoteResponseModel byTitle = noteService.getByTitle(created.getTitle());
        System.out.println("ID:     ");
        System.out.println(byTitle.getId());

        noteService.getAllNotes().forEach(noteResponseModel -> System.out.println(noteResponseModel.getId()+noteResponseModel.getTitle()+noteResponseModel.getContent()));

        noteService.removeNoteById(1L);

        // then
        assertThrows(NoteNotFoundException.class, ()->  noteService.getById(1L));
    }

    @Test
    void getByTitleShouldReturnProperNote() {
        // given
        NoteRequestModel noteRequestModel = new NoteRequestModel("title", "content");

        // when
        noteService.createNewNote(noteRequestModel);
        NoteResponseModel responseNote = noteService.getByTitle("title");

        // then
        assertThat(responseNote.getContent(), equalTo(noteRequestModel.getContent()));
        assertThat(responseNote.getTitle(), equalTo(noteRequestModel.getTitle()));
    }

    @Test
    void getByIdShouldReturnProperNote() {
        // given
        NoteRequestModel noteRequestModel = new NoteRequestModel("title", "content");

        // when
        noteService.createNewNote(noteRequestModel);
        NoteResponseModel responseNote = noteService.getById(1L);

        // then
        assertThat(responseNote.getContent(), equalTo(noteRequestModel.getContent()));
        assertThat(responseNote.getTitle(), equalTo(noteRequestModel.getTitle()));
    }
}