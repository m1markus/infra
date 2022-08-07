package ch.m1m.sprinkler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.jackson.ObjectMapperCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class CustomObjectMapper implements ObjectMapperCustomizer {

    private static Logger LOG = LoggerFactory.getLogger(CustomObjectMapper.class);

    public void customize(ObjectMapper mapper) {
        LOG.info("jackson register JavaTimeModule()");
        mapper.registerModule(new JavaTimeModule());
        // activate pretty print
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        // to get "PT30S" for type Duration
        mapper.disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);
    }
}