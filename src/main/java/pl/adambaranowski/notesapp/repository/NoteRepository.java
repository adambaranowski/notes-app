package pl.adambaranowski.notesapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.adambaranowski.notesapp.model.Note;

import java.util.Optional;

/**
 * Spring Data repository for performing CRUD operations for Notes.
 * <p>
 * Spring generates implementation of this interface and inject it where needed.
 * <p>
 * Thanks to Hibernate cascade operations, there is no need to save/update NoteVersions manually to the DataBase.
 * It is done automatically when performing right operation for Note.
 */
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    /**
     * Method for finding Note by title. Spring Data automatically generates implementation
     * of this method by its name
     *
     * @param title - search parameter
     * @return Optional of Note model
     */
    Optional<Note> findByTitle(String title);
}
