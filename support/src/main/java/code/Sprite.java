package code;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite extends BufferedImage implements Size{

    public Sprite(int width, int height) {
        super(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public Sprite(BufferedImage img) {
        super(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        getGraphics().drawImage(img, 0, 0, null);
    }

    public GContext create() {
        return new GContext(createGraphics());
    }

    public Rectangle getRect(int x, int y) {
        return Pos.TOPLEFT.calcPos(this, x, y);
    }

    public Rectangle getRect() {
        return Pos.SIZE.posCalc.getPos(0, 0, getWidth(), getHeight());
    }

    public Sprite rotate(double angle) {
        Sprite result = new Sprite(getWidth(), getHeight());
        result.create().rotate(Math.toRadians(angle), getWidth() / 2, getHeight() / 2).drawImage(this, 0, 0);
        return result;
    }

    public Sprite resize(int width, int height) {
        Sprite result = new Sprite(width, height);
        result.create().drawImage(this, 0, 0, width, height);
        return result;
    }

    public void draw(GContext c, int x, int y) {
        c.drawImage(this, x, y);
    }

}
