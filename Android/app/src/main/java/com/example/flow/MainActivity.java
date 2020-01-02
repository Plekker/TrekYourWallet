package com.example.flow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.flow.classes.Person;
import com.example.flow.displayClasses.LoginScreens.Login;
import com.example.flow.services.RetrofitBuild;

import android.os.Handler;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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

		String apiKey = readApiKey();

		if (apiKey == null) {
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
			person.setApiKey(apiKey.substring(0, apiKey.length() - 1));
			RetrofitBuild retrofit = RetrofitBuild.getInstance();
			Call<Person> call = retrofit.apiService.getPersonForApiKey(person.getApiKey());
			call.enqueue(new Callback<Person>() {
				@Override
				public void onResponse(Call<Person> call, Response<Person> response) {
					Person person = response.body();

					if(person.getEmail() != null){
						//Got to new activity
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

	private String readApiKey() {
		StringBuilder text = new StringBuilder();
		try {
			File directory = getApplicationContext().getFilesDir();
			File file = new File(directory, "token");

			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String ttt = text.toString();
		if (ttt != "") {
			return ttt;
		}
		return null;
	}
}
