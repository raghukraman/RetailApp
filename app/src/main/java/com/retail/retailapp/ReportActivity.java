package com.retail.retailapp;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.numetriclabz.numandroidcharts.ChartData;
import com.numetriclabz.numandroidcharts.DonutChart;
import com.retail.retailapp.database.DBHandler;
import com.retail.retailapp.util.SpinnerUtil;
import com.retail.retailapp.vo.SpinnerEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static DBHandler dbHandler;

    static List<PieEntry> entries = new ArrayList<>();

    private String year ;

    private String month;

    private static PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        dbHandler = new DBHandler(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

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
        getMenuInflater().inflate(R.menu.menu_report, menu);
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

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_report, container, false);

            List<PieEntry> entries = loadYearAndMonth(rootView);



//            entries = dbHandler.getPieChartDetails(2016,11);

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

                 pieChart= (PieChart) rootView.findViewById(R.id.pieChart);

                PieDataSet pieDataSet = new PieDataSet(entries, "Category");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                PieData pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);


            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

                BarChart chart = (BarChart) rootView.findViewById(R.id.barChart1);
                ArrayList<BarEntry> entries2 = new ArrayList<>();
                entries2.add(new BarEntry(10f, 0));
                entries2.add(new BarEntry(5f, 1));
                entries2.add(new BarEntry(6f, 2));
                entries2.add(new BarEntry(8f, 3));
                entries2.add(new BarEntry(2f, 4));
                entries2.add(new BarEntry(20f, 5));

                BarDataSet dataset = new BarDataSet(entries2, "Monthly Amount");

                dataset.setValues(entries2);

                ArrayList<String> labels = new ArrayList<String>();
                labels.add("January");
                labels.add("February");
                labels.add("March");
                labels.add("April");
                labels.add("May");
                labels.add("June");


                BarData data = new BarData(dataset);
                data.addDataSet(dataset);
                data.setDrawValues(true);

                chart.setData(data);


            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

                //
                DonutChart donut = (DonutChart) rootView.findViewById(R.id.piegraph);
                ChartData values = new ChartData();
                values.setSectorValue(4);
                donut.addSector(values);

                values = new ChartData();
                values.setSectorValue(20);
                donut.addSector(values);

                values = new ChartData();
                values.setSectorValue(8);
                donut.addSector(values);

                values = new ChartData();
                values.setSectorValue(5);
                donut.addSector(values);

            } else {

            }
            return rootView;


        }

        private List<PieEntry> loadYearAndMonth(View rootView) {
            final Spinner yearSpinner = (Spinner) rootView.findViewById(R.id.year_id);
            Spinner monthSpinner = (Spinner) rootView.findViewById(R.id.month_id);

            List<String> years = new ArrayList<>();
            List<String> month = new ArrayList<>();

            //Loading the years into spinner
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);

            for (int i = 2015; i <= year; i++) {
                years.add(String.valueOf(i).toString());
            }
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, years);
            adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            yearSpinner.setAdapter(adapter1);

            //Loading the months into spinner

            final SpinnerEntry items[] = new SpinnerEntry[12];
            final String months[] = { "January", "Febrary", "March", "April", "May","June","July", "August","September","October","November","December"};
            for (int i = 0; i < 12; i++) {
                items[i]= new SpinnerEntry(months[i],""+i);
            }

            ArrayAdapter<SpinnerEntry> adapter2 = new ArrayAdapter<SpinnerEntry>(this.getActivity(), android.R.layout.simple_spinner_item, items);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            monthSpinner.setAdapter(adapter2);





            String yearValue = yearSpinner.getItemAtPosition(yearSpinner.getSelectedItemPosition()).toString();
            System.out.println("Year Value " + yearValue);
            SpinnerEntry selectedEntry = (SpinnerEntry) monthSpinner.getItemAtPosition(monthSpinner.getSelectedItemPosition());

            Integer yearVal = Integer.parseInt(yearValue);
            Integer monthVal = Integer.parseInt(selectedEntry.getValue());
            System.out.println("Month Value is " + monthVal);

            Calendar c = Calendar.getInstance();
            int calendar_year = c.get(Calendar.YEAR);
            int calendar_month = c.get(Calendar.MONTH);

            String[] yearsArray = years.toArray(new String[years.size()]);
            SpinnerUtil.SetSpinnerSelection(yearSpinner,yearsArray,calendar_year+"");
            monthSpinner.setSelection(calendar_month);

            monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    SpinnerEntry entry = (SpinnerEntry) parent.getSelectedItem();
                    int selectedMonth = Integer.parseInt(entry.getValue());
                    String yearValue = yearSpinner.getItemAtPosition(yearSpinner.getSelectedItemPosition()).toString();
                    int selectedYear = Integer.parseInt(yearValue);
                    System.out.println("Year selected " + selectedYear +" selected month " + selectedMonth);
                    entries = dbHandler.getPieChartDetails(selectedYear,selectedMonth);
                    System.out.println("Entries is " + entries);
                    pieChart.clear();
                    PieDataSet pieDataSet = new PieDataSet(entries, "Category");
                    pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    PieData pieData = new PieData(pieDataSet);
                    pieChart.setData(pieData);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            System.out.println("Year " + calendar_year + " month " + calendar_month);


            List<PieEntry> entries=dbHandler.getPieChartDetails(calendar_year,calendar_month);

            return entries;

        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Category Report";
                case 1:
                    return "Monthly Report";
                case 2:
                    return "Others";
            }
            return null;
        }
    }


}
