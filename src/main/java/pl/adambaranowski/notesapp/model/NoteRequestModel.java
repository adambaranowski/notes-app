package pl.adambaranowski.notesapp.model;

import javax.validation.constraints.NotBlank;

/**
 * Note model using for processing JSON request with SpringBoot build-in
 * Jackson library.
 * <p>
 * Note request should have the following JSON form:
 * <p>
 * {
 * "title": "any given title",
 * "content":"any given content"
 * }
 * <p>
 * Contains necessary constructors, getters and setters.
 *
 * @author Adam Baranowski
 */
public class NoteRequestModel {

    /**
     * Title of Note. Cannot be blank(empty or whitespace characters)
     */
    @NotBlank
    private String title;

    /**
     * Content of Note. Cannot be blank(empty or whitespace characters)
     */
    @NotBlank
    private String content;

    public NoteRequestModel(@NotBlank String title, @NotBlank String content) {
        this.title = title;
        this.content = content;
    }

    public NoteRequestModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
