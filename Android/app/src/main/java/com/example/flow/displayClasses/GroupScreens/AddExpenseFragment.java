package com.example.flow.displayClasses.GroupScreens;


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
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.flow.R;
import com.example.flow.classes.CurrentPerson;
import com.example.flow.classes.Expense;
import com.example.flow.services.CurrentData;
import com.example.flow.services.RetrofitBuild;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddExpenseFragment extends Fragment
{
    private final String TAG = this.getClass().getSimpleName();

    Intent intent;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View RootView;

    public boolean testing = true;
    static public String createdExpenseName;
    static public double priceExpense;

    public AddExpenseFragment() {
        // Required empty public constructor
    }

    public static AddExpenseFragment newInstance(String param1, String param2) {
        AddExpenseFragment fragment = new AddExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        RootView = inflater.inflate(R.layout.fragment_add_expense, container, false);

        ImageButton ID = RootView.findViewById(R.id.confirmExpense);
        ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText createdName = RootView.findViewById(R.id.name);
                createdExpenseName = createdName.getText().toString();
                EditText price = RootView.findViewById(R.id.priceAddExpense);
                priceExpense = Double.parseDouble(price.getText().toString());

                RetrofitBuild retrofit = RetrofitBuild.getInstance();
                Call<Expense> call = retrofit.apiService.addExpense("application/json", CurrentPerson.ApiKey, CurrentData.currentTrip.getId(), new Expense(createdExpenseName, priceExpense, "EUR", CurrentData.currentTrip));
                call.enqueue(new Callback<Expense>() {
                    @Override
                    public void onResponse(Call<Expense> call, Response<Expense> response) {
                        CurrentData.currentTrip = response.body().getTrip();
                        CurrentData.expenesForCurrenTrip.add(response.body());

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        OverviewFragment NAME = new OverviewFragment();
                        fragmentTransaction.replace(R.id.relativelayout_for_fragment, NAME);
                        fragmentTransaction.addToBackStack(null); //when back button is pressed on next page, the app returns to this page
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onFailure(Call<Expense> call, Throwable t) {
                        return;
                    }
                });

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                OverviewFragment NAME = new OverviewFragment();
                fragmentTransaction.replace(R.id.relativelayout_for_fragment, NAME);
                fragmentTransaction.addToBackStack(null); //when back button is pressed on next page, the app returns to this page
                fragmentTransaction.commit();

            }
        });

        return RootView;
    }

}





