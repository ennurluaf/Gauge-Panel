package factorypanel;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Box;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.ennurluaf.*;
import factorypanel.Mode.*;

public class GaugePanel extends JPanel {

    public static record ItemData(String id, String name, String image) {}

    public static final int SIZE = 50;
    public JMenuBar menuBar = new JMenuBar();
    private JSList<Item> items;
    private FactoryPanel factorypanel;
    private JEI jei;

    public GaugePanel() {
        items = getItemDatas().map((d, id) -> new Item(d, load(d.image), id));

        factorypanel = new FactoryPanel(0, 0, (5 * 1200) / 8, 900);
        jei =          new JEI((5 * 1200) / 8, 0, (3 * 1200) / 8, 900, items);

        menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.X_AXIS));

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        JMenu modes = new JMenu("Modes");
        Modes.list.forEach(m -> modes.add(m.createMenu()));
        menuBar.add(modes);

        menuBar.add(Box.createHorizontalGlue());

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(550, 25));
        searchField.setMaximumSize(new Dimension(550, 25));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String query = searchField.getText().toLowerCase();
                jei.search(query);
                repaint();
            }
        });
        searchField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) { // Right-click to clear search
                    searchField.setText("");
                    jei.search("");
                }
                repaint();
            }
        });
        menuBar.add(searchField);

        JButton addButton = new JButton("+");
        addButton.addActionListener(e -> {
        });
        menuBar.add(addButton);

        addMouseListener(mouseAdapter());
        addMouseMotionListener(mouseAdapter());
    }

    private static Sprite load(String path) {
        try (InputStream is = loader("minecraft/textures/" + path)) {
            return new Sprite(ImageIO.read(is), SIZE, SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static InputStream loader(String path) {
        return GaugePanel.class.getClassLoader().getResourceAsStream(path);
    }

    private static JSList<ItemData> getItemDatas() {
        try (InputStream is = loader("minecraft/items.json")) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(is, new TypeReference<JSList<ItemData>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSList<>();
    }            

    private MouseAdapter mouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (factorypanel.contains(e.getPoint())) {
                    SIDE.FACTORY.setMode();
                    factorypanel.press(e.getPoint());
                } else {
                    SIDE.JEI.setMode();
                    jei.press(e.getPoint());
                }
                repaint();
            }

            public void mouseMoved(java.awt.event.MouseEvent e) {
                factorypanel.move(e.getPoint());
                jei.move(e.getPoint());
                repaint();
            };

            @Override
            public void mouseDragged(MouseEvent e) {
                factorypanel.drag(e.getPoint());
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                factorypanel.release();
                repaint();
            }
        };
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        GContext c = new GContext(g);

        factorypanel.draw(c);
        jei.draw(c);

        c.stroke(0, SIDE.FACTORY.is() ? 255 : 0).rect(factorypanel);
        c.stroke(0, SIDE.JEI.is() ? 255 : 0).rect(jei);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Factory Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        GaugePanel panel = new GaugePanel();
        panel.setPreferredSize(new Dimension(1200, 900));
        panel.setBackground(Color.lightGray);
        frame.setJMenuBar(panel.menuBar);
        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
