package pl.adambaranowski.notesapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.adambaranowski.notesapp.exception.NoteAlreadyExistException;
import pl.adambaranowski.notesapp.exception.NoteNotFoundException;
import pl.adambaranowski.notesapp.model.Note;
import pl.adambaranowski.notesapp.model.NoteRequestModel;
import pl.adambaranowski.notesapp.model.NoteResponseModel;
import pl.adambaranowski.notesapp.model.NoteVersion;
import pl.adambaranowski.notesapp.repository.NoteRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Intermediary between note repository and note controller.
 * <p>
 * It is responsible for creating and manipulating model objects
 *
 * @author Adam Baranowski
 */
@Service
public class NoteService {

    private NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * Creates new Note if it Note of given in noteRequestModel title has never been created before.
     * <p>
     * When Note of given title had been created and deleted
     * new creation means that there is created new version of note
     * and "deleted" is set to false.
     *
     * @param noteRequestModel - created automatically by Jackson Library from request JSON
     * @return Note model of created Note
     * @throws NoteAlreadyExistException - when user tries to create note of existing title
     */
    public Note createNewNote(NoteRequestModel noteRequestModel) {


        Optional<Note> byTitle = noteRepository.findByTitle(noteRequestModel.getTitle());

        //Throw an exception when user tries to create note of existing title
        if (byTitle.isPresent() && !byTitle.get().isDeleted())
            throw new NoteAlreadyExistException();

        //When Note of given title had been created and deleted
        //new creation means that there is created new version of note
        //and "deleted" is set to false
        //It is important for tracking modifications of Note
        if (byTitle.isPresent() && byTitle.get().isDeleted()) {
            Note existingNote = byTitle.get();
            existingNote.setDeleted(false);
            modifyNote(noteRequestModel);
            return existingNote;
        }

        Note note = new Note(noteRequestModel.getTitle());
        NoteVersion noteVersion = new NoteVersion(1, noteRequestModel.getContent(), LocalDateTime.now(), note);
        note.getNoteVersions().add(noteVersion);
        noteRepository.save(note);
        return note;
    }

    /**
     * List of all Notes in form of List (NoteResponseModel) for JSON response
     *
     * @return all not-deleted Notes as NoteResponseModel List(ArrayList)
     */
    public List<NoteResponseModel> getAllNotes() {
        List<Note> all = noteRepository.findAll();
        return all.stream()
                .filter(note -> !note.isDeleted())
                .map(note -> new NoteResponseModel(
                        note.getId(),
                        note.getTitle(),
                        note.getRecentVersion().getContent(),
                        note.getFirstVersion().getDateTime(),
                        note.getRecentVersion().getDateTime()
                )).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Modify (set new content) note of title given in noteRequestModel
     *
     * @param noteRequestModel contains title and new content
     * @throws NoteNotFoundException when note of given title does not exist
     */
    public void modifyNote(NoteRequestModel noteRequestModel) {
        Optional<Note> byTitle = noteRepository.findByTitle(noteRequestModel.getTitle());
        ;
        if (byTitle.isPresent() && !byTitle.get().isDeleted()) {
            Note found = byTitle.get();
            NoteVersion recentVersion = found.getRecentVersion();
            NoteVersion newVersion = new NoteVersion(
                    recentVersion.getVersion() + 1,
                    noteRequestModel.getContent(),
                    LocalDateTime.now(),
                    found
            );
            found.getNoteVersions().add(newVersion);
            //NoteVersion is saved automatically by Hibernate
            noteRepository.save(found);
        } else {
            throw new NoteNotFoundException();
        }
    }

    /**
     * Set "deleted" of note of given title to true. Note is still available in archiveAPI
     *
     * @param title title of deleted note
     * @throws NoteNotFoundException when note of given title does not exist or has "deleted" set to true
     */
    public void removeNoteByTitle(String title) {
        Optional<Note> byTitle = noteRepository.findByTitle(title);
        if (byTitle.isPresent() && !byTitle.get().isDeleted()) {
            Note found = byTitle.get();
            found.setDeleted(true);
            noteRepository.save(found);
        } else {
            throw new NoteNotFoundException();
        }
    }

    /**
     * Set "deleted" of note of given id to true. Note is still available in archiveAPI
     *
     * @param id id of deleted note
     * @throws NoteNotFoundException when note of given id does not exist or has "deleted" set to true
     */
    public void removeNoteById(Long id) {
        Optional<Note> byId = noteRepository.findById(id);
        if (byId.isPresent() && !byId.get().isDeleted()) {
            Note found = byId.get();
            found.setDeleted(true);
            noteRepository.save(found);
        } else {
            throw new NoteNotFoundException();
        }
    }

    /**
     * Returning note (NoteResponseModel for JSON) of given title
     *
     * @param title title of indicated Note
     * @return NoteResponseModel of Note
     * @throws NoteNotFoundException when note of given title does not exist or has "deleted" set to true
     */
    public NoteResponseModel getByTitle(String title) {
        Optional<Note> byTitle = noteRepository.findByTitle(title);
        if (byTitle.isPresent() && !byTitle.get().isDeleted()) {
            Note found = byTitle.get();
            return new NoteResponseModel(
                    found.getId(),
                    found.getTitle(),
                    found.getRecentVersion().getContent(),
                    found.getFirstVersion().getDateTime(),
                    found.getRecentVersion().getDateTime()
            );
        } else {
            throw new NoteNotFoundException();
        }
    }

    /**
     * Returning note (NoteResponseModel for JSON) of given id
     *
     * @param id id of indicated Note
     * @return NoteResponseModel of Note
     * @throws NoteNotFoundException when note of given id does not exist or has "deleted" set to true
     */
    public NoteResponseModel getById(Long id) {
        Optional<Note> byId = noteRepository.findById(id);
        if (byId.isPresent() && !byId.get().isDeleted()) {
            Note found = byId.get();
            return new NoteResponseModel(
                    found.getId(),
                    found.getTitle(),
                    found.getRecentVersion().getContent(),
                    found.getFirstVersion().getDateTime(),
                    found.getRecentVersion().getDateTime()
            );
        } else {
            throw new NoteNotFoundException();
        }
    }
}
