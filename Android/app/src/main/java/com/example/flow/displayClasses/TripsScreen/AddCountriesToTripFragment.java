
package com.example.flow.displayClasses.TripsScreen;


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

import com.example.flow.R;
import com.example.flow.classes.Trip;
import com.example.flow.displayClasses.OverviewScreen.OverviewFragment;


public class AddCountriesToTripFragment extends Fragment
{
    private final String TAG = this.getClass().getSimpleName();

    private View RootView;
    private Trip trip;
    static public String createdTripName;
    static public double priceExpense;

    public AddCountriesToTripFragment() {
        // Required empty public constructor
    }

    public static AddCountriesToTripFragment newInstance() {
        AddCountriesToTripFragment fragment = new AddCountriesToTripFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            trip = bundle.getParcelable("trip"); // Key
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        RootView = inflater.inflate(R.layout.fragment_add_expense, container, false);

        Button ID = RootView.findViewById(R.id.toCountries);
        ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText createdName = RootView.findViewById(R.id.name);
                createdTripName = createdName.getText().toString();
                EditText price = RootView.findViewById(R.id.budgetField);
                priceExpense = Double.parseDouble(price.getText().toString());

                Trip trip = new Trip(createdTripName, priceExpense);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                OverviewFragment NAME = new OverviewFragment();
                fragmentTransaction.replace(R.id.relativelayout_for_fragment, NAME);
                fragmentTransaction.addToBackStack(null); //when back button is pressed on next page, the app returns to this page
                Bundle args = new Bundle();
                args.putParcelable("trip", trip);
                fragmentTransaction.commit();

            }
        });

        return RootView;
    }

}





