package com.retail.retailapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
import com.retail.retailapp.vo.PurchaseItem;

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
    Map<String, List<PurchaseItem>> selectedMap;
    Map<String, List<String>> quantityMap;
    private Typeface myFont;

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
        /* Set the Font Verdana for Category */
        TextView tv = (TextView) findViewById(R.id.category_lbl);
        Typeface face = Typeface.createFromAsset(getAssets(), GroceryConstant.FONTS_VERDANA_TTF);
        tv.setTypeface(face, Typeface.NORMAL);
        loadCategories(groceryMap); //load the categories spinner
        addListenerOnSpinnerCategorySelection(); //event listener for Categories spinner
        addListenerOnSpinnerItemSelection(); //event listener for Items spinner
        init();  //Initialize the table layout with the headings
        addListenerOnAddButton(); //Listener for the add button
    }


    private void loadCategories(Map<String, List<GroceryItem>> groceryMap) {
        categoryspinner = (Spinner) findViewById(R.id.category);
        List<String> categories = GroceryUtil.getCategories(groceryMap);//get the sorted spinner list categories
        MySpinnerAdapter adapter = new MySpinnerAdapter(
                MainActivity.this,
                R.layout.spinner_item_text,
                categories
        );
        categoryspinner.setAdapter(adapter);
    }


    private void addListenerOnAddButton() {
        this.addButton = (Button) this.findViewById(R.id.add);

        this.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                content=(LinearLayout) findViewById(R.id.contentLayout);
                Typeface face = Typeface.createFromAsset(getAssets(), GroceryConstant.FONTS_VERDANA_TTF);

                int rowNumber = tblLayout.getChildCount();
                TableRow tbrow = new TableRow(MainActivity.this);
                TextView t1v = new TextView(MainActivity.this);
                t1v.setText("" + rowNumber);
                t1v.setTextColor(Color.BLACK);
                t1v.setGravity(Gravity.CENTER);
                t1v.setTypeface(face);
                tbrow.addView(t1v);

                String category = categoryspinner.getItemAtPosition(categoryspinner.getSelectedItemPosition()).toString();

                TextView textView1 = new TextView(getApplicationContext());
                String productName = itemlistspinner.getItemAtPosition(itemlistspinner.getSelectedItemPosition()).toString();
                textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                textView1.setTextColor(Color.BLACK);
                textView1.setText(productName);
                textView1.setPadding(1, 1, 1, 1);
                textView1.setTypeface(face);
                tbrow.addView(textView1);

                TextView textView2 = new TextView(getApplicationContext());
                String quantity = weightspinner.getItemAtPosition(weightspinner.getSelectedItemPosition()).toString();
                textView2.setText(quantity);
                textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                textView2.setTextColor(Color.BLACK);
                textView2.setPadding(1, 1, 1, 1);
                textView2.setTypeface(face);
                tbrow.addView(textView2);

                TextView textView3 = new TextView(getApplicationContext());
                GroceryItem gItem = GroceryUtil.getUnitPrice(groceryMap, category, productName);
                Double unitPrice = gItem.getPrice();
                String type = gItem.getType();
                double productPrice = GroceryUtil.getPrice(groceryMap, category, productName, quantity).doubleValue();
                textView3.setText(new DecimalFormat("#.00").format(productPrice));
                textView3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                textView3.setTextColor(Color.BLACK);
                textView3.setGravity(Gravity.RIGHT);
                textView3.setPadding(1, 1, 1, 1);
                textView3.setTypeface(face);
                tbrow.addView(textView3);

                Button btn = new Button(MainActivity.this);
                btn.setText("X");
                btn.setId(rowNumber);
                TableRow.LayoutParams tlp = new TableRow.LayoutParams(100, 52);
                btn.setPadding(1, 0, 1, 2);
                btn.setLayoutParams(tlp);

                if (selectedMap.get(category) != null) {
                    List<PurchaseItem> items = selectedMap.get(category);
                    items.add(new PurchaseItem(productName, unitPrice, productPrice, quantity, type));
                } else {
                    List<PurchaseItem> newList = new ArrayList<>();
                    newList.add(new PurchaseItem(productName, unitPrice, productPrice, quantity, type));
                    selectedMap.put(category, newList);
                }

                btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // row is your row, the parent of the clicked button
                        View row = (View) v.getParent();
                        // container contains all the rows, you could keep a variable somewhere else to the container which you can refer to here
                        ViewGroup container = ((ViewGroup) row.getParent());
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
        Typeface face = Typeface.createFromAsset(getAssets(), GroceryConstant.FONTS_VERDANA_TTF);
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText(GroceryConstant.SL_NO);
        tv0.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv0.setTextColor(Color.BLACK);
        tv0.setTypeface(face);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText(GroceryConstant.PRODUCT);
        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv1.setTextColor(Color.BLACK);
        tv1.setTypeface(face);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(GroceryConstant.UNIT);
        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv2.setTextColor(Color.BLACK);
        tv2.setTypeface(face);
        tbrow0.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText(GroceryConstant.PRICE);
        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv3.setTextColor(Color.BLACK);
        tv3.setTypeface(face);
        tbrow0.addView(tv3);

        Button btn = new Button(MainActivity.this);
        TableRow.LayoutParams tlp = new TableRow.LayoutParams(120, 56);
        btn.setPadding(10, 7, 0, 2);
        btn.setGravity(Gravity.TOP);
        btn.setLayoutParams(tlp);
        btn.setText(" X-All");
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
                MySpinnerAdapter adapter = new MySpinnerAdapter(
                        MainActivity.this,
                        R.layout.spinner_item_text,
                        items
                );
                itemlistspinner.setAdapter(adapter);

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
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(MainActivity.this, R.layout.spinner_item_text, quantityList);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                weightspinner.setAdapter(adapter2);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                return;

            }
        });
    }


    /**
     * Called when the user clicks the Send button
     */
    public void saveItems(View view) {
        Intent intent = new Intent(this, ShoppingListActivity.class);
        intent.putExtra("shoppinglist", (Serializable) selectedMap);
        startActivity(intent);
        // Do something in response to button
    }



    /* This is only for setting the font inside the spinner -- Font Verdana from assets folder */

    private static class MySpinnerAdapter extends ArrayAdapter<String> {
        // Initialise custom font, for example:
        Typeface font = Typeface.createFromAsset(getContext().getAssets(),
                GroceryConstant.FONTS_VERDANA_TTF);

        // (In reality I used a manager which caches the Typeface objects)
        // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

        private MySpinnerAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTypeface(font);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setTypeface(font);
            return view;
        }
    }


}
