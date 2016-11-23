package com.retail.retailapp.vo;

/**
 * Created by raghuramankumarasamy on 25/10/16.
 */
public class PurchaseItem extends GroceryItem {

    private String quantity;
    private Double itemPrice;
    private String itemKey;

    public PurchaseItem(String name, Double price, Double itemPrice, String quantity, String type, String itemKey) {
        super(name,price,type);
        this.itemPrice=itemPrice;
        this.quantity=quantity;
        this.itemKey = itemKey;
    }

    public String getQuantity() {
        return quantity;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public String getItemKey() {
        return itemKey;
    }


}
