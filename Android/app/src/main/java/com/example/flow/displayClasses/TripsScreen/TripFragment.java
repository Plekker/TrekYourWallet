package com.example.flow.displayClasses.TripsScreen;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flow.R;
import com.example.flow.classes.CurrentPerson;
import com.example.flow.classes.Trip;
import com.example.flow.services.CheckInternet;
import com.example.flow.services.CurrentData;
import com.example.flow.services.RetrofitBuild;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TripFragment extends Fragment
{
    private static DecimalFormat df = new DecimalFormat("0.00");
    private Trip tempTrip;
    private Trip trip;
    private final String TAG = this.getClass().getSimpleName();

    public TripFragment() {
        // Required empty public constructor
    }

    public static TripFragment newInstance() {
        TripFragment fragment = new TripFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            trip = bundle.getParcelable("trip"); // Key
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_trip, container, false);
        TextView nameTrip = RootView.findViewById(R.id.nameCreatedTrip);
        TextView budget = RootView.findViewById(R.id.createdTripBudget);
        TextView remainingBudget = RootView.findViewById(R.id.createdTripRemainingBudget);
        TextView style = RootView.findViewById(R.id.style);

        nameTrip.setText(trip.getNaam());
        budget.setText(getString(R.string.budgetCreatedTrip, df.format(trip.getBudget())));
        remainingBudget.setText(getString(R.string.budgetSpendCreatedTrip, df.format(trip.getBudgetSpend())));

        switch(trip.getType()){
            case 0:
                style.setText(R.string.style0); break;
            case 1:
                style.setText(R.string.style1); break;
            case 2:
                style.setText(R.string.style2); break;
            case 3:
                style.setText(R.string.style3); break;
            case 4:
                style.setText(R.string.style4); break;
            case 5:
                style.setText(R.string.style5); break;

        }

        Button activate = RootView.findViewById(R.id.setToCurrent);

        if(!trip.isCurrentTrip()){
            activate.setEnabled(true);
            activate.setText(R.string.setCurrent);

            activate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(CheckInternet.checkInternet(getActivity().getApplicationContext())){
                        RetrofitBuild retrofit = RetrofitBuild.getInstance();
                        Call<Trip> call = retrofit.apiService.activate("application/json", CurrentPerson.ApiKey, trip.getId());
                        call.enqueue(new Callback<Trip>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse(Call<Trip> call, Response<Trip> response) {
                                trip = response.body();
                                String day = getString(R.string.day, Integer.toString(trip.getDay()));
                                activate.setEnabled(false);
                                activate.setText(day);
                            }

                            @Override
                            public void onFailure(Call<Trip> call, Throwable t) {
                                return;
                            }
                        });
                    }else{
                        Context context = getActivity().getApplicationContext();
                        CharSequence text = getActivity().getApplicationContext().getResources().getString(R.string.internetConnectie);
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
            });
        }else{
            String day = getString(R.string.day, Integer.toString(trip.getDay()));
            activate.setEnabled(false);
            activate.setText(day);
        }

        ListView list = RootView.findViewById(R.id.createdPartTrips);
        CustomAdapter adapter = new CustomAdapter();
        list.setAdapter(adapter);

        return RootView;

    }

    class CustomAdapter extends BaseAdapter {

        int index;

        @Override
        public int getCount()
        {
          return trip.getPartTrips().size();
        }

        @Override
        public Object getItem(int position)
        {
            return position;
        }

        @Override
        public long getItemId(int id)
        {
            return id;
        }

        public View getView(int i, View view, ViewGroup viewgroup)
        {
            view = getLayoutInflater().inflate(R.layout.layout_part_trip, null);

            TextView country = view.findViewById(R.id.country);
            TextView textViewDay = view.findViewById(R.id.dayCountry);
            String name  = trip.getPartTrips().get(i).getCountryExpense().getCountry();
            String days = Integer.toString(trip.getPartTrips().get(i).getDays());
            String daysString = getString(R.string.daysForTrip, days);
            country.setText(name);
            textViewDay.setText(daysString);

            return view;
        }
    }
}