package factorypanel;

import java.awt.Point;
import java.awt.Rectangle;
import static factorypanel.GaugePanel.SIZE;

import com.github.ennurluaf.*;

public class JEI extends Rectangle {

    private static final long serialVersionUID = 1L;
    private final JSList<Item> items;
    private final int cols, rows, pages;
    private int index = 0;
    private long clock = System.currentTimeMillis(), cd = 500; // cooldown in milliseconds
    private final Rectangle button;
    private String searchQuery = "";

    public JEI(int x, int y, int width, int height, JSList<Item> items) {
        super(x, y, width, height);
        this.items = items;
        this.cols = width / SIZE;
        this.rows = height / SIZE - 2;
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
            item.selected = item.contains(mouse) && !item.selected;
        });
    }

    public void move(Point mouse) {
        this.getCurrentPageItems().forEach(item -> {
            item.hovered = item.contains(mouse);
        });
    }

    private JSList<Item> getCurrentPageItems() {
        int start = index * cols * rows;
        int end = Math.min(start + cols * rows, items.size());
        return items.filter(item -> item.matches(searchQuery)).slice(start, end);
    }

    public void draw(GContext c) {
        c.fill(0).rect(this);
        drawName(c);
        drawItems(c);
        drawButton(c);
    }

    private void drawName(GContext c) {
        var selected = getCurrentPageItems().find(i -> i.selected);
        var hover = getCurrentPageItems().find(i -> i.hovered);
        String hoverText = hover != null ? hover.name() : "No item hovered";
        String text = selected != null ? selected.name() : hoverText;
        c.fill(255).text(text, x + 10, button.height / 2 + c.textPos(text).y);
    }

    private void drawItems(GContext c) {
        var list = getCurrentPageItems();
        for (int row = 0, i = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++, i++) {
                if (i < list.size()) {
                    list.get(i).draw(c, x + col * SIZE, y + row * SIZE + SIZE);
                }
            }
        }
    }

    private void drawButton(GContext c) {
        String current = (this.index + 1) + " / " + pages;
        int[] prevColor = { this.index == 0 ? 100 : 0 };
        int[] nextColor = { this.index >= pages ? 100 : 0 };
        var textPos = c.textPos(current, button);
        c.save().clip(button).fill(255).rect(button)
                .fill(0).text(current, textPos.x, textPos.y)
                .translate(button.x, button.y)
                .fill(prevColor).circle(0, button.height / 2, 26)
                .fill(nextColor).circle(button.width, button.height / 2, 26)
                .restore();
    }

}
