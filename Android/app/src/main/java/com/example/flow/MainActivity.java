package com.example.flow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.flow.classes.Person;
import com.example.flow.classes.PersonDto;
import com.example.flow.displayClasses.LoginScreens.Login;
import com.example.flow.services.AppDatabase;
import com.example.flow.services.PersonDao;
import com.example.flow.services.RetrofitBuild;

import android.os.Handler;
import android.view.WindowManager;

import androidx.room.Room;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{
	private static int SPLASH_TIME_OUT = 4000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		AppDatabase db = getDb();
		PersonDao personDao = getDb().personDao();
		List<PersonDto> personDtos = personDao.getAll();

		if (personDtos.size()== 0) {
			setContentView(R.layout.activity_welcome_screen);
			new Handler().postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					Intent homeIntent = new Intent(MainActivity.this, Login.class);
					startActivity(homeIntent);
					finish();
				}
			}, SPLASH_TIME_OUT);
		} else {
			Person person = new Person();
			//person.setApiKey(apiKey.substring(0, apiKey.length() - 1));
			RetrofitBuild retrofit = RetrofitBuild.getInstance();
			Call<Person> call = retrofit.apiService.getPersonForApiKey(personDtos.get(0).apiKey);
			call.enqueue(new Callback<Person>() {
				@Override
				public void onResponse(Call<Person> call, Response<Person> response) {
					Person person = response.body();

					if(response.code() == 400){
						setContentView(R.layout.activity_welcome_screen);
						new Handler().postDelayed(new Runnable()
						{
							@Override
							public void run()
							{
								personDao.delete(personDtos.get(0));

								Intent homeIntent = new Intent(MainActivity.this, Login.class);
								startActivity(homeIntent);
								finish();
							}
						}, SPLASH_TIME_OUT);
					}else{
						setContentView(R.layout.activity_welcome_screen);
						new Handler().postDelayed(new Runnable()
						{
							@Override
							public void run()
							{
								Intent homeIntent = new Intent(MainActivity.this, Home.class);
								homeIntent.putExtra("Person", response.body());
								startActivity(homeIntent);
								finish();
							}
						}, SPLASH_TIME_OUT);
					}
				}

				@Override
				public void onFailure(Call<Person> call, Throwable t) {
					Intent homeIntent = new Intent(MainActivity.this, Login.class);
					startActivity(homeIntent);
				}
			});
		}
	}

	private AppDatabase getDb(){
		return Room.databaseBuilder(getBaseContext().getApplicationContext(), AppDatabase.class, "poerson")
				.fallbackToDestructiveMigration()
				.allowMainThreadQueries()
				.build();
	}
}
