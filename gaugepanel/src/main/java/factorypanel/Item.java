package factorypanel;

import java.awt.Rectangle;

import factorypanel.Mode.SEARCH;
import com.github.ennurluaf.*;

public class Item extends Rectangle {

    private static final long serialVersionUID = 1L;
    private final String idString;
    private final String name;
    private final Sprite sprite;
    public boolean hovered, selected;
    private final int id;

    public Item(GaugePanel.ItemData data, Sprite sprite, int id) {
        super(sprite.getRect());
        this.idString = data.id();
        this.name = data.name();
        this.sprite = sprite;
        this.id = id;
    }

    public String idString() {
        return idString;
    }

    public String name() {
        return name;
    }

    public Sprite sprite() {
        return sprite;
    }

    public int id() {
        return id;
    }

    public boolean matches(String query) {
        if (query.isEmpty()) return true;
        String lowerCaseQuery = query.toLowerCase();
        if (SEARCH.ID.is()) {
            return idString.toLowerCase().contains(lowerCaseQuery);
        } else if (SEARCH.MOD.is()) {
            String modId = idString.split(":")[0].toLowerCase();
            return modId.toLowerCase().contains(query.split(" ")[0]) &&
                   name.toLowerCase().contains(query.split(" ")[1]);
        } else {
            return name.toLowerCase().contains(lowerCaseQuery);
        }
    }

    public void draw(GContext c, int x, int y) {
        this.setBounds(sprite.getRect(x, y));
        sprite.draw(c, x, y);
        if (hovered) {
            c.fill(0, 50).rect(this);
        }
        if (selected) {
            float sw = 2f; // stroke width
            Rectangle r = new Rectangle((int)(x + sw/2),(int) (y + sw/2), (int)(width - sw), (int)(height - sw));
            c.setStrokeWidth(sw);
            c.stroke(0, 100).rect(r);
            c.setStrokeWidth(1);
        }

    }

    @Override
    public String toString() {
        return "Item{id='" + idString + ", name='" + name + '}';
    }

}
