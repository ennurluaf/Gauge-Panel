package io.github.ennurluaf.gaugepanel;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.github.ennurluaf.parse.ItemData;

/**
 * Hello world!
 *
 */
public class GaugePanel 
{
    public static void main( String[] args ) throws Exception
    {
        try(InputStream is = GaugePanel.class.getClassLoader().getResourceAsStream("items-test.json")){

            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(is);
            List<ItemData> items = gson.fromJson(reader, ItemData.class); 
    
            for (ItemData item : items) {
                System.out.println(item.name + " -> " + item.id);
            }
        }catch(FileNotFoundException e){
            
        }
    }
}
