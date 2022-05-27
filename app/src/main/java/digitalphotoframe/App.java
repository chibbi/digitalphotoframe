package digitalphotoframe;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    public static final Logger log = Logger.getLogger("mainLogger");

    public static void main(String[] args) {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new LogFormatter());
        // TODO: make log Level configurable
        handler.setLevel(Level.ALL); // i dont get why both have to be set to all
        log.setLevel(Level.ALL); // in my mind log should not have this function at all, or at least be set to
                                 // all by default
        // deactivate default handler
        log.setUseParentHandlers(false);
        // activate custom handler
        log.addHandler(handler);
        log.finer("Program starting");
        Thread detectorThread = new Thread(new USBDetector());
        detectorThread.start();
        // MAYBE: do this:
        // https://stackoverflow.com/questions/26271464/implementation-of-addshutdownhook#26271986
        // and then run detectorThread.stop()
        // but out of experience:
        // sometimes does not work as intended and useless
    }
}
