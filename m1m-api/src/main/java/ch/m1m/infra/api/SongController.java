package ch.m1m.infra.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/song")
public class SongController {

    @Autowired
    private DataSource dataSource;

    private static final Logger log = LoggerFactory.getLogger(SongController.class);

    private final SongRepository repository;

    SongController(SongRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/all")
    List<Song> all() {
        log.info("dataSource: {}", dataSource);
        return repository.findAll();
    }

    @GetMapping("/like/{pathLike}")
    List<Song> matchLike(@PathVariable String pathLike) throws SQLException {
        String likeExpr = "%" + pathLike + "%";
        likeExpr = likeExpr.toUpperCase();
        log.info("expression is: {}", likeExpr);
        return repository.findByTitleLike(likeExpr);
        //return execSqlLikeQuery(likeExpr);
    }

    List<Song> execSqlLikeQuery(String likeExpr) throws SQLException {
        String SQL_QUERY = "SELECT id, performer, title, url FROM SONG s WHERE UPPER(s.title) LIKE ?1 OR UPPER(s.performer) LIKE ?2";
        List<Song> songs = null;
        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_QUERY);) {
            songs = new ArrayList<>();
            Song song;
            pst.setString(1, likeExpr);
            pst.setString(2, likeExpr);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                songs.add(createSongFromRs(rs));
            }
            rs.close();
        }
        return songs;
    }

    Song createSongFromRs(ResultSet rs) throws SQLException {
        Song song = new Song();
        song.setId(rs.getLong("id"));
        song.setPerformer(rs.getString("performer"));
        song.setTitle(rs.getString("title"));
        song.setUrl(rs.getString("url"));
        return song;
    }
}
