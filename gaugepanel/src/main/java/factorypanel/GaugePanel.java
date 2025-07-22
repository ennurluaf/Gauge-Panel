package factorypanel;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import code.*;

public class GaugePanel extends JPanel {

    private record ItemData(String id, String name, String image) {
    }

    public JMenuBar menuBar = new JMenuBar();
    public JTextField searchField = new JTextField();
    private JSArray<Item> items;
    private Rectangle factorypanel;
    private JEI jei;

    public GaugePanel() {
        try (InputStream is = loader().getResourceAsStream("minecraft/items.json")) {
            JSArray<ItemData> data = new ObjectMapper().readValue(is, new TypeReference<JSArray<ItemData>>() {});
            items = data.map((d, id) -> new Item(d.id, d.name, load(d.image).resize(50, 50), id));
        } catch (IOException e) {
            e.printStackTrace();
        }

        factorypanel = new Rectangle(0, 0, (5*1200)/8, 900);
        jei = new JEI((5*1200)/8, 0, (3*1200)/8, 900, items, 50);

        menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.X_AXIS));    
        
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);      

        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

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
        addButton.addActionListener(e -> {});
        menuBar.add(addButton); 


        addMouseListener(mouseAdapter());
        addMouseMotionListener(mouseAdapter());
    }

    private static Sprite load(String path) {
        try(InputStream is = loader().getResourceAsStream("minecraft/textures/" + path)) {
            return new Sprite(ImageIO.read(is));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ClassLoader loader() {
        return GaugePanel.class.getClassLoader();
    }

    private MouseAdapter mouseAdapter(){
        return new MouseAdapter() {
        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            jei.press(e.getPoint());
            repaint();
        }

        public void mouseMoved(java.awt.event.MouseEvent e) {
            jei.move(e.getPoint());
            repaint();
        };
    };
}


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        GContext c = new GContext(g);

        c.fill(255,0,0,100).rect(factorypanel);
        // c.fill(0,0,255,100).rect(jei);

        jei.draw(c);
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
