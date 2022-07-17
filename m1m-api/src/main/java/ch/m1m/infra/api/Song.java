package ch.m1m.infra.api;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Song {

    private @Id
    @GeneratedValue
    Long id;

    private String performer;
    private String title;

    private String url;

    public Song() {}

    public Song(String performer, String title, String url) {
        this.performer = performer;
        this.title = title;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(id, song.id) && performer.equals(song.performer) && title.equals(song.title) && url.equals(song.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, performer, title, url);
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", performer='" + performer + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
