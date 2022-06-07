package com.gp.shifa.data.models;

import com.google.gson.annotations.SerializedName;

public class CommissionCalculatorModel {

    /**
     * value : 0.75
     */

    @SerializedName("value")
    private double value;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
