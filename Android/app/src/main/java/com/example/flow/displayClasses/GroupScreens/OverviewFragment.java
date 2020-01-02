package com.example.flow.displayClasses.GroupScreens;


import android.annotation.SuppressLint;
import android.support.v4.app.FragmentManager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.flow.R;

import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.flow.classes.CurrentPerson;
import com.example.flow.classes.Expense;
import com.example.flow.services.CurrentData;
import com.example.flow.services.RetrofitBuild;

import java.util.ArrayList;
import java.util.List;

import android.support.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OverviewFragment extends Fragment
{
    private final String TAG = this.getClass().getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public OverviewFragment() {
        // Required empty public constructor
    }

    public static OverviewFragment newInstance(String param1, String param2) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_overview, container, false);

        RetrofitBuild retrofit = RetrofitBuild.getInstance();
        Call<List<Expense>> call = retrofit.apiService.getExpensesForCurrentTrip("application/json", CurrentPerson.ApiKey);
        call.enqueue(new Callback<List<Expense>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<Expense>> call, Response<List<Expense>> response) {

                if(response.code() == 404){
                    TextView textView = RootView.findViewById(R.id.remaining);
                    textView.setText(R.string.activate);
                }
                else{

                    CurrentData.expenesForCurrenTrip = response.body();

                    if(CurrentData.expenesForCurrenTrip.size() == 0){

                    }
                    CurrentData.currentTrip = CurrentData.expenesForCurrenTrip.get(0).getTrip();

                    TextView textView = RootView.findViewById(R.id.budgetRemaining);
                    textView.setText(Double.toString(CurrentData.currentTrip.getBudgetRemainingToday()));

                    ListView list = RootView.findViewById(R.id.listView);
                    CustomAdapter adapter = new CustomAdapter();
                    list.setAdapter(adapter);

                    /*
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                        {

                            selectedGroup = UsedObjects.groups.get(position);
                            HomeGroupFragment homeGroupFragment = new HomeGroupFragment();
                            FragmentManager manager = getFragmentManager();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            Fragment f = homeGroupFragment;
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            Bundle bundle = new Bundle();
                            f.setArguments(bundle);
                            ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
                            ft.replace(R.id.relativelayout_for_fragment, f, homeGroupFragment.getTag());
                            ft.addToBackStack(null); //when back button is pressed on next page, the app returns to this page
                            ft.commit();


                        }
                    });
        */

                    ImageButton ID = RootView.findViewById(R.id.plusButton);
                    ID.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            AddExpenseFragment NAME = new AddExpenseFragment();
                            fragmentTransaction.replace(R.id.relativelayout_for_fragment, NAME);
                            fragmentTransaction.addToBackStack(null); //when back button is pressed on next page, the app returns to this page
                            fragmentTransaction.commit();

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Expense>> call, Throwable t) {
                return;
            }
        });



        return RootView;

    }

    class CustomAdapter extends BaseAdapter {

        int index;

        @Override
        public int getCount()
        {
          return CurrentData.expenesForCurrenTrip.size();
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
            view = getLayoutInflater().inflate(R.layout.expensen_layout, null);

            TextView textViewName = view.findViewById(R.id.expenseName);
            TextView textViewPrice = view.findViewById(R.id.price);
            TextView textViewDay = view.findViewById(R.id.day);
            String name  = CurrentData.expenesForCurrenTrip.get(i).getName();
            String price  = Double.toString(CurrentData.expenesForCurrenTrip.get(i).getPrice());
            String day  = "Day " + CurrentData.expenesForCurrenTrip.get(i).getDay();
            textViewName.setText(name);
            textViewPrice.setText(price);
            textViewDay.setText(day);

            return view;
        }
    }
}