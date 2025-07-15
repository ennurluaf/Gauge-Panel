package factorypanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import code.GContext;
import code.JSArray;
import code.Sprite;

public class GaugePanel extends JPanel{

    private record ItemData(String id, String name, String image){}

    public static record Item(String id, String name, Sprite sprite) {

        public void draw(GContext c, int x, int y){
            sprite.draw(c, x, y, 50, 50);
        }
    }

    private JSArray<Item> items;

    public GaugePanel() {
        InputStream is = GaugePanel.class.getClassLoader().getResourceAsStream("items.json");
        JSArray<ItemData> data = new JSArray<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            data.addAll(mapper.readValue(is, new TypeReference<List<ItemData>>() {}));
        } catch (IOException e) {
            e.printStackTrace();
        }
        items = new JSArray<>(data.map(d -> new Item(d.id, d.name, Sprite.load(d.image))));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        GContext c = new GContext(g);
        int cols = getWidth()/50, rows = getHeight()/50;
        for (int row = 0, index = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++, index++) {
                if (index < items.size()) {
                    items.get(index).draw(c, col*55, row*55);
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Factory Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        GaugePanel panel = new GaugePanel();
        panel.setPreferredSize(new Dimension(1200, 900));
        panel.setBackground(Color.LIGHT_GRAY);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
