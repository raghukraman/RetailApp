package com.retail.retailapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.retail.retailapp.util.GroceryConstant;
import com.retail.retailapp.vo.PurchaseItem;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ShoppingListActivity extends AppCompatActivity {

    TableLayout tblLayout;
    TextView totalAmountText;
    CheckBox productSpaceCheckBox;
    double totalPrice = 0;
    Spinner categoryspinner, itemlistspinner, weightspinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        Intent caller = getIntent();

        Map<String, List<PurchaseItem>> data_map = (Map<String, List<PurchaseItem>>) caller.getExtras().get("shoppinglist");
        init_table_layout();
        load_table(data_map);

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void load_table(Map<String, List<PurchaseItem>> data_map) {


        Iterator it = data_map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            List<PurchaseItem> list = (List) pair.getValue();
            for (PurchaseItem purchaseItem : list) {

                TableRow tbrow = new TableRow(this);

//                CheckBox box = new CheckBox(this);
//                tbrow.addView(box);

                final CheckBox checkBox = new CheckBox(this);
                checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                checkBox.setTextColor(Color.BLACK);
                checkBox.setText(purchaseItem.getName());
                checkBox.setId(new Random().nextInt());
                checkBox.setPadding(1, 1, 1, 1);
                Typeface tf = Typeface.createFromAsset(getAssets(), GroceryConstant.FONTS_VERDANA_TTF);
                checkBox.setTypeface(tf, Typeface.NORMAL);
                tbrow.addView(checkBox);


                TextView tv2 = new TextView(getApplicationContext());
                tv2.setText(purchaseItem.getQuantity());
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tv2.setTextColor(Color.BLACK);
                tv2.setTypeface(tf, Typeface.NORMAL);
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
                tv3.setTypeface(tf, Typeface.NORMAL);
                tbrow.addView(tv3);

                tblLayout.addView(tbrow);

                checkBox.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (checkBox.isChecked() == true) {
                            totalAmountText = (TextView) findViewById(R.id.amount);
                            View row = (View) v.getParent();
                            TableRow tbRow = (TableRow) row;
                            TextView textView = (TextView) tbRow.getChildAt(2);
                            double rowPrice = Double.valueOf(textView.getText().toString()).doubleValue();
                            totalPrice = totalPrice + rowPrice;
                            totalAmountText.setText(new DecimalFormat("#.00").format(totalPrice));
                        } else if (checkBox.isChecked() == false) {
                            totalAmountText = (TextView) findViewById(R.id.amount);
                            View row = (View) v.getParent();
                            TableRow tbRow = (TableRow) row;
                            TextView textView = (TextView) tbRow.getChildAt(2);
                            double rowPrice = Double.valueOf(textView.getText().toString()).doubleValue();
                            totalPrice = totalPrice - rowPrice;
                            totalAmountText.setText(new DecimalFormat("#.00").format(totalPrice));
                        } else {

                        }

                    }

                });

                productSpaceCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (productSpaceCheckBox.isChecked() == true) {

                            View row = (View) view.getParent();
                            TableRow tbRow = (TableRow) row;
                            int totalRows = ((TableRow) row).getChildCount();
                            for (int i = 0; i < totalRows; i++) {
                                CheckBox tempCheckBox = (CheckBox) tbRow.getChildAt(i);
                                tempCheckBox.setEnabled(true);
                                TextView textView = (TextView) tbRow.getChildAt(i);
                                double rowPrice = Double.valueOf(textView.getText().toString()).doubleValue();
                                totalPrice = totalPrice + rowPrice;
                            }
                            totalAmountText.setText(new DecimalFormat("#.00").format(totalPrice));
                        } else if (productSpaceCheckBox.isChecked() == false) {
                            View row = (View) view.getParent();
                            TableRow tbRow = (TableRow) row;
                            int totalRows = ((TableRow) row).getChildCount();
                            for (int i = 0; i < totalRows; i++) {
                                CheckBox tempCheckBox = (CheckBox) tbRow.getChildAt(i);
                                tempCheckBox.setEnabled(false);
                                TextView textView = (TextView) tbRow.getChildAt(i);
                                double rowPrice = Double.valueOf(textView.getText().toString()).doubleValue();
                                totalPrice = totalPrice - rowPrice;
                            }
                            totalAmountText.setText(new DecimalFormat("#.00").format(totalPrice));
                        } else {

                        }


                    }
                });

            }

        }


    }

    public void init_table_layout() {
        tblLayout = (TableLayout) findViewById(R.id.table_shopping_list);
        TableRow tbrow0 = new TableRow(this);

        Typeface tf = Typeface.createFromAsset(getAssets(), GroceryConstant.FONTS_VERDANA_TTF);

//
//        CheckBox box = new CheckBox(this);
//        tbrow0.addView(box);

        productSpaceCheckBox = new CheckBox(this);
        productSpaceCheckBox.setText(GroceryConstant.PRODUCT_SPACE);
        productSpaceCheckBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        productSpaceCheckBox.setTextColor(Color.BLACK);
        productSpaceCheckBox.setTypeface(tf, Typeface.NORMAL);
        tbrow0.addView(productSpaceCheckBox);

        TextView tv2 = new TextView(this);
        tv2.setText(GroceryConstant.UNIT_LESS_SPACE);
        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv2.setTextColor(Color.BLACK);
        tv2.setTypeface(tf, Typeface.NORMAL);
        tbrow0.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText(GroceryConstant.PRICE_SPACE);
        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv3.setTextColor(Color.BLACK);
        tv3.setTypeface(tf, Typeface.NORMAL);
        tbrow0.addView(tv3);

        tblLayout.addView(tbrow0);
    }
}
