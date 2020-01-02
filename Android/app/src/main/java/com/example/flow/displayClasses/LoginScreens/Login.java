package com.example.flow.displayClasses.LoginScreens;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.flow.Home;
import com.example.flow.R;
import com.example.flow.classes.Person;
import com.example.flow.services.RetrofitBuild;

import java.io.FileOutputStream;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity {

    private int counter=3;
    private static final String TAG = "LoginActivity";

    private EditText _emailText;
    private EditText _passwordText;
    private Button _loginButton;
    private TextView _signupLink;
    private TextView _signinattempts;
    private Person _person;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//standard
        setContentView(R.layout.activity_login);//standard

        _emailText = findViewById(R.id.input_email);
        _passwordText = findViewById(R.id.input_password);
        _loginButton = findViewById(R.id.btn_login);
        _signupLink = findViewById(R.id.link_signup);
        _signinattempts = findViewById(R.id.SignInAttempts);


        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(Login.this, activity_register.class);//getApplicationContext() - Returns the context for all activities running in application.
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);// create an animated transition between the two activities
            }
        });
    }

    public void login() {
        if(!validate())
            return;

        _loginButton.setEnabled(false);
        _person = new Person(_passwordText.getText().toString(), _emailText.getText().toString());

        RetrofitBuild retrofit = RetrofitBuild.getInstance();
        Call<Person> call = retrofit.apiService.getApiKey("application/json", _person);
        call.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {

                Person personIn = response.body();

                if(personIn == null){
                    _loginButton.setEnabled(true);
                    _signinattempts.setText(R.string.unvalid);
                    return;
                }

                SaveApiKey(personIn.getApiKey());

                Intent intent = new Intent(Login.this, Home.class);
                intent.putExtra("Person", personIn);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
                counter--;
                _signinattempts.setText("No of attempts remaining: "+ String.valueOf(counter));
                if(counter==0) { _loginButton.setEnabled(false); }
                return;
            }
        });
    }

    private void SaveApiKey(String apiKey){
        String filename = "apiKey";
        FileOutputStream outputStream;
        String fileContents = apiKey;

        try {
            outputStream = getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validate() {
        boolean valid = true;

       String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        final Pattern PASSWORD_PATTERN =
                Pattern.compile("^" +
                        "(?=.*[0-9])" +         //at least 1 digit
                        "(?=.*[a-z])" +         //at least 1 lower case letter
                        "(?=.*[A-Z])" +         //at least 1 upper case letter
                        "(?=\\S+$)" +           //no white spaces
                        ".{4,}" +               //at least 4 characters
                        "$");

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {//checking if email is valid
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError("Field can't be empty");
            valid =false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            _passwordText.setError("Password too weak");
            valid =false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }



    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, Login.class);
    }
}


