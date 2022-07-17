package ch.m1m.infra.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface SongRepository extends JpaRepository<Song, Long> {

    @Query(value = "SELECT * FROM SONG s WHERE UPPER(s.title) LIKE ?1 OR UPPER(s.performer) LIKE ?1", nativeQuery = true)
    List<Song> findByTitleLike(String titleLike);
}
