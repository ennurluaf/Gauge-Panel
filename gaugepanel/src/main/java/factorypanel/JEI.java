package factorypanel;

import java.awt.Rectangle;

import factorypanel.GaugePanel.Item;
import code.*;

public class JEI extends Rectangle {

    private static final long serialVersionUID = 1L;
    private final JSArray<Item> items;
    private final int cols, rows, pages;
    private int index = 0;
    private final Rectangle button;

    public JEI(int x, int y, int width, int height, JSArray<Item> items, int size) {
        super(x, y, width, height);
        this.items = items;
        this.cols = width / size;
        this.rows = height / size - 2;
        this.button = new Rectangle(x, height - 50, width, 50);
        this.pages = (int) Math.ceil((double) items.size() / (cols * rows));
    }

    private JSArray<Item> getCurrentPageItems() {
        int start = index * cols * rows;
        int end = Math.min(start + cols * rows, items.size());
        return (JSArray<Item>) items.subList(start, end);
    }

    public void draw(GContext c) {
        var list = getCurrentPageItems();
        c.fill(0, 0, 255, 100).rect(this);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++, index++) {
                if (index < list.size()) {
                    list.get(index).sprite().draw(c, x + col * 50, y + row * 50);
                }
            }
        }
        drawButton(c);
    }

    private void drawButton(GContext c) {
        String prev = "Prev", next = "Next", current = "Item " + (index + 1) + "/" + pages;
        int[] prevColor = {0, index == 0 ? 100 : 255}, nextColor = {0, index >= items.size() - 1 ? 100 : 255};
        int textY = c.textPos(next).y;
        int nextX = button.width - c.textPos(next).x;
        var textPos = c.textPos(current);
        c.save().translate(button.x, button.y);
        c.fill(prevColor).text(prev, button.x, textY);
        c.fill(nextColor).text(next, nextX, textY);
        c.fill().text(current, button.width+textPos.x, textY).restore();
    }

}
