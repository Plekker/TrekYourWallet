package com.example.flow.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Trip implements Parcelable {
    @SerializedName("id")
    private int Id;
    @SerializedName("naam")
    private String Naam;
    @SerializedName("budget")
    private double Budget;
    @SerializedName("budgetSpend")
    private double BudgetSpend;
    @SerializedName("currentTrip")
    private boolean CurrentTrip;
    @SerializedName("day")
    private int Day;
    @SerializedName("partTrips")
    private PartTrip[] PartTrips;
    @SerializedName("budgetRemainingToday")
    private double BudgetRemainingToday;

    public Trip(String naam, double budget) {
        Naam = naam;
        Budget = budget;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNaam() {
        return Naam;
    }

    public void setNaam(String naam) {
        Naam = naam;
    }

    public double getBudget() {
        return Budget;
    }

    public void setBudget(double budget) {
        Budget = budget;
    }

    public double getBudgetSpend() {
        return BudgetSpend;
    }

    public void setBudgetSpend(double budgetSpend) {
        BudgetSpend = budgetSpend;
    }

    public boolean isCurrentTrip() {
        return CurrentTrip;
    }

    public void setCurrentTrip(boolean currentTrip) {
        CurrentTrip = currentTrip;
    }

    public int getDay() {
        return Day;
    }

    public void setDay(int day) {
        Day = day;
    }

    public PartTrip[] getPartTrips() {
        return PartTrips;
    }

    public void setPartTrips(PartTrip[] partTrips) {
        PartTrips = partTrips;
    }

    public double getBudgetRemainingToday() {
        return BudgetRemainingToday;
    }

    public void setBudgetRemainingToday(double budgetRemainingToday) {
        BudgetRemainingToday = budgetRemainingToday;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
