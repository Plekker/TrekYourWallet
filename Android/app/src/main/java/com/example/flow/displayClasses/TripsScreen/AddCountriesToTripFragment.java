
package com.example.flow.displayClasses.TripsScreen;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flow.R;
import com.example.flow.classes.CountryExpense;
import com.example.flow.classes.CurrentPerson;
import com.example.flow.classes.Trip;
import com.example.flow.services.CheckInternet;
import com.example.flow.services.RetrofitBuild;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddCountriesToTripFragment extends Fragment
{
    private final String TAG = this.getClass().getSimpleName();

    private View RootView;
    private Trip trip;
    private List<CountryExpense> countryExpenses;
    ArrayList<CountryExpense> filters;
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



        RootView = inflater.inflate(R.layout.fragment_add_countries_to_trip, container, false);


        EditText search =  RootView.findViewById(R.id.createdTripBudget);
        ListView list = RootView.findViewById(R.id.listCountryExpenses);
        Button confirm = RootView.findViewById(R.id.confirmPartTrip);


        if(CheckInternet.checkInternet(getActivity().getApplicationContext())){
            RetrofitBuild retrofit = RetrofitBuild.getInstance();
            Call<List<CountryExpense>> call = retrofit.apiService.getCountryExpenses();
            call.enqueue(new Callback<List<CountryExpense>>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<List<CountryExpense>> call, Response<List<CountryExpense>> response) {

                    if(response.body().size() != 0){
                        countryExpenses = response.body();

                        CustomAdapter adapter = new CustomAdapter();
                        list.setAdapter(adapter);

                        search.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                adapter.getFilter().filter(charSequence);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });

                        confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                RetrofitBuild retrofit = RetrofitBuild.getInstance();
                                Call<Trip> call = retrofit.apiService.saveTrip("application/json", CurrentPerson.ApiKey, trip);
                                call.enqueue(new Callback<Trip>() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onResponse(Call<Trip> call, Response<Trip> response) {

                                        Trip createdTrip = response.body();

                                        RetrofitBuild retrofit = RetrofitBuild.getInstance();
                                        Call<Trip> callCalculate = retrofit.apiService.calculate("application/json", CurrentPerson.ApiKey, createdTrip.getId(), createdTrip);
                                        callCalculate.enqueue(new Callback<Trip>() {
                                            @SuppressLint("SetTextI18n")
                                            @Override
                                            public void onResponse(Call<Trip> callCalculate, Response<Trip> response) {
                                                Trip calculatedTrip  = response.body();
                                                FragmentManager fragmentManager = getFragmentManager();
                                                Bundle args = new Bundle();
                                                args.putParcelable("trip", calculatedTrip);
                                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                TripFragment NAME = new TripFragment();
                                                NAME.setArguments(args);
                                                fragmentTransaction.replace(R.id.relativelayout_for_fragment, NAME);
                                                fragmentTransaction.addToBackStack(null); //when back button is pressed on next page, the app returns to this page
                                                fragmentTransaction.commit();
                                            }

                                            @Override
                                            public void onFailure(Call<Trip> callCalculate, Throwable t) {
                                                return;
                                            }
                                        });


                                    }

                                    @Override
                                    public void onFailure(Call<Trip> call, Throwable t) {
                                        return;
                                    }
                                });
                            }
                        });
                    }

                }

                @Override
                public void onFailure(Call<List<CountryExpense>> call, Throwable t) {
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


    class CustomAdapter extends BaseAdapter implements Filterable {

        private CustomFilter filter = new CustomFilter();
        private List<CountryExpense> filteredData = countryExpenses;


        int index;

        @Override
        public int getCount()
        {
            return filteredData.size();
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
            view = getLayoutInflater().inflate(R.layout.layout_contry_expense, null);

            TextView textViewName = view.findViewById(R.id.countryName);
            String name  = filteredData.get(i).getCountry();
            textViewName.setText(name);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getFragmentManager();
                    Bundle args = new Bundle();
                    args.putParcelable("trip", trip);
                    args.putParcelable("country", filteredData.get(i));
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    AddPartTripFragment NAME = new AddPartTripFragment();
                    NAME.setArguments(args);
                    fragmentTransaction.replace(R.id.relativelayout_for_fragment, NAME);
                    fragmentTransaction.addToBackStack(null); //when back button is pressed on next page, the app returns to this page
                    fragmentTransaction.commit();

                }
            });
            return view;
        }

        @Override
        public Filter getFilter() {
            return filter;
        }

        class CustomFilter extends Filter{

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();

                if(charSequence!=null && charSequence.length() > 0){
                    String filterString = charSequence.toString().toUpperCase();
                    final List<CountryExpense> list = countryExpenses;
                    int count = list.size();

                    final ArrayList<CountryExpense> nlist = new ArrayList<CountryExpense>(count);


                    for (int i = 0; i<count; i++){
                        if(list.get(i).getCountry().toUpperCase().contains(filterString)){
                            nlist.add(countryExpenses.get(i));
                        }
                    }
                    results.count = nlist.size();
                    results.values = nlist;
                }else{
                    results.count = countryExpenses.size();
                    results.values = countryExpenses;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredData = (ArrayList<CountryExpense>) filterResults.values;
                notifyDataSetChanged();
            }
        }
    }

}





