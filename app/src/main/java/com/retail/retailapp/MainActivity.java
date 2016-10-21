package com.retail.retailapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.retail.retailapp.dataloader.GroceryItemLoader;
import com.retail.retailapp.dataloader.GroceryItemLoaderImpl;
import com.retail.retailapp.util.GroceryConstant;
import com.retail.retailapp.util.GroceryUtil;
import com.retail.retailapp.vo.GroceryItem;

import org.json.JSONException;


import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    Spinner categoryspinner, itemlistspinner, weightspinner;
    Button addButton;
    LinearLayout content;
    RelativeLayout rlayout;
    TableLayout tblLayout;
    Map<String, List<GroceryItem>> groceryMap;
    Map<String, List<GroceryItem>> selectedMap;
    Map<String, List<String>> quantityMap;

    Map<String, Map<String, Object>> masterMap;

    GroceryItemLoader itemloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedMap = new HashMap<>();
        itemloader = new GroceryItemLoaderImpl(getApplicationContext());
        try {
            masterMap = itemloader.load(GroceryConstant.GROCERYITEMS_JSON);
            groceryMap = (Map) masterMap.get(GroceryConstant.GROCERY_MAP);
            quantityMap = (Map) masterMap.get(GroceryConstant.QUANTITY_MAP);
            System.out.println("grocery map :=" + groceryMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);
        addListenerOnSpinnerCategorySelection();
        addListenerOnSpinnerItemSelection();
        init();
        addListenerOnAddButton();
    }


    private void addListenerOnAddButton() {
        this.addButton = (Button) this.findViewById(R.id.add);
        this.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                content=(LinearLayout) findViewById(R.id.contentLayout);
                int rowNumber = tblLayout.getChildCount();
                TableRow tbrow = new TableRow(MainActivity.this);
                TextView t1v = new TextView(MainActivity.this);
                t1v.setText("" + rowNumber);
                t1v.setTextColor(Color.BLACK);
                t1v.setGravity(Gravity.CENTER);
                tbrow.addView(t1v);

                String category = categoryspinner.getItemAtPosition(categoryspinner.getSelectedItemPosition()).toString();

                TextView textView1 = new TextView(getApplicationContext());
                String itemValue = itemlistspinner.getItemAtPosition(itemlistspinner.getSelectedItemPosition()).toString();
                textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                textView1.setTextColor(Color.BLACK);
                textView1.setText(itemValue);
                textView1.setPadding(1, 1, 1, 1);
                tbrow.addView(textView1);

                TextView textView2 = new TextView(getApplicationContext());
                String quantity = weightspinner.getItemAtPosition(weightspinner.getSelectedItemPosition()).toString();
                textView2.setText(quantity);
                textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                textView2.setTextColor(Color.BLACK);
                textView2.setPadding(1, 1, 1, 1);
                tbrow.addView(textView2);

                TextView textView3 = new TextView(getApplicationContext());
                double itemVal = GroceryUtil.getPrice(groceryMap, category, itemValue, quantity).doubleValue();
                textView3.setText(new DecimalFormat("#.00").format(itemVal));
                textView3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                textView3.setTextColor(Color.BLACK);
                textView3.setGravity(Gravity.RIGHT);
                textView3.setPadding(1, 1, 1, 1);
                tbrow.addView(textView3);

                Button btn = new Button(MainActivity.this);
                btn.setText("X");
                btn.setId(rowNumber);
                TableRow.LayoutParams tlp = new TableRow.LayoutParams(100,52);
                btn.setPadding(1,0,1,2);
                btn.setLayoutParams(tlp);

                if (selectedMap.get(category) != null) {
                    List<GroceryItem> items = selectedMap.get(category);
                    items.add(new GroceryItem(itemValue,itemVal,quantity));
                } else {
                    List<GroceryItem> newList = new ArrayList<GroceryItem>();
                    newList.add(new GroceryItem(itemValue,itemVal,quantity));
                    selectedMap.put(category,newList);
                }


                btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // row is your row, the parent of the clicked button
                        View row = (View) v.getParent();
                        // container contains all the rows, you could keep a variable somewhere else to the container which you can refer to here
                        ViewGroup container = ((ViewGroup)row.getParent());
                        // delete the row and invalidate your view so it gets redrawn
                        container.removeView(row);
                        container.invalidate();
                    }

                });
                tbrow.addView(btn);

                tblLayout.addView(tbrow);


            }

        });

    }


    public void init() {
        tblLayout = (TableLayout) findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText(GroceryConstant.SL_NO);
        tv0.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv0.setTextColor(Color.BLACK);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText(GroceryConstant.PRODUCT);
        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv1.setTextColor(Color.BLACK);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(GroceryConstant.UNIT);
        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv2.setTextColor(Color.BLACK);
        tbrow0.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText(GroceryConstant.PRICE);
        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv3.setTextColor(Color.BLACK);
        tbrow0.addView(tv3);

        Button btn = new Button(MainActivity.this);
        TableRow.LayoutParams tlp = new TableRow.LayoutParams(120,56);
        btn.setPadding(10,7,0,2);
        btn.setGravity(Gravity.TOP);
        btn.setLayoutParams(tlp);
        btn.setText("X-All");
        btn.setId(0);

//        btn.setLayoutParams(layoutParams);
        tbrow0.addView(btn);


        tblLayout.addView(tbrow0);
    }

    public void addListenerOnSpinnerCategorySelection() {

        categoryspinner = (Spinner) findViewById(R.id.category);

        categoryspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                itemlistspinner = (Spinner) findViewById(R.id.list);
                String categoryName = categoryspinner.getItemAtPosition(categoryspinner.getSelectedItemPosition()).toString();

                List<String> items = GroceryUtil.getItemList(groceryMap, categoryName);
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, items);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                itemlistspinner.setAdapter(adapter1);

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
                List<String> quantityList = GroceryUtil.getQuantityList(groceryMap, quantityMap, categoryName, itemName);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, quantityList);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                weightspinner.setAdapter(adapter2);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                return;

            }
        });
    }


    /** Called when the user clicks the Send button */
    public void saveItems(View view) {
        Intent intent = new Intent(this, ShoppingListActivity.class);
        intent.putExtra("shoppinglist", (Serializable) selectedMap);
        startActivity(intent);
        // Do something in response to button
    }




}
