package factorypanel;

import java.awt.Point;
import java.awt.Rectangle;
import static factorypanel.GaugePanel.SIZE;

import code.*;

public class JEI extends Rectangle {

    private static final long serialVersionUID = 1L;
    private final JSArray<Item> items;
    private final int cols, rows, pages;
    private int index = 0;
    private long clock = System.currentTimeMillis(), cd = 500; // cooldown in milliseconds
    private final Rectangle button;
    private String searchQuery = "";

    public JEI(int x, int y, int width, int height, JSArray<Item> items, int size) {
        super(x, y, width, height);
        this.items = items;
        this.cols = width / size;
        this.rows = height / size - 2;
        this.button = new Rectangle(x, height - SIZE, width, SIZE);
        this.pages = (int) Math.ceil((double) items.size() / (cols * rows));
    }

    public void search(String query) {
        if (query.isEmpty()) {
            index = 0; // Reset index if search is cleared
            this.searchQuery = ""; // Clear search query
        } else {
            this.searchQuery = query.toLowerCase();
        }
    }

    public void press(Point mouse) {
        if (button.contains(mouse) && System.currentTimeMillis() - clock > cd) {
            if (mouse.x < button.x + button.width / 2) {
                if (index > 0) {
                    index--;
                }
            } else {
                if (index < pages - 1) {
                    index++;
                }
            }
            clock = System.currentTimeMillis();
        }
        this.getCurrentPageItems().forEach(item -> {
            item.selected = item.contains(mouse);
        });
    }

    public void move(Point mouse) {
        this.getCurrentPageItems().forEach(item -> {
            item.hovered = item.contains(mouse);
        });
    }

    private JSArray<Item> getCurrentPageItems() {
        int start = index * cols * rows;
        int end = Math.min(start + cols * rows, items.size());
        return items.filter(item -> item.name().contains(searchQuery)).slice(start, end);
    }

    public void draw(GContext c) {
        drawName(c);
        drawItems(c);
        drawButton(c);
    }

    private void drawName(GContext c) {
        var item = getCurrentPageItems().find(i -> i.hovered);
        String text = item != null ? item.name() : "No item selected";
        c.fill(255).text(text, x + 5, button.height / 2 + c.textPos(text).y);
    }
    
    private void drawItems(GContext c) {
        var list = getCurrentPageItems();
        c.fill(0).rect(this);
        for (int row = 0, i = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++, i++) {
                if (i < list.size()) {
                    list.get(i).draw(c, x + col * SIZE, y + row * SIZE+ SIZE);
                }
            }
        }
    }

    private void drawButton(GContext c) {
        String current = (this.index + 1) + " / " + pages;
        int[] prevColor = { this.index == 0 ? 100 : 0 }, nextColor = { this.index >= pages ? 100 : 0 };
        var textPos = c.textPos(current);
        int textY = button.height / 2 + textPos.y;
        c.save().clip(button).fill(255).rect(button).translate(button.x, button.y)
                .fill(prevColor).circle(5, button.height/2, 25)
                .fill(nextColor).circle(button.width - 5, button.height/2, 25)
                .fill(0).text(current, button.width / 2 + textPos.x, textY).restore();
    }

}
