package pk.org.cas.notepad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class Notes {

    private String id;
    private String title;
    private String description;
    private long time;
    private int colour;

    public Notes() {
    }

    public Notes(String title, String description, int colour, long time) {
        this.title = title;
        this.description = description;
        this.colour = colour;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getColour() {
        return colour;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notes notes = (Notes) o;
        return time == notes.time &&
                colour == notes.colour &&
                Objects.equals(id, notes.id) &&
                Objects.equals(title, notes.title) &&
                Objects.equals(description, notes.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, time, colour);
    }

    @NonNull
    @Override
    public String toString() {
        return "Notes{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", time=" + time +
                ", colour=" + colour +
                '}';
    }
}
