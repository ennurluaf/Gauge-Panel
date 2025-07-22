package code;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class GContext {

    private Graphics2D g;
    private AffineTransform af;
    private boolean fill;
    public static boolean centered = false;

    public GContext(Graphics2D g) {
        this.g = g;
    }

    public GContext(Graphics g) {
        this.g = (Graphics2D) g;
    }

    public static GContext create(Graphics2D g) {
        return new GContext(g);
    }

    public Graphics2D getGraphics() {
        return g;
    }

    private void setColor(int... v) {
        Color c = Color.BLACK;
        switch (v.length) {
            case 1 -> c = new Color(v[0], v[0], v[0]);
            case 2 -> c = new Color(v[0], v[0], v[0], v[1]);
            case 3 -> c = new Color(v[0], v[1], v[2]);
            case 4 -> c = new Color(v[0], v[1], v[2], v[3]);
        }
        g.setColor(c);
    }

    public GContext fill(int... v) {
        setColor(v);
        fill = true;
        return this;
    }

    public GContext stroke(int... v) {
        setColor(v);
        fill = false;
        return this;
    }

    public GContext setStrokeWidth(float width) {
        g.setStroke(new BasicStroke(width));
        return this;
    }

    public GContext noStroke() {
        fill = true;
        return this;
    }

    public GContext noFill() {
        fill = false;
        return this;
    }

    public GContext ellipse(int x, int y, int w, int h) {
        if (fill)
            g.fillOval(x, y, w, h);
        else
            g.drawOval(x, y, w, h);
        return this;
    }

    public GContext circle(int x, int y, int r) {
        return ellipse(x - r, y - r, 2 * r, 2 * r);
    }

    public GContext line(int x1, int y1, int x2, int y2) {
        g.drawLine(x1, y1, x2, y2);
        return this;
    }

    public GContext point(int x, int y) {
        g.drawLine(x, y, x, y);
        return this;
    }

    public GContext roundRect(int x, int y, int w, int h, int arcW, int arcH) {
        if (fill)
            g.fillRoundRect(x, y, w, h, arcW, arcH);
        else
            g.drawRoundRect(x, y, w, h, arcW, arcH);
        return this;
    }

    public GContext save() {
        af = g.getTransform();
        return this;
    }

    public GContext restore() {
        if (af == null) return null;
        g.setTransform(af);
        g.setClip(null);
        return this;
    }

    public GContext clip(Shape shape) {
        g.setClip(shape);
        return this;
    }

    public GContext translate(int x, int y) {
        g.translate(x, y);
        return this;
    }

    public GContext rotate(double theta) {
        g.rotate(theta);
        return this;
    }

    public GContext rotate(double theta, int x, int y) {
        g.rotate(theta, x, y);
        return this;
    }

    public GContext scale(double sx, double sy) {
        g.scale(sx, sy);
        return this;
    }

    public GContext scale(double s) {
        return scale(s, s);
    }   

    public GContext text(String text, float x, float y) {
        if (centered) {
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();
            x -= textWidth / 2f;
            y += textHeight / 2f;
        }
        g.drawString(text, x, y);
        return this;
    }

    public Point textPos(String text) {
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        return new Point(-textWidth / 2, textHeight / 2);
    }

    public GContext drawImage(BufferedImage img, int x, int y, int w, int h) {
        g.drawImage(img, x, y, w, h, null);
        return this;
    }

    public GContext drawImage(BufferedImage img, int x, int y) {
        drawImage(img, x, y, img.getWidth(), img.getHeight());
        return this;
    }

    public GContext rect(int x, int y, int w, int h) {
        if (fill) {
            g.fillRect(x, y, w, h);
        } else {
            g.drawRect(x, y, w, h);
        }
        return this;
    }

    public GContext rect(Rectangle rect) {
        rect(rect.x, rect.y, rect.width, rect.height);
        return this;
    }





}
