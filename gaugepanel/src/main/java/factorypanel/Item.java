package factorypanel;

import java.awt.Rectangle;

import code.*;

public class Item extends Rectangle {

    private static final long serialVersionUID = 1L;
    private final String idString;
    private final String name;
    private final Sprite sprite;
    public static String searchMode = "";
    public boolean hovered, selected;
    private final int id;

    public Item(String idString, String name, Sprite sprite, int id) {
        super(sprite.getRect());
        this.idString = idString;
        this.name = name;
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
        if (searchMode.equals("id")) {
            return idString.toLowerCase().contains(lowerCaseQuery);
        } else if (searchMode.equals("mod")) {
            String modId = idString.split(":")[0].toLowerCase();
            return modId.toLowerCase().contains(query.split(" ")[0]) &&
                   name.toLowerCase().contains(query.split(" ")[1]);
        } else {
            return name.toLowerCase().contains(lowerCaseQuery);
        }
    }

    public void draw(GContext c, int x, int y) {
        this.setBounds(x,y, sprite.getWidth(), sprite.getHeight());
        sprite.draw(c, x, y);
        if (hovered) {
            c.fill(0, 50).rect(this);
        }
        if (selected) {
            c.stroke(0, 255).rect(this);  
        }

    }

    @Override
    public String toString() {
        return "Item{id='" + idString + ", name='" + name + '}';
    }

}
