package digitalphotoframe;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

public class USBContentHandler {

    public static final Logger log = Logger.getLogger("mainLogger");

    private static File[] allPicturesFiles;

    private static Thread diaShowThread;

    private static boolean isLinux = true;

    public USBContentHandler() {
        if (System.getProperty("os.name").contains("Windows")
                || System.getProperty("os.name").contains("windows")) {
            isLinux = false;
        }
    }

    public void handleNewDrive(File driveLocation) {
        log.fine("handling new Drive: " + driveLocation.getAbsolutePath());
        if (isLinux) {
            Runtime rt = Runtime.getRuntime();
            // as far as i know, this "final" should let the compiler be a more efficient
            // commented out for testing
            final String[] commands = { "ls", driveLocation.getAbsolutePath() };
            try {
                allPicturesFiles = LinuxHelper.listLinuxMedia(rt, commands, new CheckFunction() {
                    public boolean checkNameOfFile(String fileName) {
                        return fileName.contains(".png") || fileName.contains(".jpg") || fileName.contains(".jpeg");
                    }
                });
                log.fine(Arrays.asList(allPicturesFiles).toString());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            ArrayList<File> result = new ArrayList<File>();
            for (File imageFile : driveLocation.listFiles()) {
                if (imageFile.getName().contains(".png") || imageFile.getName().contains(".jpg")
                        || imageFile.getName().contains(".jpeg")) {
                    result.add(imageFile);
                }
            }
            allPicturesFiles = result.toArray(new File[result.size()]);
        }
        diaShowThread = new Thread(new DiaShowHandler(allPicturesFiles));
        diaShowThread.start();
    }

    protected void stopDiaShow() {
        if (diaShowThread != null && !diaShowThread.isInterrupted()) {
            diaShowThread.interrupt();
        }
    }
}
