package com.retail.retailapp.vo;

/**
 * Created by raghuramankumarasamy on 08/12/16.
 */
public class SpinnerEntry {

    private String spinnerText;
    private String value;


    public SpinnerEntry(String spinnerText, String value) {
        this.spinnerText = spinnerText;
        this.value = value;
    }

    public String getSpinnerText() {
        return spinnerText;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return spinnerText;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SpinnerEntry){
            SpinnerEntry c = (SpinnerEntry )obj;
            if(c.getSpinnerText().equals(spinnerText) && c.getValue().equals(value) ) return true;
        }

        return false;
    }
}
