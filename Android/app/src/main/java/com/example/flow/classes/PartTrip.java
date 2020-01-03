package com.example.flow.classes;

import com.google.gson.annotations.SerializedName;

public class PartTrip {
    @SerializedName("id")
    private int Id;
    @SerializedName("countryExpense")
    private CountryExpense CountryExpense;
    @SerializedName("days")
    private int Days;
    @SerializedName("budgetPerDay")
    private double BudgetPerDay;

    public PartTrip() {
    }

    public PartTrip(com.example.flow.classes.CountryExpense countryExpense, int days) {
        CountryExpense = countryExpense;
        Days = days;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public com.example.flow.classes.CountryExpense getCountryExpense() {
        return CountryExpense;
    }

    public void setCountryExpense(com.example.flow.classes.CountryExpense countryExpense) {
        CountryExpense = countryExpense;
    }

    public int getDays() {
        return Days;
    }

    public void setDays(int days) {
        Days = days;
    }

    public double getBudgetPerDay() {
        return BudgetPerDay;
    }

    public void setBudgetPerDay(double budgetPerDay) {
        BudgetPerDay = budgetPerDay;
    }
}
