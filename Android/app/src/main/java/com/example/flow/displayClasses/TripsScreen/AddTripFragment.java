package com.example.flow.displayClasses.TripsScreen;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.flow.R;
import com.example.flow.classes.CurrentPerson;
import com.example.flow.classes.Expense;
import com.example.flow.classes.Trip;
import com.example.flow.displayClasses.OverviewScreen.OverviewFragment;
import com.example.flow.services.CurrentData;
import com.example.flow.services.RetrofitBuild;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddTripFragment extends Fragment
{
    private final String TAG = this.getClass().getSimpleName();

    private View RootView;
    static public String createdTripName;
    static public double priceExpense;

    public AddTripFragment() {
        // Required empty public constructor
    }

    public static AddTripFragment newInstance() {
        AddTripFragment fragment = new AddTripFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        RootView = inflater.inflate(R.layout.fragment_add_trip, container, false);

        Button ID = RootView.findViewById(R.id.toCountries);
        ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText createdName = RootView.findViewById(R.id.nameTripField);
                createdTripName = createdName.getText().toString();
                EditText price = RootView.findViewById(R.id.budgetField);
                priceExpense = Double.parseDouble(price.getText().toString());

                Trip trip = new Trip(createdTripName, priceExpense);

                FragmentManager fragmentManager = getFragmentManager();
                Bundle args = new Bundle();
                args.putParcelable("trip", trip);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AddCountriesToTripFragment NAME = new AddCountriesToTripFragment();
                NAME.setArguments(args);
                fragmentTransaction.replace(R.id.relativelayout_for_fragment, NAME);
                fragmentTransaction.addToBackStack(null); //when back button is pressed on next page, the app returns to this page
                fragmentTransaction.commit();

            }
        });

        return RootView;
    }

}





