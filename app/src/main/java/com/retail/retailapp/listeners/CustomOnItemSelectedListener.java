package com.retail.retailapp.listeners;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import android.widget.Spinner;

import com.retail.retailapp.R;

/**
 * Created by raghuramankumarasamy on 14/10/16.
 * To do: Move to this class
 */
public class CustomOnItemSelectedListener implements OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view,
                               int position, long id) {
        Spinner categoryspinner = (Spinner) view.findViewById(R.id.category);
        Spinner itemlistspinner = (Spinner) view.findViewById(R.id.list);
        String categoryName = categoryspinner.getItemAtPosition(categoryspinner.getSelectedItemPosition()).toString();

//        List<String> items = getList(categoryName);
//        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, items);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        itemlistspinner.setAdapter(adapter);

        System.out.println("Selected value :=" + categoryName);
        System.out.println("Id is :=" + id);

    }
    public void onNothingSelected(AdapterView<?> parent) {
        return;

    }
}
