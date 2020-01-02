package com.example.flow.displayClasses.LoginScreens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flow.R;
import com.example.flow.classes.Person;
import com.example.flow.services.RetrofitBuild;

import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class activity_register extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private EditText _name;
    private EditText _email;
    private EditText _password;
    private EditText _reenterpassword;
    private TextView _signupButton;
    private TextView _loginlink;
    private TextView _mailValid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        _name = findViewById(R.id.priceExpense);
        _email = findViewById(R.id.input_email);
        _password = findViewById(R.id.input_password);
        _reenterpassword = findViewById(R.id.input_reEnterPassword);
        _signupButton = findViewById((R.id.btn_signup));
        _loginlink = findViewById((R.id.link_login));
        _mailValid = findViewById((R.id.mailValid));

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(activity_register.this,Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        Person person = new Person(_name.getText().toString(), _password.getText().toString(), _email.getText().toString());

        RetrofitBuild retrofit = RetrofitBuild.getInstance();
        Call<ResponseBody> call = retrofit.apiService.register("application/json", person);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 400){
                    _mailValid.setText(R.string.mailValid);
                    _signupButton.setEnabled(true);
                    return;
                }

                final ProgressDialog progressDialog = new ProgressDialog(activity_register.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Creating Account...");
                progressDialog.show();

                String username = _name.getText().toString();
                String email = _email.getText().toString();
                String password = _password.getText().toString();
                String reEnterPassword = _reenterpassword.getText().toString();

                // TODO: Implement your own signup logic here.

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // On complete call either onSignupSuccess or onSignupFailed
                                // depending on success
                                onSignupSuccess();
                                // onSignupFailed();
                                progressDialog.dismiss();
                            }
                        }, 3000);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                return;
            }
        });


    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _name.getText().toString();
        String email = _email.getText().toString();
        String password = _password.getText().toString();
        String reEnterPassword = _reenterpassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _name.setError("at least 3 characters");
            valid = false;
        } else {
            _name.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _email.setError("enter a valid email address");
            valid = false;
        } else {
            _email.setError(null);
        }

        final Pattern PASSWORD_PATTERN =
                Pattern.compile("^" +
                        "(?=.*[0-9])" +         //at least 1 digit
                        "(?=.*[a-z])" +         //at least 1 lower case letter
                        "(?=.*[A-Z])" +         //at least 1 upper case letter
                        "(?=\\S+$)" +           //no white spaces
                        ".{4,}" +               //at least 4 characters
                        "$");

        if (password.isEmpty()) {
            _password.setError("Field can't be empty");
            valid =false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            _password.setError("Password too weak");
            valid =false;
        } else {
            _password.setError(null);
        }

        if (!(reEnterPassword.equals(password))) {
            _reenterpassword.setError("Password Do not match");
            valid = false;
        } else {
            _reenterpassword.setError(null);
        }

        return valid;
    }
}