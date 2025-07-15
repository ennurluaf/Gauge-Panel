package code;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Sprite extends BufferedImage {

    public static enum Pos {
        SIZE((x, y, w, h) -> rect(x, y, w, h)),
        TOPLEFT((x, y, w, h) -> rect(x, y, w, h)),
        TOPRIGHT((x, y, w, h) -> rect(x + w, y, w, h)),
        BOTTOMLEFT((x, y, w, h) -> rect(x, y + h, w, h)),
        BOTTOMRIGHT((x, y, w, h) -> rect(x + w, y + h, w, h)),
        CENTER((x, y, w, h) -> rect(x + w / 2, y + h / 2, w, h));

        private PosFunction posCalc;

        private Pos(PosFunction func) {
            this.posCalc = func;
        }

        private static Rectangle rect(int x, int y, int w, int h) {
            return new Rectangle(x, y, w, h);
        }

        protected Rectangle calcPos(Sprite sprite, int x, int y) {
            return posCalc.getPos(x, y, sprite.getWidth(), sprite.getHeight());
        }
    }

    private interface PosFunction {
        Rectangle getPos(int x, int y, int w, int h);

    }

    public Sprite(int width, int height) {
        super(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public Sprite(BufferedImage img) {
        super(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        getGraphics().drawImage(img, 0, 0, null);
    }

    public static Sprite load(String path) {
        try (InputStream is = Sprite.class.getClassLoader().getResourceAsStream("textures/" + path)) {
            return new Sprite(ImageIO.read(is));
        } catch (Exception e) {
            System.out.println("Failed to load: " + path + "\n" + e.getMessage());
            return null;
        }
    }

    public Rectangle getRect(Pos pos, int x, int y) {
        return pos.calcPos(this, x, y);
    }

    public Rectangle getRect() {
        return Pos.SIZE.posCalc.getPos(0, 0, getWidth(), getHeight());
    }

    public Sprite rotate(double angle) {
        Sprite result = new Sprite(getWidth(), getHeight());
        GContext c = new GContext(result.createGraphics());
        c.save().rotate(angle, -getWidth() / 2, -getHeight() / 2);
        c.drawImage(this, getWidth() / 2, getHeight() / 2).restore();
        return result;
    }

    public Sprite resize(int width, int height) {
        Sprite result = new Sprite(width, height);
        GContext c = new GContext(result.createGraphics());
        c.drawImage(this, 0, 0, width, height);
        return result;
    }

    public void draw(GContext c, int x, int y) {
        c.drawImage(this, x, y);
    }

    public void draw(GContext c, int x, int y, int w, int h) {
        c.drawImage(this, x, y, w, h);
    }

}
