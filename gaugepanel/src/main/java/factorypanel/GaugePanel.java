package factorypanel;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Box;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import factorypanel.GaugePanel.Mode.ModeProvider;

import code.*;

public class GaugePanel extends JPanel {

    public static enum Mode {
        PANEL("factory", "jei"),
        SEARCH("item", "mod", "id"),
        FACTORY("drag", "box"),
        BOX("add", "move", "edit", "remove");

        public static final Mode[] values = values();
        public String[] modes;
        public int index;

        private Mode(String... modes) {
            this.modes = modes;
            this.index = 0;
        }

        public interface ModeProvider {
            void apply(String mode);
        }

        public boolean is(String mode) {
            return modes[index].equals(mode);
        }
    }

    private record ItemData(String id, String name, String image) {
    }

    public static final int SIZE = 50;
    public JMenuBar menuBar = new JMenuBar();
    public JTextField searchField = new JTextField();
    private JSList<Item> items;
    private FactoryPanel factorypanel;
    private JEI jei;
    private static String panelMode = "factory";

    public GaugePanel() {
        try (InputStream is = loader().getResourceAsStream("minecraft/items.json")) {
            JSList<ItemData> data = new ObjectMapper().readValue(is, new TypeReference<JSList<ItemData>>() {
            });
            items = data.map((d, id) -> new Item(d.id, d.name, load(d.image).resize(SIZE, SIZE), id));
        } catch (IOException e) {
            e.printStackTrace();
        }

        factorypanel = new FactoryPanel(0, 0, (5 * 1200) / 8, 900);
        jei = new JEI((5 * 1200) / 8, 0, (3 * 1200) / 8, 900, items, SIZE);

        menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.X_AXIS));

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        JMenu modes = new JMenu("Modes");
        modes.add(createMenu(Mode.PANEL, m -> panelMode = m));
        modes.add(createMenu(Mode.SEARCH, m -> Item.searchMode = m));
        // modes.add(createMenu(Mode.BOX, m -> {}));
        menuBar.add(modes);

        menuBar.add(Box.createHorizontalGlue());

        searchField.setPreferredSize(new Dimension(450, 25));
        searchField.setMaximumSize(new Dimension(450, 25));
        searchField.setToolTipText("Search items...");
        searchField.addActionListener(e -> {
            String query = searchField.getText().toLowerCase();
            jei.search(query);
            repaint();
        });
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
                if (e.getButton() == MouseEvent.BUTTON2) { // Right-click to clear search
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

    private JMenu createMenu(Mode mode, ModeProvider provider) {
        String name = mode.name().substring(0,1) + mode.name().toLowerCase().substring(1);
        JMenu menu = new JMenu(name);
        for (int i = 0; i < mode.modes.length; i++) {
            String itemName = mode.modes[i].toUpperCase();
            JMenuItem menuItem = new JMenuItem(itemName);
            final int idx = i; // Final variable for lambda
            menuItem.addActionListener(e -> {
                mode.index = idx;
                provider.apply(itemName); // This sets the static variable!
                menu.setText(name + " - " + itemName);
                repaint();
            });
            menu.add(menuItem);
        }
        return menu;
    }

    private static Sprite load(String path) {
        try (InputStream is = loader().getResourceAsStream("minecraft/textures/" + path)) {
            return new Sprite(ImageIO.read(is));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ClassLoader loader() {
        return GaugePanel.class.getClassLoader();
    }

    private MouseAdapter mouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (factorypanel.contains(e.getPoint())) {
                    panelMode = "factory";
                    factorypanel.press(e.getPoint());
                } else {
                    panelMode = "jei";
                    jei.press(e.getPoint());
                }
                repaint();
            }

            public void mouseMoved(java.awt.event.MouseEvent e) {
                if (Mode.PANEL.is("factory")) {
                    factorypanel.move(e.getPoint());
                } else {
                    jei.move(e.getPoint());
                }
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

        c.stroke(0, Mode.PANEL.index == 0 ? 255 : 0).rect(factorypanel);
        c.stroke(0, Mode.PANEL.index == 1 ? 255 : 0).rect(jei);
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
