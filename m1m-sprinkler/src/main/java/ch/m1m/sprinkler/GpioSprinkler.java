package ch.m1m.sprinkler;

import com.pi4j.io.gpio.*;
import io.quarkus.runtime.ShutdownEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private String d1Name = "Led-1";
    private GpioPinDigitalOutput led1;

    public GpioSprinkler() {
        try {
            gpio = GpioFactory.getInstance();
            LOG.info("gpio calling getProvisionedPin() " + d1Name + " ...");
            led1 = (GpioPinDigitalOutput) gpio.getProvisionedPin(d1Name);
            LOG.info("gpio getProvisionedPin() returned " + led1);
            if (led1 == null) {
                LOG.info("gpio calling provisionDigitalOutputPin() " + d1Name);
                led1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, d1Name, PinState.LOW);
                LOG.info("gpio provisionDigitalOutputPin() returned " + led1);
            }

        } catch (UnsatisfiedLinkError e) {
            isOnRealPi = false;
            LOG.error("failed to initialize GPIO");
            LOG.error("when running on RaspberryPi this is critical");
            LOG.warn("falling back to GPIO emulation mode");
        }
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

    public void activate() {
        LOG.info("called Gpio...activate()");
        if (isOnRealPi) {
            led1.high();
        } else {
            LOG.info("fake led-1 toggle()");
        }
    }
}
