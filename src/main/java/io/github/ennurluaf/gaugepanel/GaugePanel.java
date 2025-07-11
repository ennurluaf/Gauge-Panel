package io.github.ennurluaf.gaugepanel;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
        
        // ✅ Declare Gson instance BEFORE using it
        Gson gson = new Gson();

        // ✅ Define the type for List<ItemData>
        Type itemListType = new TypeToken<List<ItemData>>() {}.getType();

        List<ItemData> items = gson.fromJson(reader, itemListType);

        for (ItemData item : items) {
            System.out.println(item.name + " -> " + item.id);
        }
    }
}
