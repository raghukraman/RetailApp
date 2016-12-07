package com.retail.retailapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.mikephil.charting.data.PieEntry;
import com.retail.retailapp.vo.PurchaseItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by raghuramankumarasamy on 21/11/16.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "retail";

    private static final String TABLE_ORDER = "retail_order";

    private static final String TABLE_ORDER_DETAILS = "retail_order_details";

    private static final String ORDER_NO = "orderno";
    private static final String ID = "id";
    private static final String CREATION_DATE = "creation_date";
    private static final String STATUS = "status";



    private static final String ITEM_NAME = "item_name";
    private static final String CATEGORY = "category";
    private static final String QUANTITY = "quantity";
    private static final String PRICE    = "price";
    private static final int ZERO = 0;
    private static final int ONE = 1;


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /*
     * There is no datetime object in sqlite.
     * hence store the datetime as integer as unix time
     * the number of seconds since 1970-01-01 00:00:00 UTC
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_ORDER = "CREATE TABLE " + TABLE_ORDER + "("
//                + ID + "INTEGER PRIMARY KEY ,"
                + ORDER_NO + " TEXT," + CREATION_DATE + " INTEGER,"
                + STATUS + " INTEGER" + ")";

        String CREATE_TABLE_ORDER_DETAILS = "CREATE TABLE " + TABLE_ORDER_DETAILS + "("
                + ORDER_NO + " TEXT ,"
                + ITEM_NAME + " TEXT,"
                + CATEGORY  + " TEXT,"
                + QUANTITY  + " TEXT,"
                + PRICE     + " REAL,"
                + STATUS    + " INTEGER"
        + ")";


        db.execSQL(CREATE_TABLE_ORDER);
        db.execSQL(CREATE_TABLE_ORDER_DETAILS);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_DETAILS);

        // Create tables again
        onCreate(db);
    }


    public String createOrder(Map<String, List<PurchaseItem>> selectedMap,String orderNumber) {

        String formattedOrderNumber = "";
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0); // same for minutes and seconds
        today.set(Calendar.MINUTE,0);
        today.set(Calendar.SECOND,0);
        Date todaysDateAt12AM = today.getTime();
        int year=today.get(Calendar.YEAR);
        int month=today.get(Calendar.MONTH)+1;
        int date=today.get(Calendar.DATE);
        long unixtime = todaysDateAt12AM.getTime()/1000;
        int noOfOrdersExisting = getNumberOfRows(unixtime)+1;

        if (orderNumber != null && !"".equals(orderNumber.trim())) {
            formattedOrderNumber = orderNumber;
        } else {
            formattedOrderNumber = year + "-" + month +"-"+date +"-"+noOfOrdersExisting;
        }

        contentValues.put(ORDER_NO, formattedOrderNumber);
        long unixTimeNow = System.currentTimeMillis() / 1000;
        contentValues.put(CREATION_DATE, unixTimeNow);
        contentValues.put(STATUS, 0);

        // Inserting Row
        if (orderNumber == null || "".equals(orderNumber.trim())) {
            System.out.println("Inserting into the ORder Table");
            db.insert(TABLE_ORDER, null, contentValues); //insert only new orders
        }
        createOrderDetails(selectedMap,db,formattedOrderNumber); //create Order Details
        db.close(); // Closing database connection
        return formattedOrderNumber;
    }

    private void createOrderDetails(Map<String, List<PurchaseItem>> selectedMap, SQLiteDatabase db,String orderNumber) {

        db.execSQL("delete from " + TABLE_ORDER_DETAILS + " where "+ ORDER_NO   + "= '" + orderNumber + "' ");

        Iterator it = selectedMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            List<PurchaseItem> list = (List) pair.getValue();
            for (PurchaseItem purchaseItem : list) {
                String category = (String) pair.getKey();
                ContentValues contentValues = new ContentValues();
                contentValues.put(ORDER_NO, orderNumber);
                int unixTimeNow = (int) System.currentTimeMillis() / 1000;
                contentValues.put(ITEM_NAME, purchaseItem.getName());
                contentValues.put(CATEGORY,category);
                contentValues.put(QUANTITY,purchaseItem.getQuantity());
                contentValues.put(PRICE,purchaseItem.getPrice());
                contentValues.put(STATUS,0);
                db.insert(TABLE_ORDER_DETAILS,null,contentValues);
                System.out.println("Order Details are inserted := Successfully for order Number " + orderNumber);
            }
        }
    }


    public int getNumberOfRows(long unixTime) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from retail_order where creation_date>=" + unixTime + "", null);
        int noOfRows = res.getCount();
        return noOfRows;
    }


    public List<String> getOpenOrders() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + ORDER_NO + " FROM " + TABLE_ORDER + " WHERE " + STATUS + "='" + ZERO + "'" + " ORDER BY " + CREATION_DATE + " DESC LIMIT 3 ";
        Cursor c = db.rawQuery(query, null);
        List<String> openOrders = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                openOrders.add(c.getString(0));
            }while(c.moveToNext());
        }
        System.out.println("List(openorders) :=" + openOrders);
        c.close();
        db.close();
        return openOrders;

    }

    public Map<String, List<PurchaseItem>> getOrderDetails(String orderNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + CATEGORY + "," + ITEM_NAME + "," + QUANTITY + "," + PRICE + "," + STATUS + " FROM " + TABLE_ORDER_DETAILS + " where " + ORDER_NO + " = '" + orderNumber + "'";
        System.out.println("quer is :=" + query);
        Cursor c = db.rawQuery(query, null);
        Map<String, List<PurchaseItem>> selectedMap = new HashMap<>();

        if(c.moveToFirst()){
            do{
                //assing values
                String category = c.getString(0);
                String itemname = c.getString(1);
                String quantity = c.getString(2);
                Double price    = c.getDouble(3);

                System.out.println("The price of the item is : = "+ price);

                boolean status  = (c.getInt(4)==1); // 1 means item purchased, 0 means not yet purchased

                PurchaseItem currentItem = new PurchaseItem(itemname,price,quantity,null,status);

                if (selectedMap.get(category)==null) {
                    List<PurchaseItem> list = new ArrayList<>();
                    list.add(currentItem);
                    selectedMap.put(category,list);
                } else {
                    List<PurchaseItem> list = selectedMap.get(category);
                    list.add(currentItem);
                }
                //Do something Here with values

            }while(c.moveToNext());
        }
        c.close();
        db.close();
        return selectedMap;
    }

    public boolean updateOrder(String orderNo) {
        boolean result = false;
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues set_values = new ContentValues();
        set_values.put(STATUS, 1);


        int updated_count = db.update(TABLE_ORDER, set_values, ORDER_NO + "='" + orderNo + "'", null);

        if (updated_count > 0) {
            result = true;
        }

        return result;
    }

    public List<PieEntry> getPieChartDetails(int year,int month) {
        SQLiteDatabase db = this.getReadableDatabase();
        Calendar start = Calendar.getInstance();
        start.set(year,month,1,0,0,0);
        Date date = start.getTime();
        long startUnixTime = date.getTime()/1000;

        System.out.println("Start Unix Time " + startUnixTime);


        Calendar mycal = new GregorianCalendar(year, month, 1);
        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);

        Calendar end = Calendar.getInstance();
        end.set(year,month,daysInMonth,24,0,0);
        Date end1 = end.getTime();
        long endUnixTime = end1.getTime()/1000;

        System.out.println("End Unix Time " + endUnixTime);

        String query = "select category,sum(price) from retail_order_details rod join retail_order ro on rod.orderno=ro.orderno where creation_date>=" + startUnixTime +  " and creation_date<=" + endUnixTime + " and ro.status=1 and rod.status=0 group by category";

//        String query = "select category,price,rod.orderno,ro.creation_date from retail_order_details rod join retail_order ro on ro.orderno=rod.orderno";

        List<PieEntry> entries = new ArrayList<>();

        Cursor c = db.rawQuery(query, null);
        System.out.println("no of rows c" + c.getCount());
        List<String> openOrders = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                String label = c.getString(0);
                float priceValue = c.getFloat(1);
//                String order_num = c.getString(2);
//                long creationtime = c.getLong(3);
                System.out.println("the values are label " + label + " value " + priceValue);
//                System.out.println("the values are label " + label + " value " + priceValue + "order number " + order_num + "creation time " + creationtime);
                entries.add(new PieEntry(priceValue,label));
            }while(c.moveToNext());
        }

        c.close();
        db.close();
        return entries;


    }



}

