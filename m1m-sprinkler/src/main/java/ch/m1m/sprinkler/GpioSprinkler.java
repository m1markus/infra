package ch.m1m.sprinkler;

import ch.m1m.sprinkler.api.SprinklerState;
import ch.m1m.sprinkler.api.WaterPipeState;
import com.pi4j.io.gpio.*;
import io.quarkus.runtime.ShutdownEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

// GpioController gpioController = GpioFactory.getInstance();
//
// GpioPinDigitalOutput led1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "Led1", PinState.LOW);
// led1.high();
// led1.low();
// led1.toggle();

@ApplicationScoped
public class GpioSprinkler {

    private static Logger LOG = LoggerFactory.getLogger(GpioSprinkler.class);

    private boolean isOnRealPi = true;

    private GpioController gpio;

    private GpioPinDigitalOutput led1;
    private GpioPinDigitalOutput led2;

    public GpioSprinkler() {
        LOG.info("gpio ctor()...");
    }

    @PostConstruct
    public void onInit() {
        LOG.info("gpio onInit()...");
        try {
            gpio = GpioFactory.getInstance();

            led1 = provisionPiPin("Led-1", RaspiPin.GPIO_04);
            led2 = provisionPiPin("Led-2", RaspiPin.GPIO_05);

        } catch (UnsatisfiedLinkError e) {
            isOnRealPi = false;
            LOG.error("failed to initialize GPIO");
            LOG.error("when running on RaspberryPi this is critical");
            LOG.warn("falling back to GPIO emulation mode");
        }
    }

    private GpioPinDigitalOutput provisionPiPin(String pinName, Pin piPin) {
        GpioPinDigitalOutput pin;
        LOG.info("gpio calling getProvisionedPin() " + pinName + " ...");
        pin = (GpioPinDigitalOutput) gpio.getProvisionedPin(pinName);
        LOG.info("gpio getProvisionedPin() returned " + led1);
        if (pin == null) {
            LOG.info("gpio calling provisionDigitalOutputPin() " + pinName);
            pin = gpio.provisionDigitalOutputPin(piPin, pinName, PinState.LOW);
            LOG.info("gpio provisionDigitalOutputPin() returned " + led1);
        }
        return pin;
    }

    void onStop(@Observes ShutdownEvent event) throws InterruptedException {
        LOG.info("sprinkler onStop()...");
        shutdown();
    }

    private void shutdown() {
        if (isOnRealPi) {
            LOG.info("calling gpio.shutdown()");
            gpio.shutdown();
            gpio.unprovisionPin(led1);
        } else {
            LOG.info("fake gpio.shutdown()");
        }
    }

    public void evaluateAndSetPins(SprinklerState newSprinklerState) {
        LOG.info("called Gpio...activate()");

        for (WaterPipeState waterPipeState : newSprinklerState.getPipes()) {

            GpioPinDigitalOutput digitalPin = getDigitalPinFromId(waterPipeState.getId());
            if (waterPipeState.isFlow()) {
                if (isOnRealPi) {
                    digitalPin.high();
                } else {
                    LOG.info("fake set high() on pin for ID {}", waterPipeState.getId());
                }
            } else {
                if (isOnRealPi) {
                    digitalPin.low();
                } else {
                    LOG.info("fake set low() on pin for ID {}", waterPipeState.getId());
                }
            }
        }
    }

    public GpioPinDigitalOutput getDigitalPinFromId(int id) {
        if (id == 1) return led1;
        if (id == 2) return led2;
        throw new IllegalArgumentException("only id 1 or 2 are allowed");
    }
}
