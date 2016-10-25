package com.retail.retailapp.util;

import com.retail.retailapp.vo.GroceryItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by raghuramankumarasamy on 20/10/16.
 */
public class GroceryUtil {



    public static List<String> getItemList(Map<String,List<GroceryItem>> groceryMap, String name) {
        List<GroceryItem> items = groceryMap.get(name);
        List<String> list=new ArrayList<>();
        for (GroceryItem gi: items) {
            String itemName = gi.getName();
            list.add(itemName);
        }
        return list;
    }

    public static GroceryItem getUnitPrice(Map<String,List<GroceryItem>> groceryMap,String CategoryName,String itemname) {
        Double price = null;
        String type = null;
        Double unitPrice=null;
        GroceryItem item = null;
        List<GroceryItem> items = groceryMap.get(CategoryName);
        for (GroceryItem gi: items) {
            if (gi.getName().equals(itemname)) {
                price = gi.getPrice();
                type = gi.getType();
                item = new GroceryItem(itemname,price,type);
                break;
            }
        }
        return item;
    }

    public static Double getPrice(Map<String,List<GroceryItem>> groceryMap,String CategoryName,String itemname,String quantity) {
        GroceryItem item = getUnitPrice(groceryMap,CategoryName,itemname);
        String type = item.getType();
        Double itemPrice=null;
        Double price=item.getPrice();


        if (type.equalsIgnoreCase("Weight")) {
            if (quantity.indexOf("kg") != -1) {
                String val = quantity.substring(0,quantity.indexOf("kg"));
                itemPrice = Double.parseDouble(val)*price;
            }
            else if (quantity.indexOf("g") != -1) {
                String val = quantity.substring(0,quantity.indexOf("g"));
                itemPrice = (Double.parseDouble(val)/1000)*price;
            }
        }
        if (type.equalsIgnoreCase("Litre")) {
            if (quantity.indexOf("L") != -1) {
                String val = quantity.substring(0,quantity.indexOf("L"));
                itemPrice = Double.parseDouble(val)*price;
            }

        }

        if (type.equalsIgnoreCase("numbers")) {
            itemPrice = price * Double.parseDouble(quantity);
        }

        itemPrice = round(itemPrice.doubleValue(),2);

        return itemPrice;
    }

    public static List<String> getQuantityList(Map<String,List<GroceryItem>> groceryMap,Map<String,List<String>> quantityMap,String CategoryName,String itemname) {
        List<String> quantityList = null;
        List<GroceryItem> items = groceryMap.get(CategoryName);
        for (GroceryItem gi: items) {
            if (gi.getName().equals(itemname)) {
                quantityList = quantityMap.get(gi.getType());
            }
        }
        return quantityList;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}


