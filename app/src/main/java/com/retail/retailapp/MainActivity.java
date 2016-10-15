package com.retail.retailapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import android.widget.AdapterView.OnItemSelectedListener;

import com.retail.retailapp.com.retail.retailapp.vo.GroceryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Spinner categoryspinner, itemlistspinner, weightspinner;
    Map<String,List<GroceryItem>> groceryMap;
    Map<String,List<String>> quantityMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        List<GroceryItem> items;
        List<String> quantityValues;
        List<String> weightList;
        try {
            groceryMap = new HashMap<>();
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
//            JSONArray jsonArray2 = obj.getJSONArray("weight");
//            weightList = new ArrayList<>();
//            for (int k = 0; k < jsonArray2.length(); k++) {
//                weightList.add(jsonArray2.getString(k));
//            }
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


            System.out.println("grocery map :=" + groceryMap);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);
        addListenerOnSpinnerCategorySelection();
        addListenerOnSpinnerItemSelection();
//

    }

    private List<String> getItemList(String name) {
        List<GroceryItem> items = groceryMap.get(name);
        List<String> list=new ArrayList<>();
        for (GroceryItem gi: items) {
            String itemName = gi.getName();
            list.add(itemName);
        }
        return list;
    }

    private List<String> getQuantityList(String CategoryName,String itemname) {
        List<String> quantityList = null;
        List<GroceryItem> items = groceryMap.get(CategoryName);
        for (GroceryItem gi: items) {
            if (gi.getName().equals(itemname)) {
                quantityList = quantityMap.get(gi.getType());
            }
        }
        return quantityList;
    }

    public void addListenerOnSpinnerCategorySelection() {

        categoryspinner = (Spinner) findViewById(R.id.category);

        categoryspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                itemlistspinner = (Spinner) findViewById(R.id.list);
                String categoryName = categoryspinner.getItemAtPosition(categoryspinner.getSelectedItemPosition()).toString();

                List<String> items = getItemList(categoryName);
                ArrayAdapter<String> adapter1 =  new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, items);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                itemlistspinner.setAdapter(adapter1);

//                weightspinner = (Spinner) findViewById(R.id.weight);
//                List<String> quantityList = getQuantityList(categoryName);
//                ArrayAdapter<String> adapter2 =  new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, quantityList);
//                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                weightspinner.setAdapter(adapter2);

                System.out.println("Selected value :=" + categoryName);
                System.out.println("Id is :=" + id);

            }
            public void onNothingSelected(AdapterView<?> parent) {
                return;

            }
        });
    }


    public void addListenerOnSpinnerItemSelection() {

        itemlistspinner = (Spinner) findViewById(R.id.list);

        itemlistspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                String itemName = itemlistspinner.getItemAtPosition(itemlistspinner.getSelectedItemPosition()).toString();
                String categoryName = categoryspinner.getItemAtPosition(categoryspinner.getSelectedItemPosition()).toString();



                weightspinner = (Spinner) findViewById(R.id.weight);
                List<String> quantityList = getQuantityList(categoryName,itemName);
                ArrayAdapter<String> adapter2 =  new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, quantityList);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                weightspinner.setAdapter(adapter2);

            }
            public void onNothingSelected(AdapterView<?> parent) {
                return;

            }
        });
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("groceryitems.json");
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
