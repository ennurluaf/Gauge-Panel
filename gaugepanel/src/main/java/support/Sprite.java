package support;

import java.awt.image.BufferedImage;

public class Sprite extends BufferedImage{

    public Sprite(int width, int height) {
        super(width, height, BufferedImage.TYPE_INT_RGB);
        
    }

    public static Sprite load(String path) {
        return null;
    }

    public void draw(GContext c, int x, int y){
        c.drawImage(this, x, y);
    }

}
