package factorypanel;

import java.awt.Point;
import java.awt.Rectangle;
import static factorypanel.GaugePanel.SIZE;
import code.Drag;
import code.GContext;

public class FactoryPanel extends Rectangle {

    private static final long serialVersionUID = 1L;
    public final Drag drag = new Drag();
    private Point mouse = new Point(0, 0);
    
    public FactoryPanel(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void press(Point mouse) {
        if (contains(mouse)) {
            drag.press(mouse);
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
    }

    public void release() {
        drag.release();
    }

    private Point tilePos() {
        var offset = drag.getMouse(mouse);
        int negX = offset.x < 0 ? -1 : 0;
        int negY = offset.y < 0 ? -1 : 0;
        return new Point(
            (int) (offset.x / SIZE) + negX,
            (int) (offset.y / SIZE) + negY
        );
    }

    public void draw(GContext c) {
        c.save().clip(this).fill(200).rect(this);
        drawLines(c);
        c.translate(drag.origin.x, drag.origin.y);

        var tilePos = tilePos();
        c.fill(255,100,100).rect(tilePos.x*SIZE, tilePos.y*SIZE, SIZE, SIZE);
        
        c.restore();
    }
    
    private void drawLines(GContext c) {
        Point lineOffset = new Point(
            (int) this.drag.origin.x - (int) (this.drag.origin.x / SIZE) * SIZE,
            (int) this.drag.origin.y - (int) (this.drag.origin.y / SIZE) * SIZE
        );
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
