package digitalphotoframe;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

public class DiaShowHandler implements Runnable {

    public static final Logger log = Logger.getLogger("mainLogger");

    private static int runningShows = 0;
    static boolean wanted = true;

    private static final int timeBetweenChange = 2; // in seconds

    private ImageDisplayer display = new ImageDisplayer();
    private File[] allPicturesFiles;
    private int currentPicture = 0;
    private int currentTime = 0;

    public DiaShowHandler(File[] allPicturesFiles) {
        runningShows = runningShows + 1;
        if (runningShows > 1) {
            // TODO: stop running diashows except this one
            // for now it just won't execute this one
            Thread.currentThread().interrupt();
        }
        this.allPicturesFiles = allPicturesFiles;
        log.fine("Diashow running on: " + Arrays.asList(allPicturesFiles).toString());
    }

    @Override
    public void run() {
        try {
            display.setImage(allPicturesFiles[currentPicture]);
            while (wanted) {
                currentPicture++;
                if (currentPicture >= allPicturesFiles.length) {
                    currentPicture = 0;
                }
                while (currentTime < timeBetweenChange * 1000) {
                    Thread.sleep(10);
                    currentTime += 10;
                }
                currentTime = 0;
                display.setImage(allPicturesFiles[currentPicture]);
            }
        } catch (InterruptedException e) {
            wanted = false;
            runningShows = runningShows - 1;
            Thread.currentThread().interrupt();
            display.closeWindow();
        }
    }

}
