package code;

import java.awt.Point;
import java.awt.geom.Point2D.Double;

public class Drag {

    public Double origin, offset;
    public boolean active;

    public Drag() {
        this.origin = new Double(0, 0);
        this.offset = new Double(0, 0);
        this.active = false;
    }

    public Double getMouse(Point mouse) {
        return new Double(
            mouse.x - origin.x, mouse.y - origin.y
        );
    }

    public Point getLineOffset(int size) {
        int x = (int) origin.x - (int) (origin.x / size) * size;
        int y = (int) origin.y - (int) (origin.y / size) * size;
        return new Point(x, y);
    }

    public void press(Point p) {
        this.active = true;
        this.offset.x  = p.x - this.origin.x;
        this.offset.y  = p.y - this.origin.y;
    }

    public void drag(Point p) {
        if (this.active) {
            this.origin.x = p.x - this.offset.x;
            this.origin.y = p.y - this.offset.y;
        }
    }

    public void release() {
        this.active = false;
    }

}
