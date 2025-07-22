package factorypanel;

import java.awt.Rectangle;

import code.*;

public class Item extends Rectangle {

    private static final long serialVersionUID = 1L;
    private final String idString;
    private final String name;
    private final Sprite sprite;
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
        return "Item{" +
                "idString='" + idString + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

}
