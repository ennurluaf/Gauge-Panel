package factorypanel;

import java.awt.Rectangle;

import code.Sprite;

public class Item extends Rectangle {

    private static final long serialVersionUID = 1L;
    private final String idString;
    private final String name;
    private final Sprite sprite;
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

}
