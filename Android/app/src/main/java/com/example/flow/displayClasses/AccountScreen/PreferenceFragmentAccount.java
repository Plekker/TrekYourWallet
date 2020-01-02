package com.example.flow.displayClasses.AccountScreen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.flow.R;

public class PreferenceFragmentAccount extends PreferenceFragmentCompat {
    public final static String USERNAME = "username";
    public final static String EMAIL = "email";
    public final static String FIRSTNAME = "firstname";
    public final static String LASTNAME = "lastname";
    public final static String PHONE = "phone";

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // Load the Preferences from the XML file
        addPreferencesFromResource(R.xml.account);
        setPreferencesFromResource(R.xml.account, s);

    }
}


