package digitalphotoframe;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;

public class USBDetector implements Runnable {

    public static final Logger log = Logger.getLogger("mainLogger");

    public static final USBContentHandler contentHandler = new USBContentHandler();

    private static final int sleepDuration = 1000;

    private static File[] oldListRoot = File.listRoots();
    private static boolean isLinux = true;

    static boolean wanted = true;

    // https://stackoverflow.com/questions/3831825/detect-usb-drive-in-java
    // by default this only finds usb DRIVES so this is like actually really perfekt
    // oh shit this wont work on linux
    @Override
    public void run() {
        try {
            log.fine("USB Detector now Starting");
            if (System.getProperty("os.name").contains("Windows")
                    || System.getProperty("os.name").contains("windows")) {
                isLinux = false;
            }
            log.config("running on: " + (isLinux ? "linux" : "windows"));
            if (isLinux) {
                // TODO: make a config file to have the folder which is scanned for Linux
                // variable
                // TODO: in this config file maybe also specify a folder for pictures and be
                // able to deactivate this usb scanning
                log.info("This Code requires Linux to automatically mount the USB Sticks to /media/.");
                Runtime rt = Runtime.getRuntime();
                // as far as i know, this "final" should let the compiler be a more efficient
                final String[] commands = { "ls", "/media/" };
                while (wanted) {
                    Thread.sleep(sleepDuration);
                    handleFileList(LinuxHelper.listLinuxMedia(rt, commands, new CheckFunction() {
                        public boolean checkNameOfFile(String fileName) {
                            return !".".equals(fileName) && !"..".equals(fileName);
                        }
                    }));
                }
            } else {
                // contentHandler.handleNewDrive(File.listRoots()[File.listRoots().length - 1]);
                while (wanted) {
                    Thread.sleep(sleepDuration);
                    handleFileList(File.listRoots());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            wanted = false;
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleFileList(File[] newList) {
        if (newList.length > oldListRoot.length) {
            log.fine("new drive detected");
            oldListRoot = newList;
            log.info(oldListRoot[oldListRoot.length - 1] + " drive detected");
            // handle the new usb drive/load its content to the display
            contentHandler.handleNewDrive(oldListRoot[oldListRoot.length - 1]);

        } else if (newList.length < oldListRoot.length) {
            log.info(oldListRoot[oldListRoot.length - 1] + " drive removed");
            contentHandler.stopDiaShow();
            oldListRoot = newList;
        }
    }

    public static void stop() {
        wanted = false;
    }
}
