package factorypanel;

import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


public class GaugePanel {

    private record ItemData(String id, String name, String image){}

    public static void main(String[] args) throws Exception {
        // Try loading the JSON file
        InputStream is = GaugePanel.class.getClassLoader().getResourceAsStream("items.json");

        if (is == null) {
            System.err.println("items-test.json not found in classpath!");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        List<ItemData> items = mapper.readValue(is, new TypeReference<List<ItemData>>() {});


        System.out.println(items.size());
    }
}
