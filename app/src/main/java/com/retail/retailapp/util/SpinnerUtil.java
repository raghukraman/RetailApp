package com.retail.retailapp.util;

import android.widget.Spinner;

/**
 * Created by raghuramankumarasamy on 08/12/16.
 */
public class SpinnerUtil {

    public static void SetSpinnerSelection(Spinner spinner,String[] array,String text) {
        for(int i=0;i<array.length;i++) {
            if(array[i].equals(text)) {
                spinner.setSelection(i);
            }
        }
    }

}
