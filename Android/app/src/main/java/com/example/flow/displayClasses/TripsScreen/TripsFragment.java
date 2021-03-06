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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TripsFragment extends Fragment
{
    private final String TAG = this.getClass().getSimpleName();

    public TripsFragment() {
        // Required empty public constructor
    }

    public static TripsFragment newInstance() {
        TripsFragment fragment = new TripsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_trips, container, false);

        ImageButton ID = RootView.findViewById(R.id.addTrip);
        ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AddTripFragment NAME = new AddTripFragment();
                fragmentTransaction.replace(R.id.relativelayout_for_fragment, NAME);
                fragmentTransaction.addToBackStack(null); //when back button is pressed on next page, the app returns to this page
                fragmentTransaction.commit();

            }
        });
        if(CheckInternet.checkInternet(getActivity().getApplicationContext())){
            RetrofitBuild retrofit = RetrofitBuild.getInstance();
            Call<List<Trip>> call = retrofit.apiService.getTrips("application/json", CurrentPerson.ApiKey);
            call.enqueue(new Callback<List<Trip>>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {

                    if(response.body().size() == 0){

                    }
                    else{

                        CurrentData.trips = response.body();


                        ListView list = RootView.findViewById(R.id.tripsList);
                        CustomAdapter adapter = new CustomAdapter();
                        list.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<List<Trip>> call, Throwable t) {
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



        return RootView;

    }

    class CustomAdapter extends BaseAdapter {

        int index;

        @Override
        public int getCount()
        {
          return CurrentData.trips.size();
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
            view = getLayoutInflater().inflate(R.layout.layout_trip, null);

            TextView textViewName = view.findViewById(R.id.tripName);
            TextView textViewDay = view.findViewById(R.id.tripDay);
            String name  = CurrentData.trips.get(i).getNaam();
            if(CurrentData.trips.get(i).isCurrentTrip()){
                String day  = "Day " + CurrentData.trips.get(i).getDay();
                textViewDay.setText(day);
            }
            textViewName.setText(name);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getFragmentManager();
                    Bundle args = new Bundle();
                    args.putParcelable("trip", CurrentData.trips.get(i));
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    TripFragment NAME = new TripFragment();
                    NAME.setArguments(args);
                    fragmentTransaction.replace(R.id.relativelayout_for_fragment, NAME);
                    fragmentTransaction.addToBackStack(null); //when back button is pressed on next page, the app returns to this page
                    fragmentTransaction.commit();
                }
            });

            return view;
        }
    }
}