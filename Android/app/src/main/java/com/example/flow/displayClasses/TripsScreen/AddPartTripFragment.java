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
import com.example.flow.classes.CountryExpense;
import com.example.flow.classes.PartTrip;
import com.example.flow.classes.Trip;


public class AddPartTripFragment extends Fragment
{
    private final String TAG = this.getClass().getSimpleName();

    private View RootView;
    static public int daysPerCountry;
    private Trip trip;
    private CountryExpense country;

    public AddPartTripFragment() {
        // Required empty public constructor
    }

    public static AddPartTripFragment newInstance() {
        AddPartTripFragment fragment = new AddPartTripFragment();
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
            country = bundle.getParcelable("country"); // Key
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        RootView = inflater.inflate(R.layout.fragment_add_part_trip, container, false);

        Button ID = RootView.findViewById(R.id.confirmPartTrip);
        ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText days = RootView.findViewById(R.id.setDays);
                daysPerCountry = Integer.parseInt(days.getText().toString());

                int order = trip.getPartTrips().size() + 1;

                trip.getPartTrips().add(new PartTrip(country, daysPerCountry, order));

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





