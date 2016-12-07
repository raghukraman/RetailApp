package com.retail.retailapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.retail.retailapp.database.DBHandler;
import com.retail.retailapp.util.GroceryConstant;
import com.retail.retailapp.vo.PurchaseItem;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FetchSavedListActivity extends AppCompatActivity {

    TableLayout tblLayout;

    DBHandler dbHandler;

    Spinner spinner;

    TextView totalAmountText;

    double totalPrice = 0;

    CheckBox productSpaceCheckBox;
    Map<String, String> mapOrderedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_saved_list);
        Intent caller = getIntent();
        dbHandler = new DBHandler(this);
        mapOrderedItems = new HashMap<String, String>();
        String orderNumber = "";
                try {
                    orderNumber= (String) caller.getExtras().get("orderNumber");
                } catch (Exception e) {

                }
        System.out.println("Order Number :=" + orderNumber);


//
//        System.out.println("the size of selecte map is " + data_map.size());
//        System.out.println("the selected map is :=" + data_map);

//        init_table_layout();
//        load_table(data_map);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<String> openOrders = dbHandler.getOpenOrders();
        String[] openOrdersArray = new String[openOrders.size()];
        openOrders.toArray(openOrdersArray);

        System.out.println("The Open Ordres are :=" + openOrders);
        // Setup spinner
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                openOrdersArray));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                final LinearLayout linearLayoutForTable = (LinearLayout) findViewById(R.id.linearLayoutForTable);
                final TableLayout tableLayout = (TableLayout) findViewById(R.id.table_shopping_list);
                tableLayout.removeAllViews();

                String cartNumber=spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();

                Map<String, List<PurchaseItem>> data_map = dbHandler.getOrderDetails(cartNumber);

                final TextView cartNumberView = (TextView) findViewById(R.id.cartnumber);
                System.out.println("Cart Number " + cartNumberView.getText());
                cartNumberView.setText(cartNumber);

//                init_table_layout();
                load_table(data_map);

                // When the given dropdown item is selected, show its contents in the
                // container view.
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
//                        .commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button finsihButton = (Button) findViewById(R.id.done_btn);
        finsihButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView cartNumberText = (TextView) findViewById(R.id.cartnumber);
                String cart_number = cartNumberText.getText().toString();
                boolean result = dbHandler.updateOrder(cart_number, mapOrderedItems);
                if (result) {
                    Spinner spinnerView = (Spinner) findViewById(R.id.spinner);
                    Toast.makeText(FetchSavedListActivity.this, "This order is closed successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(FetchSavedListActivity.this, "Something went wrong !! ", Toast.LENGTH_LONG).show();

                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fetch_saved_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void load_table(Map<String, List<PurchaseItem>> data_map) {
        tblLayout = (TableLayout) findViewById(R.id.table_shopping_list);

        Iterator it = data_map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            List<PurchaseItem> list = (List) pair.getValue();
            for (PurchaseItem purchaseItem : list) {

                TableRow tbrow = new TableRow(this);

                final CheckBox checkBox = new CheckBox(this);
                checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                checkBox.setTextColor(Color.BLACK);
                checkBox.setText(purchaseItem.getName());
                checkBox.setId(new Random().nextInt());
                checkBox.setPadding(1, 1, 1, 1);
                Typeface tf = Typeface.createFromAsset(getAssets(), GroceryConstant.FONTS_VERDANA_TTF);
                checkBox.setTypeface(tf, Typeface.NORMAL);
                tbrow.addView(checkBox);

                checkBox.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (checkBox.isChecked() == true) {
                            totalAmountText = (TextView) findViewById(R.id.amount);
                            View row = (View) v.getParent();
                            TableRow tbRow = (TableRow) row;
                            TextView textView = (TextView) tbRow.getChildAt(2);
                            TextView itemName = (TextView) tbRow.getChildAt(0);
                            double rowPrice = Double.valueOf(textView.getText().toString()).doubleValue();
                            totalPrice = totalPrice + rowPrice;
                            totalAmountText.setText(new DecimalFormat("#.00").format(totalPrice));
                            mapOrderedItems.put(itemName.getText().toString(), itemName.getText().toString());

                        } else if (checkBox.isChecked() == false) {
                            totalAmountText = (TextView) findViewById(R.id.amount);
                            View row = (View) v.getParent();
                            TableRow tbRow = (TableRow) row;
                            TextView textView = (TextView) tbRow.getChildAt(2);
                            TextView itemName = (TextView) tbRow.getChildAt(0);
                            double rowPrice = Double.valueOf(textView.getText().toString()).doubleValue();
                            totalPrice = totalPrice - rowPrice;
                            totalAmountText.setText(new DecimalFormat("#.00").format(totalPrice));
                            mapOrderedItems.remove(itemName.getText().toString());
                        } else {

                        }

                    }

                });

                TextView tv2 = new TextView(getApplicationContext());
                tv2.setText(purchaseItem.getQuantity());
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tv2.setTextColor(Color.BLACK);
                tv2.setTypeface(tf, Typeface.NORMAL);
                tv2.setGravity(Gravity.RIGHT);
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
                tv3.setGravity(Gravity.RIGHT);
                tbrow.addView(tv3);

//                Button btn = new Button(this);
//                btn.setText("X");
////                btn.setId(rowNumber);
//                TableRow.LayoutParams tlp = new TableRow.LayoutParams(100, 52);
//                btn.setPadding(1, 0, 1, 2);
//                btn.setLayoutParams(tlp);
//
//                tbrow.addView(btn);

                tblLayout.addView(tbrow);

//                btn.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        // row is your row, the parent of the clicked button
//                        View row = (View) v.getParent();
//                        // container contains all the rows, you could keep a variable somewhere else to the container which you can refer to here
//                        ViewGroup container = ((ViewGroup) row.getParent());
//                        // delete the row and invalidate your view so it gets redrawn
//                        container.removeView(row);
//                        container.invalidate();
//                    }
//
//                });

            }

        }


    }

    public void init_table_layout() {
        tblLayout = (TableLayout) findViewById(R.id.table_shopping_list);
        TableRow tbrow0 = new TableRow(this);

        Typeface tf = Typeface.createFromAsset(getAssets(), GroceryConstant.FONTS_VERDANA_TTF);


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

//        Button btn = new Button(this);
//        TableRow.LayoutParams tlp = new TableRow.LayoutParams(120, 56);
//        btn.setPadding(10, 7, 0, 2);
//        btn.setGravity(Gravity.TOP);
//        btn.setLayoutParams(tlp);
//        btn.setText("X-All");
//        btn.setId(0);

//        btn.setLayoutParams(layoutParams);
//        tbrow0.addView(btn);


        tblLayout.addView(tbrow0);
    }

    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_fetch_saved_list, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

    }
}
