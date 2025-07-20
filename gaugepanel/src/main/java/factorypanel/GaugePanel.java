package factorypanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import code.GContext;
import code.JSArray;
import code.Sprite;

public class GaugePanel extends JPanel {

    private record ItemData(String id, String name, String image) {
    }

    public static record Item(String idString, String name, Sprite sprite, int id) {
    }

    public JTextField searchField = new JTextField();
    private JSArray<Item> items;
    private Rectangle factorypanel, jei;

    public GaugePanel() {
        try (InputStream is = loader().getResourceAsStream("create/items.json")) {
            JSArray<ItemData> data = new ObjectMapper().readValue(is, new TypeReference<JSArray<ItemData>>() {});
            items = data.map((d, id) -> new Item(d.id, d.name, load(d.image).resize(50, 50), id));
        } catch (IOException e) {
            e.printStackTrace();
        }

        factorypanel = new Rectangle(0, 0, (5*1200)/8, 900);
        jei = new Rectangle((5*1200)/8, 0, (3*1200)/8, 900);
    }

    private static Sprite load(String path) {
        try(InputStream is = loader().getResourceAsStream("create/textures/" + path)) {
            return new Sprite(ImageIO.read(is));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ClassLoader loader() {
        return GaugePanel.class.getClassLoader();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        GContext c = new GContext(g);

        c.fill(255,0,0,100).rect(factorypanel);
        c.fill(0,0,255,100).rect(jei);

        int cols = jei.width / 55, rows = getHeight() / 50;
        for (int row = 0, index = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++, index++) {
                if (index < items.size()) {
                    items.get(index).sprite().draw(c, jei.x + col * 55 + 5, row * 55 + 5);
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
        panel.setBackground(Color.lightGray);
        panel.searchField.setPreferredSize(new Dimension(1200, 30));
        frame.add(panel.searchField, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
