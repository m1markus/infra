package ch.m1m.infra.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(SongRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new Song("Griff", "Black Hole", "https://www.youtube.com/watch?v=B2SK_jb68dk")));
            log.info("Preloading " + repository.save(new Song("Michael Patrick Kelly", "Beautiful Madness", "https://www.youtube.com/watch?v=t_eCNMMyL_Y")));
            log.info("Preloading " + repository.save(new Song("The Weeknd", "Save Your Tears", "https://www.youtube.com/watch?v=XXYlFuWEuKI")));
            log.info("Preloading " + repository.save(new Song("The Weeknd", "Blinding Lights", "https://www.youtube.com/watch?v=fHI8X4OXluQ")));
        };
    }
}

/*

    Michael Patrick Kelly - Beautiful Madness
    https://www.youtube.com/watch?v=t_eCNMMyL_Y
    https://youtu.be/t_eCNMMyL_Y

*/
