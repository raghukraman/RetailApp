package com.retail.retailapp.dataloader;

import com.retail.retailapp.vo.GroceryItem;

import org.json.JSONException;

import java.util.List;
import java.util.Map;

/**
 * Created by raghuramankumarasamy on 20/10/16.
 */
public interface GroceryItemLoader {

    public Map<String, Map<String,Object>>  load(String filename) throws JSONException;

}
