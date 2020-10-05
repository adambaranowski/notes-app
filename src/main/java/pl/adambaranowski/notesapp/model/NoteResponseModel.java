package pl.adambaranowski.notesapp.model;

import java.time.LocalDateTime;

/**
 * Note model using for sending JSON responses with SpringBoot build-in
 * Jackson library.
 * <p>
 * Note response have the following JSON form:
 * {
 * "id": 1,
 * "title": "example title",
 * "content": "example content",
 * "created": "2020-10-05T19:08:39",
 * "modified": "2020-10-05T19:08:39"
 * }
 * Contains necessary constructors, getters and setters.
 *
 * @author Adam Baranowski
 */
public class NoteResponseModel {

    /**
     * Should be taken from Note
     */
    private Long id;
    /**
     * Should be taken from Note
     */
    private String title;
    /**
     * Should be taken from NoteVersion
     */
    private String content;

    /**
     * Should be taken from first NoteVersion
     */
    private LocalDateTime created;
    /**
     * Should be taken from last NoteVersion
     */
    private LocalDateTime modified;

    public NoteResponseModel(Long id, String title, String content, LocalDateTime created, LocalDateTime modified) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.created = created;
        this.modified = modified;
    }

    public NoteResponseModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }
}

