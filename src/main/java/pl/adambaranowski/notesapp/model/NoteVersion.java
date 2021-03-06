package pl.adambaranowski.notesapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * Java Bean model of NoteVersion for single Note for Hibernate.
 * <p>
 * Every Note has at least one NoteVersion.
 * <p>
 * Contains constructors, getters and setters.
 *
 * @author Adam Baranowski
 */
@Entity
public class NoteVersion implements Serializable {

    /**
     * Database main key - autogenerated by database engine
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Version identifier
     */
    private int version;

    /**
     * Content of Note
     */
    private String content;

    /**
     * Timestamp(date and time) of given NoteVersion creation
     * <p>
     * For Note dateTime field of first NoteVersion is creation time
     * and dateTime field of last NoteVersion is modified time.
     */
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateTime;

    /**
     * NoteVersion is relationship owner of relation Note-NoteVersion.
     * In DataBase there is a Table NoteVersion with foreign key "note_id", which
     * points to right Note id.
     */
    @ManyToOne
    @JoinColumn(name = "note_id")
    private Note note;

    public NoteVersion() {
    }

    public NoteVersion(int version, String content, LocalDateTime dateTime, Note note) {
        this.version = version;
        this.content = content;
        this.dateTime = dateTime;
        this.note = note;
    }

    public NoteVersion(int version, String content, LocalDateTime dateTime) {
        this.version = version;
        this.content = content;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }
}
