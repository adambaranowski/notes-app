package pl.adambaranowski.notesapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.adambaranowski.notesapp.model.Note;
import pl.adambaranowski.notesapp.model.NoteVersion;
import pl.adambaranowski.notesapp.repository.NoteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Intermediary between note repository and note archive controller.
 * <p>
 * It is responsible for returning all data from database
 *
 * @author Adam Baranowski
 */
@Service
public class ArchiveService {

    private NoteRepository noteRepository;

    private StringBuilder sb = new StringBuilder();

    @Autowired
    public ArchiveService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    // YES, I KNOW THAT IT WOULD BE BETTER TO USE ANY JSON LIBRARY INSTEAD OF HARDCODING THIS JSONS MANUALLY
    // I WAS JUST BORED

    /**
     * Manually created JSON contains all versions of all notes
     *
     * @return JSON String contains all versions of all notes
     */
    public String getAllVersionsOfAllNotes() {
        List<Note> all = noteRepository.findAll();
        ArrayList<String> collect = all.stream()
                .map(this::parseNoteIntoJSON).collect(Collectors.toCollection(ArrayList::new));
        //clearing sb
        sb.setLength(0);
        sb.append("[");
        for (String note : collect
        ) {
            sb.append(note);
            sb.append(",");
        }
        sb.delete(sb.lastIndexOf(","), sb.lastIndexOf(",") + 1);
        sb.append("]");
        return sb.toString();
    }

    /**
     * @param note given note
     * @return JSON String of all versions of given note
     */
    private String parseNoteIntoJSON(Note note) {
        sb.setLength(0);
        sb.append("{\"id\":" + note.getId() + ",\"title\":\"" + note.getTitle() + "\", \"versions\": [");
        List<NoteVersion> noteVersions = note.getNoteVersions();
        for (NoteVersion version : noteVersions
        ) {
            sb.append("{\"content\":\"" + version.getContent() + "\", \"date\":\"" + version.getDateTime() + "\"},");
        }
        sb.delete(sb.lastIndexOf(","), sb.lastIndexOf(",") + 1);
        sb.append("]}");

        return sb.toString();
    }
}
