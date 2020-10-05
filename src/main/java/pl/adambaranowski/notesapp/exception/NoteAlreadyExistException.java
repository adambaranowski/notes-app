package pl.adambaranowski.notesapp.exception;

/**
 * Exception being thrown, when NoteRepository found Note(with deleted set to false), which user want to create
 */
public class NoteAlreadyExistException extends RuntimeException {

}
