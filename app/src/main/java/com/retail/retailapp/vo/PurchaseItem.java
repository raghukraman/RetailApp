package com.retail.retailapp.vo;

/**
 * Created by raghuramankumarasamy on 25/10/16.
 */
public class PurchaseItem extends GroceryItem {

    private String quantity;
    private Double itemPrice;
    private String itemKey;
    private Boolean status;

    public PurchaseItem(String name, Double price, Double itemPrice, String quantity, String type, String itemKey,Boolean status) {
        super(name,price,type);
        this.itemPrice=itemPrice;
        this.quantity=quantity;
        this.itemKey = itemKey;
        this.status=status;
    }

    public PurchaseItem(String name, Double itemPrice, String quantity, String type, Boolean status) {
        this(name,null,itemPrice,quantity,type,"",status);
    }

    public PurchaseItem(String name, Double price, Double itemPrice, String quantity,String type) {
        this(name,price,itemPrice,quantity,type,"",false);
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

    @Override
    public String toString() {
        return "PurchaseItem{" +
                "quantity='" + quantity + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemKey='" + itemKey + '\'' +
                ", status=" + status +
                '}';
    }
}
