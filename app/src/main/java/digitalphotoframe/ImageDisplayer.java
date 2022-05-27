package digitalphotoframe;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ImageDisplayer {

    public static final Logger log = Logger.getLogger("mainLogger");

    public final ImageIcon icon = new ImageIcon();
    public final JFrame frame = new JFrame();
    public final JLabel lbl = new JLabel();

    public final int windowWidth = 1280;
    public final int windowHeight = 800;

    public ImageDisplayer() {
        frame.setLayout(new FlowLayout());
        // TODO: make this size configurable
        // TODO: make it fullscreen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        // TODO: also configurable always on top
        frame.setSize(windowWidth, windowHeight);
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    protected void setImage(File test) {
        log.finest("Changing Image to: " + test.getAbsolutePath());
        BufferedImage img = null;
        try {
            img = ImageIO.read(test);
            log.finest("New Size of Image is: " + frame.getWidth() + ", " + frame.getHeight());
            lbl.setSize(frame.getWidth(), frame.getHeight());
            icon.setImage(manImage(img, frame.getWidth(), frame.getHeight()));
            lbl.repaint();
            frame.repaint();
            frame.setVisible(true);
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
    }

    private Image manImage(BufferedImage src, int width, int height) {
        float factor = 1;
        // TODO: Neko Chibbi gets fucked
        // half of his body is gone
        // i think that has something to do with the absolutly enourmous size jojo used
        // for him ITS 8 FUCKING K xD
        System.out.print(src.getWidth());
        System.out.print(" - ");
        System.out.print(width);
        System.out.print(" > ");
        System.out.print(src.getHeight());
        System.out.print(" - ");
        System.out.println(height);

        if ((src.getWidth() - width) > (src.getHeight() - height)) {
            factor = (float) width / src.getWidth();
        } else {
            factor = (float) height / src.getHeight();
        }
        Image dest = src.getScaledInstance(Math.round(src.getWidth() * factor), Math.round(src.getHeight() * factor),
                java.awt.Image.SCALE_SMOOTH);
        return dest;
    }

    protected void closeWindow() {
        frame.setVisible(false);
    }
}