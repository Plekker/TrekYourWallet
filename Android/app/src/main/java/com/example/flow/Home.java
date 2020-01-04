package com.example.flow;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Gravity;

import com.example.flow.classes.CurrentPerson;
import com.example.flow.classes.Person;
import com.example.flow.classes.PersonDto;
import com.example.flow.displayClasses.LogOutScreen.LogOut;
import com.example.flow.displayClasses.ChangePassword.ChangePasswordFragment;
import com.example.flow.displayClasses.LoginScreens.Login;
import com.example.flow.displayClasses.TripsScreen.AddTripFragment;
import com.example.flow.displayClasses.TripsScreen.TripsFragment;
import com.example.flow.services.AppDatabase;
import com.example.flow.services.PersonDao;
import com.example.flow.services.RetrofitBuild;

import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import androidx.room.Room;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        ChangePasswordFragment.OnFragmentInteractionListener,
        BottomNavigationView.OnNavigationItemSelectedListener
{

    FragmentManager manager;
    String currentFragment;

    DrawerLayout drawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Person person = (Person)getIntent().getSerializableExtra("Person");

        CurrentPerson.Name = person.getName();
        CurrentPerson.Email = person.getEmail();
        CurrentPerson.Id = person.getId();
        CurrentPerson.ApiKey = person.getApiKey();

        setContentView(R.layout.bottom_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        com.example.flow.displayClasses.OverviewScreen.OverviewFragment overviewFragment = new com.example.flow.displayClasses.OverviewScreen.OverviewFragment();
        manager = getSupportFragmentManager();
        manager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                List<Fragment> f = manager.getFragments();
                Fragment frag = f.get(0);
                currentFragment = frag.getClass().getSimpleName();
            }
        });
        manager.beginTransaction().replace(
                R.id.relativelayout_for_fragment,
                overviewFragment,
                overviewFragment.getTag()
        ).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.main_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (currentFragment) {
            case "AddCountriesToTripFragment":
                AddTripFragment NAME = new AddTripFragment();
                fragmentTransaction.replace(R.id.relativelayout_for_fragment, NAME);
                fragmentTransaction.addToBackStack(null); //when back button is pressed on next page, the app returns to this page
                fragmentTransaction.commit();
                return;
            case "TripFragment":
                TripsFragment tripsFragment = new TripsFragment();
                fragmentTransaction.replace(R.id.relativelayout_for_fragment, tripsFragment);
                fragmentTransaction.addToBackStack(null); //when back button is pressed on next page, the app returns to this page
                fragmentTransaction.commit();
                return;
            default:
                manager.popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuRight) {
            if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                drawer.closeDrawer(Gravity.RIGHT);
            } else {
                drawer.openDrawer(Gravity.RIGHT);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_password) {

            ChangePasswordFragment blankFragment = new ChangePasswordFragment();
            FragmentManager manager = getSupportFragmentManager();
            for(int i = 0; i < manager.getBackStackEntryCount(); ++i) {
                manager.popBackStack();
            } //to completly delete backstack
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out)
                    .replace(R.id.relativelayout_for_fragment,
                            blankFragment,
                            blankFragment.getTag()
                    ).commit();

        }
        else if(id == R.id.nav_logout){
            RetrofitBuild retrofit = RetrofitBuild.getInstance();
            Call<ResponseBody> call = retrofit.apiService.logout(CurrentPerson.ApiKey);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    deleteApiKey();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    deleteApiKey();
                }
            });
        }
        else if (id == R.id.trips) {

            TripsFragment tripsFragment = new TripsFragment();
            FragmentManager manager = getSupportFragmentManager();
            for(int i = 0; i < manager.getBackStackEntryCount(); ++i) {
                manager.popBackStack();
            } //to completly delete backstack
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out)
                    .replace(R.id.relativelayout_for_fragment,
                            tripsFragment,
                            tripsFragment.getTag()
                    )
                    .commit();
        }

        else if (id == R.id.overview) {

            com.example.flow.displayClasses.OverviewScreen.OverviewFragment overviewFragment = new com.example.flow.displayClasses.OverviewScreen.OverviewFragment();
            FragmentManager manager = getSupportFragmentManager();
            for(int i = 0; i < manager.getBackStackEntryCount(); ++i) {
                manager.popBackStack();
            } //to completly delete backstack
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.push_left_out, R.anim.push_left_in)
                    .replace(R.id.relativelayout_for_fragment,
                            overviewFragment,
                            overviewFragment.getTag()
                    ).commit();
        }
        else if (id == R.id.nav_logout) {
            Intent intent = new Intent(Home.this, LogOut.class);//getApplicationContext() - Returns the context for all activities running in application.
            startActivity(intent);
        }




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onFragmentInteraction(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void deleteApiKey(){
        AppDatabase db = getDb();
        PersonDao personDao = getDb().personDao();
        List<PersonDto> personDtos = personDao.getAll();
        personDao.delete(personDtos.get(0));

        Intent homeIntent = new Intent(Home.this, Login.class);
        startActivity(homeIntent);
        finish();
    }

    private AppDatabase getDb(){
        return Room.databaseBuilder(getBaseContext().getApplicationContext(), AppDatabase.class, "poerson")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }
}




