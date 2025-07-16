package code;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Sprite extends BufferedImage implements Size{

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

    @Override
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

}
