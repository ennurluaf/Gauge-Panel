package factorypanel;

import java.awt.Point;
import java.awt.Rectangle;
import static factorypanel.GaugePanel.SIZE;
import code.Drag;
import code.GContext;
import code.JSList;

public class FactoryPanel extends Rectangle {

    private static final long serialVersionUID = 1L;
    public final Drag drag = new Drag();
    private Point mouse = new Point(0, 0);
    private JSList<Box> boxes = new JSList<>();

    public FactoryPanel(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void press(Point mouse) {
        drag.press(mouse);
        var pos = tilePos();
        var next = boxes.find(box -> box.x == pos.x && box.y == pos.y);
        if(next == null) {
            boxes.add(new Box(pos.x, pos.y));
        } else if(next.selected){
            next.selected = false;
        } else {
            next.selected = true;
        }
    }

    public void drag(Point mouse) {
        this.mouse = mouse;
        if (drag.active) {
            drag.drag(mouse);
        }
    }

    public void move(Point mouse) {
        this.mouse = mouse;
        boxes.forEach(box -> {
            box.hovered = box.contains(mouse);
        });
    }

    public void release() {
        drag.release();
    }

    private Point tilePos() {
        var offset = drag.getMouse(mouse);
        int negX = offset.x < 0 ? -50 : 0;
        int negY = offset.y < 0 ? -50 : 0;
        return new Point(
                (int) (offset.x / SIZE) * 50 + negX,
                (int) (offset.y / SIZE) * 50 + negY);
    }

    public void draw(GContext c) {
        c.save().clip(this).fill(200).rect(this);
        drawLines(c);
        c.translate(drag.origin.x, drag.origin.y);

        var tilePos = tilePos();
        c.fill(255, 100, 100, 50).rect(tilePos.x, tilePos.y, SIZE, SIZE);

        c.fill(0).circle(0, 0, 5);

        boxes.forEach(box -> box.draw(c));

        c.restore();
    }

    private void drawLines(GContext c) {
        Point lineOffset = new Point(
                (int) this.drag.origin.x - (int) (this.drag.origin.x / SIZE) * SIZE,
                (int) this.drag.origin.y - (int) (this.drag.origin.y / SIZE) * SIZE);
        c.stroke(100, 100);
        // Vertical lines
        for (int i = 0; i <= width / SIZE; i++) {
            int x = i * SIZE + lineOffset.x;
            c.line(x, 0, x, height);
        }
        // Horizontal lines
        for (int j = 0; j <= height / SIZE; j++) {
            int y = j * SIZE + lineOffset.y;
            c.line(0, y, width, y);
        }
    }

}

class Box extends Rectangle {

    private static final long serialVersionUID = 1L;
    public boolean hovered = false, selected = false;

    public Box(int x, int y) {
        super(x, y, SIZE, SIZE);
    }

    public void draw(GContext c) {
        c.fill(100,255,100,100).rect(this);
        if (hovered) {
            c.fill(0, 50).rect(this);
        }
        if (selected) {
            c.stroke(0, 255).rect(this);
        }
    }

}
