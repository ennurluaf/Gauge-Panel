package support;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

public class Sprite extends BufferedImage {

    public Sprite(int width, int height) {
        super(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public static Sprite load(String path) {
        String base = System.getProperty("user.dir") + "/gaugepanel/src/main/resources/textures/";
        try {
            File file = new File(base + path);
            System.out.println(file + ", " + file.exists());

            BufferedImage img = ImageIO.read(file);
            if (img == null) {
                System.out.println("❌ Could not read image: " + path);
                return null;
            }

            Sprite sprite = new Sprite(img.getWidth(), img.getHeight());
            sprite.getGraphics().drawImage(img, 0, 0, null);
            return sprite;
        } catch (Exception e) {
            System.out.println(path + " does not exist \n" + e.getMessage());
        }
        return null;
    }

    public void draw(GContext c, int x, int y) {
        c.drawImage(this, x, y);
    }

    public void draw(GContext c, int x, int y, int w, int h) {
        c.drawImage(this, x, y, w, h);
    }

}
