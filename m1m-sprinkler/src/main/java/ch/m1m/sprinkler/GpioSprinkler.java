package ch.m1m.sprinkler;

import com.pi4j.io.gpio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

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

    public GpioSprinkler() {
        try {
            gpio = GpioFactory.getInstance();
            led1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "Led-1", PinState.LOW);

        } catch (UnsatisfiedLinkError e) {
            isOnRealPi = false;
            LOG.error("failed to initialize GPIO");
            LOG.error("when running on RaspberryPi this is critical");
            LOG.warn("falling back to GPIO emulation mode");
        }
    }

    public void activate() {
        LOG.info("called Gpio...activate()");
        if (isOnRealPi) {
            led1.toggle();
        } else {
            LOG.info("fake led-1 toggle()");
        }
    }
}
