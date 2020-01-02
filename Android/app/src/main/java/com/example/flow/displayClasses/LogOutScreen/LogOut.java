package com.example.flow.displayClasses.LogOutScreen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import com.example.flow.Home;

public class LogOut extends AppCompatActivity {

    public void show(){


            new AlertDialog.Builder(LogOut.this)

                    .setTitle("Log out")
                    .setMessage("Are you sure you want to log out?")

                    .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            System.exit(0);
                        }
                    })
                    .setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(LogOut.this, Home.class);
                            startActivity(intent);
                        }
                    }).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        show();
    }
}
