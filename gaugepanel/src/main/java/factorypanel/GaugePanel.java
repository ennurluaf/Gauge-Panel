package factorypanel;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

// import java.io.InputStream;
// import java.util.List;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.core.type.TypeReference;


public class GaugePanel extends JPanel{

    private record ItemData(String id, String name, String image){}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Factory Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        GaugePanel panel = new GaugePanel();
        panel.setPreferredSize(new Dimension(1200, 900));
        panel.setBackground(Color.RED);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // public static void main(String[] args) throws Exception {
    //     // Try loading the JSON file
    //     InputStream is = GaugePanel.class.getClassLoader().getResourceAsStream("items.json");

    //     if (is == null) {
    //         System.err.println("items-test.json not found in classpath!");
    //         return;
    //     }

    //     ObjectMapper mapper = new ObjectMapper();
    //     List<ItemData> items = mapper.readValue(is, new TypeReference<List<ItemData>>() {});


    //     System.out.println(items.size());
    // }
}
