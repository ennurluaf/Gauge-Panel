package factorypanel;

import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import parse.ItemData;


public class GaugePanel {
    public static void main(String[] args) throws Exception {
        // Try loading the JSON file
        InputStream is = GaugePanel.class.getClassLoader().getResourceAsStream("items-test.json");

        if (is == null) {
            System.err.println("items-test.json not found in classpath!");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        List<ItemData> items = mapper.readValue(is, new TypeReference<List<ItemData>>() {});


        for (ItemData item : items) {
            System.out.println(item.name + " -> " + item.id);
        }
    }
}
