package ch.m1m.sprinkler;

import ch.m1m.sprinkler.api.SprinklerAppState;
import ch.m1m.sprinkler.api.SprinklerState;
import ch.m1m.sprinkler.api.WaterPipe;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalTime;

@ApplicationScoped
public class StateManager {

    private static Logger LOG = LoggerFactory.getLogger(StateManager.class);

    private static final String APP_STATE_FILE_NAME = "sprinkler.json";

    // input from static- and web config
    private SprinklerAppState sprinklerAppState;

    // evaluated output if water is actually flow or not
    private SprinklerState sprinklerState = new SprinklerState();

    private String applConfigfileName;

    @Inject
    SprinklerStateEvaluator sprinklerStateEvaluator;

    @Inject
    GpioSprinkler gpioSprinkler;

    @Inject
    AppConfig appConfig;

    @Inject
    ObjectMapper objectMapper;

    @PostConstruct
    public void onInit() throws IOException {
        LOG.info("initialize global state...");
        applConfigfileName = appConfig.getDataDirectory() + "/" + APP_STATE_FILE_NAME;

        try {
            sprinklerAppState = readFromFile();
            LOG.info("restored appl config from file: {}", applConfigfileName);

        } catch (IOException e) {
            LOG.error("failed to read appl config file: {}", applConfigfileName, e);
            sprinklerAppState = initFromStaticConfig();
            LOG.info("fallback to static appl config");
            writeToFile(sprinklerAppState);
        }
    }

    private SprinklerAppState readFromFile() throws IOException {
        ObjectReader jsonReader = objectMapper.reader();
        return jsonReader.readValue(Paths.get(applConfigfileName).toFile(), SprinklerAppState.class);
    }

    public void writeToFile(SprinklerAppState appState) throws IOException {
        ObjectWriter jsonWriter = objectMapper.writer();
        jsonWriter.writeValue(Paths.get(applConfigfileName).toFile(), appState);
    }

    private SprinklerAppState initFromStaticConfig() {
        SprinklerAppState sprinklerAppState = new SprinklerAppState();
        sprinklerAppState.setActive(true);
        WaterPipe waterPipe = WaterPipeCreator.newWaterPipe(1, LocalTime.of(18, 00), 240);
        sprinklerAppState.getPipes().add(waterPipe);
        waterPipe = WaterPipeCreator.newWaterPipe(2, LocalTime.of(00, 01), 60);
        sprinklerAppState.getPipes().add(waterPipe);
        return sprinklerAppState;
    }

    public SprinklerAppState getSprinklerAppState() {
        return sprinklerAppState;
    }

    public SprinklerState getSprinklerState() {
        return sprinklerState;
    }

    public void updatePipeState() {

        SprinklerState newSprinklerState = sprinklerStateEvaluator.evaluate(sprinklerAppState);

        gpioSprinkler.activate();

        // replace the old value
        sprinklerState = newSprinklerState;
    }
}
