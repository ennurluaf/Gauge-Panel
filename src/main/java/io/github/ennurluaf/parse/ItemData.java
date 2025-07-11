package io.github.ennurluaf.parse;

public class ItemData {
    public String id, name, image;

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}
