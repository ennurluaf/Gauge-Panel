package code;

import java.awt.Point;
import java.awt.Rectangle;

public interface Size {

    public int getWidth();
    public int getHeight();

    public static <Rect extends Rectangle> Point get(Rect rect, Pos pos) {
        return pos.posCalc.getPos(rect.x, rect.y, rect.width, rect.height).getLocation();
    }

    public static enum Pos {
        SIZE((x, y, w, h) -> rect(0, 0, w, h)),
        TOPLEFT((x, y, w, h) -> rect(x, y, w, h)),
        TOPRIGHT((x, y, w, h) -> rect(x + w, y, w, h)),
        BOTTOMLEFT((x, y, w, h) -> rect(x, y + h, w, h)),
        BOTTOMRIGHT((x, y, w, h) -> rect(x + w, y + h, w, h)),
        CENTER((x, y, w, h) -> rect(x + w / 2, y + h / 2, w, h));
        
        public PosFunction posCalc;
        
        private Pos(PosFunction func) {
            this.posCalc = func;
        }
    
        public interface PosFunction {
            Rectangle getPos(int x, int y, int w, int h);
    
        }

        private static Rectangle rect(int x, int y, int w, int h) {
            return new Rectangle(x, y, w, h);
        }

        public Rectangle calcPos(Size sprite, int x, int y) {
            return posCalc.getPos(x, y, sprite.getWidth(), sprite.getHeight());
        }
    }

}
