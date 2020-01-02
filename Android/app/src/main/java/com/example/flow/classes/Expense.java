package com.example.flow.classes;

import com.google.gson.annotations.SerializedName;

public class Expense {
    @SerializedName("id")
    private int Id;
    @SerializedName("name")
    private String Name;
    @SerializedName("price")
    private double Price;
    @SerializedName("day")
    private int Day;
    @SerializedName("currency")
    private String Currency;
    @SerializedName("trip")
    private Trip Trip;
    @SerializedName("dateTime")
    private String DateTime;

    public Expense(String name, double price, com.example.flow.classes.Trip trip) {
        Name = name;
        Price = price;
        Trip = trip;
    }

    public Expense(String name, double price, String currency, com.example.flow.classes.Trip trip) {
        Name = name;
        Price = price;
        Currency = currency;
        Trip = trip;
    }

    public Expense() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getDay() {
        return Day;
    }

    public void setDay(int day) {
        Day = day;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        this.Currency = currency;
    }

    public Trip getTrip() {
        return Trip;
    }

    public void setTrip(Trip trip) {
        this.Trip = trip;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }
}
