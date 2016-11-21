package com.retail.retailapp;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.retail.retailapp.util.GroceryConstant;

public class FetchSavedListActivity extends AppCompatActivity {

    TableLayout tblLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_saved_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Setup spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        "Shopping List 1",
                        "Shopping List 2",
                        "Shopping List 3",
                }));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                        .commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void init() {
        tblLayout = (TableLayout) findViewById(R.id.table_shopping_list);
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
        tv1.setWidth(230);
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
        tv3.setPadding(0, 0, 40, 0);  //set some space after the price column
        tv3.setTypeface(face);
        tbrow0.addView(tv3);

//        Button btn = new Button(MainActivity.this);
//        TableRow.LayoutParams tlp = new TableRow.LayoutParams(120, 56);
//        btn.setPadding(10, 7, 0, 2);
//        btn.setGravity(Gravity.TOP);
//        btn.setLayoutParams(tlp);
//        btn.setText(" X-All");
//        btn.setId(0);

        ImageView image = new ImageView(FetchSavedListActivity.this);
        image.setImageResource(R.drawable.cross_mark);
        TableRow.LayoutParams tlp = new TableRow.LayoutParams(50, 52);
        image.setPadding(1, 0, 1, 2);
        image.setLayoutParams(tlp);
        image.setId(0);

//            btn.setLayoutParams(layoutParams);
        tbrow0.addView(image);


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
