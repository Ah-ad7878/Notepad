package pk.org.cas.notepad;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Notes {

    String title;
    String description;
    long time;

    private int colour;

    public Notes() {
    }

    public Notes(String title, String description,int colour, long time) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.colour = colour;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColour() {
        return colour;
    }

    public void setColour(int colour) {
        this.colour = colour;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Notes notes = (Notes) o;
        return time == notes.time && colour == notes.colour && Objects.equals(title, notes.title) && Objects.equals(description, notes.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, time, colour);
    }

    @NonNull
    @Override
    public String toString() {
        return "Notes{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", time=" + time +
                ", colour=" + colour +
                '}';
    }
}
