package com.retail.retailapp.dataloader;

import android.content.Context;

import com.retail.retailapp.MainActivity;
import com.retail.retailapp.vo.GroceryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by raghuramankumarasamy on 20/10/16.
 *
 */
public class GroceryItemLoaderImpl implements GroceryItemLoader {

    private Context mCtx;

    Map<String,Map<String,Object>> masterMap;


    public GroceryItemLoaderImpl(Context mCtx) {
        this.mCtx = mCtx;
        masterMap = new HashMap<>();
    }

    @Override
    public Map<String, Map<String,Object>> load(String filename) throws JSONException {
        List<GroceryItem> items;
        List<String> quantityValues;
        List<String> weightList;

        Map<String,Object> groceryMap = new HashMap<>();
        Map<String,Object> quantityMap=new HashMap<>();

        JSONObject obj = new JSONObject(loadJSONFromAsset());
        JSONArray jsonArray = obj.getJSONArray("grocery");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj1 = jsonArray.getJSONObject(i);
            String category=(String) obj1.get("category");
            JSONArray itemlist =  obj1.getJSONArray("itemlist");
            items= new ArrayList<>();
            for (int j = 0; j < itemlist.length(); j++) {
                JSONObject element = itemlist.getJSONObject(j);
                GroceryItem item = new GroceryItem(element.getString("name"),element.getDouble("price"),element.getString("type"));
                items.add(item);
            }
            groceryMap.put(category,items);
        }
        quantityMap = new HashMap<>();
        JSONArray quantityArray = obj.getJSONArray("quantity");
        for (int i = 0; i < quantityArray.length(); i++) {
            JSONObject obj1 = quantityArray.getJSONObject(i);
            String quantityType=(String) obj1.get("type");
            JSONArray valuelist =  obj1.getJSONArray("value");
            quantityValues= new ArrayList<>();
            for (int k = 0; k < valuelist.length(); k++) {
                quantityValues.add(valuelist.getString(k));
            }
            quantityMap.put(quantityType,quantityValues);
        }

        masterMap.put("GroceryMap", groceryMap);
        masterMap.put("QuantityMap", quantityMap);
        return masterMap;

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = mCtx.getAssets().open("groceryitems.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }




}
