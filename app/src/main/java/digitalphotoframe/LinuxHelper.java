package digitalphotoframe;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;

public class LinuxHelper {

    public static final Logger log = Logger.getLogger("mainLogger");

    /**
     * 
     * @param rt       just exists, so the method doesnt have to reinitialize the
     *                 variable
     * @param commands just exists, so the method doesnt have to reinitialize the
     *                 variable
     * @return
     */
    protected static File[] listLinuxMedia(Runtime rt, String[] commands, CheckFunction checkFunction)
            throws IOException {
        Process proc = rt.exec(commands);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

        ArrayList<File> result = new ArrayList<File>();

        String s = null;
        StringBuilder outputBuilder = new StringBuilder();
        while ((s = stdInput.readLine()) != null) {
            outputBuilder.append(s + "\n");
        }
        String[] filesStrings = outputBuilder.toString().split("\n");
        for (int i = 1; i < filesStrings.length; i++) {
            final String fileName = filesStrings[i].replaceAll("  ", " ").split(" ")[8];
            if (checkFunction.checkNameOfFile(fileName)) {
                result.add(new File(commands[commands.length - 1], fileName));
            }
        }

        log.finer("Linux media List:\n" + result.toString());

        // Read any errors from the attempted command
        while ((s = stdError.readLine()) != null) {
            log.warning("Getting Linux Media List throwed an error:\n" + s);
        }
        return result.toArray(new File[result.size()]);
    }

}
