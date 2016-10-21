package com.retail.retailapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.retail.retailapp.vo.GroceryItem;

import java.util.List;
import java.util.Map;

public class ShoppingListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        Intent caller = getIntent();
        Map<String, List<GroceryItem>> data_map = (Map<String, List<GroceryItem>>)caller.getExtras().get("shoppinglist");
        System.out.println("data_map :="+ data_map);
    }
}
