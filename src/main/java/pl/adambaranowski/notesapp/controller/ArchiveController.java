package pl.adambaranowski.notesapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.adambaranowski.notesapp.service.ArchiveService;


/**
 * Controller for ArchiveApi
 */
@RestController
@RequestMapping("/archive")
public class ArchiveController {

    private ArchiveService archiveService;

    @Autowired
    public ArchiveController(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }
    /**
     * @return all versions of all notes
     */
    @GetMapping("/getall")
    public ResponseEntity<String> getAllNotesWithAllVersions(){
        return new ResponseEntity<>(archiveService.getAllVersionsOfAllNotes(), HttpStatus.OK);
    }
}
