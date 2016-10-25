package com.retail.retailapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.retail.retailapp.util.GroceryConstant;
import com.retail.retailapp.vo.GroceryItem;
import com.retail.retailapp.vo.PurchaseItem;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ShoppingListActivity extends AppCompatActivity {

    TableLayout tblLayout;

    Spinner categoryspinner, itemlistspinner, weightspinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        Intent caller = getIntent();
        Map<String, List<PurchaseItem>> data_map = (Map<String, List<PurchaseItem>>)caller.getExtras().get("shoppinglist");
        init_table_layout();
        load_table(data_map);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void load_table(Map<String, List<PurchaseItem>> data_map) {

        Iterator it = data_map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            List<PurchaseItem> list = (List)pair.getValue();
            for (PurchaseItem purchaseItem : list) {

                TableRow tbrow = new TableRow(this);

                CheckBox box= new CheckBox(this);
                tbrow.addView(box);

                TextView textView1 = new TextView(getApplicationContext());
                textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                textView1.setTextColor(Color.BLACK);
                textView1.setText(purchaseItem.getName());
                textView1.setPadding(1, 1, 1, 1);
                tbrow.addView(textView1);


                TextView tv2 = new TextView(getApplicationContext());
                tv2.setText(purchaseItem.getQuantity());
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tv2.setTextColor(Color.BLACK);
                tbrow.addView(tv2);

                TextView tv3 = new TextView(this);
                if (purchaseItem.getItemPrice() == null) {
                    tv3.setText("0.00");
                } else {
                    tv3.setText(new DecimalFormat("#.00").format(purchaseItem.getItemPrice()));
                }
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tv3.setTextColor(Color.BLACK);
                tv3.setGravity(Gravity.RIGHT);
                tbrow.addView(tv3);

                Button btn = new Button(this);
                btn.setText("X");
//                btn.setId(rowNumber);
                TableRow.LayoutParams tlp = new TableRow.LayoutParams(100,52);
                btn.setPadding(1,0,1,2);
                btn.setLayoutParams(tlp);

                tbrow.addView(btn);

                tblLayout.addView(tbrow);

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

            }

        }



    }

    public void init_table_layout() {
        tblLayout = (TableLayout) findViewById(R.id.table_shopping_list);
        TableRow tbrow0 = new TableRow(this);

        CheckBox box= new CheckBox(this);
        tbrow0.addView(box);

        TextView tv1 = new TextView(this);
        tv1.setText(GroceryConstant.PRODUCT_SPACE);
        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv1.setTextColor(Color.BLACK);
        tbrow0.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setText(GroceryConstant.UNIT_LESS_SPACE);
        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv2.setTextColor(Color.BLACK);
        tbrow0.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText(GroceryConstant.PRICE_SPACE);
        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv3.setTextColor(Color.BLACK);
        tbrow0.addView(tv3);

        Button btn = new Button(this);
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
}
