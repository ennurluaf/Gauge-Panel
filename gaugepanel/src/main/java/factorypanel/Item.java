package factorypanel;

import java.awt.Rectangle;

import factorypanel.Mode.SEARCH;
import static factorypanel.GaugePanel.SIZE;
import code.*;

public class Item extends Rectangle {

    private static final long serialVersionUID = 1L;
    private final String idString;
    private final String name;
    private final Sprite sprite;
    public boolean hovered, selected;
    private final int id;

    public Item(GaugePanel.ItemData data, Sprite sprite, int id) {
        super(0, 0, SIZE, SIZE);
        this.idString = data.id();
        this.name = data.name();
        this.sprite = sprite.resize(SIZE, SIZE);
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
            c.save();
            // c.setStrokeWidth(3);
            c.stroke(0, 255).rect(this);
            c.restore();  
        }

    }

    @Override
    public String toString() {
        return "Item{id='" + idString + ", name='" + name + '}';
    }

}
