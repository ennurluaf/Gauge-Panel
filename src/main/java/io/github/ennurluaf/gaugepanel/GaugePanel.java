package io.github.ennurluaf.gaugepanel;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import io.github.ennurluaf.parse.ItemData;


public class GaugePanel {
    public static void main(String[] args) throws Exception {
        // Try loading the JSON file
        InputStream is = GaugePanel.class.getClassLoader().getResourceAsStream("item-test.json");

        if (is == null) {
            System.err.println("item-test.json not found in classpath!");
            return;
        }

        InputStreamReader reader = new InputStreamReader(is);
        
        ObjectMapper mapper = new ObjectMapper();
List<ItemData> items = mapper.readValue(is, new TypeReference<List<ItemData>>() {});


        for (ItemData item : items) {
            System.out.println(item.name + " -> " + item.id);
        }
    }
}
