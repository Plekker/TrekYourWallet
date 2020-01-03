package com.example.flow.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CountryExpense implements Parcelable {

    @SerializedName("id")
    private int Id;
    @SerializedName("country")
    private String Country;
    @SerializedName("lowestPrice")
    private double LowestPrice;
    @SerializedName("lowPrice")
    private double LowPrice;
    @SerializedName("averagePrice")
    private double AveragePrice;
    @SerializedName("highPrice")
    private double HighPrice;
    @SerializedName("highestPrice")
    private double HighestPrice;
    @SerializedName("currency")
    private String Currency;

    public CountryExpense() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public double getLowestPrice() {
        return LowestPrice;
    }

    public void setLowestPrice(double lowestPrice) {
        LowestPrice = lowestPrice;
    }

    public double getLowPrice() {
        return LowPrice;
    }

    public void setLowPrice(double lowPrice) {
        LowPrice = lowPrice;
    }

    public double getAveragePrice() {
        return AveragePrice;
    }

    public void setAveragePrice(double averagePrice) {
        AveragePrice = averagePrice;
    }

    public double getHighPrice() {
        return HighPrice;
    }

    public void setHighPrice(double highPrice) {
        HighPrice = highPrice;
    }

    public double getHighestPrice() {
        return HighestPrice;
    }

    public void setHighestPrice(double highestPrice) {
        HighestPrice = highestPrice;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
