package pl.adambaranowski.notesapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.adambaranowski.notesapp.exception.NoteAlreadyExistException;
import pl.adambaranowski.notesapp.exception.NoteNotFoundException;
import pl.adambaranowski.notesapp.model.Note;
import pl.adambaranowski.notesapp.model.NoteRequestModel;
import pl.adambaranowski.notesapp.model.NoteResponseModel;
import pl.adambaranowski.notesapp.service.NoteService;

import javax.validation.Valid;
import java.util.List;

/**
 * Main controller of note application. Responsible for processing note requests.
 * Annotation @RestController means that all of methods' return types are objects not views
 * @author Adam Baranowski
 */
@RestController
@RequestMapping("/notes")
public class NoteController {

    private NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    /**
     * PostMapping for creating new Note
     *
     * @param noteRequestModel - model which contains title and content of new note
     * @param bindingResult - errors that have occured during validation. Spring Validation take care of this param automatically
     * @return response entity with proper HttpStatus and JSON contains id of new note and its sended properties (title, content)
     * @throws ResponseStatusException - which in spring results of responsing with right HttpStatus
     */
    @PostMapping()
    public ResponseEntity<String> addNote(@RequestBody @Valid NoteRequestModel noteRequestModel, BindingResult bindingResult){
        //spring validation
        if(bindingResult.hasErrors())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Title and Content cannot be empty!");

        try {
            Note newNote = noteService.createNewNote(noteRequestModel);

            //manually created JSON to include Id of newly created note
            String response = "{" +
                    "\"id\":"+ newNote.getId()+","+
                    "\"title\":\"" + newNote.getTitle() + "\","+
                    "\"content\":\"" + newNote.getRecentVersion().getContent() + "\"}";

            return new ResponseEntity<String>(response, HttpStatus.CREATED);

            //noteService Exception
        }catch (NoteAlreadyExistException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Given note has been already created! You can modify it!");
        }
    }

    /**
     * Get note by its id
     * @param id id of wanted Note
     * @return Note of given id in NoteResponseModel form (for JSON)
     * @throws ResponseStatusException which results with sending proper HttpStatus
     */
    @GetMapping("/{id}")
    public ResponseEntity<NoteResponseModel> getById(@PathVariable Long id){
        try {
            return new ResponseEntity<>(noteService.getById(id), HttpStatus.OK);
        }catch (NoteNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note of given id does not exist!");
        }
    }

    /**
     * Get note by its title
     * @param title of wanted Note
     * @return Note of given title in NoteResponseModel form (for JSON)
     * @throws ResponseStatusException which results with sending proper HttpStatus
     */
    @GetMapping("/title")
    public ResponseEntity<NoteResponseModel> getByTitle(@RequestParam String title){
        try {
            return new ResponseEntity<>(noteService.getByTitle(title), HttpStatus.OK);
        }catch (NoteNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note of given title does not exist!");
        }
    }

    /**
     * Get all not-deleted notes
     * @return list of NoteResponseModel that contains every Note which "deleted" field is false
     */
    @GetMapping("/getall")
    public ResponseEntity<List<NoteResponseModel>> getAllNotes(){
        return new ResponseEntity<List<NoteResponseModel>>(noteService.getAllNotes(), HttpStatus.OK);
    }

    /**
     *  Mapping for updating Note.
     *
     *  Content of Note of title given in noteRequestModel is changed into content sent in it
     *
     * @param noteRequestModel model which contains title and content of modified note
     * @param bindingResult errors that have occured during validation. Spring Validation take care of this param automatically
     * @return proper HttpStatus
     * @throws ResponseStatusException which in spring results of responsing with right HttpStatus
     */
    @PutMapping
    public ResponseEntity<?> modifyNote(@Valid @RequestBody NoteRequestModel noteRequestModel, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Title and Content cannot be empty!");
        try {
            noteService.modifyNote(noteRequestModel);
            return new ResponseEntity(HttpStatus.OK);
        }catch (NoteNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Note of given title does not exist!");
        }
    }

    /**
     * Deleting note of given id. See NoteService for more details.
     * @param id id of deleting note as pathvariable
     * @return proper HttpStatus
     * @throws ResponseStatusException which in spring results of responsing with right HttpStatus
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        try {
            noteService.removeNoteById(id);
            return new ResponseEntity(HttpStatus.OK);
        }catch (NoteNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Note of given title does not exist!");
        }
    }

    /**
     * Deleting note of given id. See NoteService for more details.
     * @param title of deleting note as request-param
     * @return proper HttpStatus
     * @throws ResponseStatusException which in spring results of responsing with right HttpStatus
     */
    @DeleteMapping("/title")
    public ResponseEntity<?> deleteById(@RequestParam String title){
        try {
            noteService.removeNoteByTitle(title);
            return new ResponseEntity(HttpStatus.OK);
        }catch (NoteNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Note of given title does not exist!");
        }
    }
}
