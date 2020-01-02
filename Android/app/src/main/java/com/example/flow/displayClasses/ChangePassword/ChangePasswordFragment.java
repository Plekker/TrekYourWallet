package com.example.flow.displayClasses.ChangePassword;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.flow.R;
import com.example.flow.classes.CurrentPerson;
import com.example.flow.displayClasses.AccountScreen.AccountFragment;
import com.example.flow.displayClasses.OverviewScreen.OverviewFragment;
import com.example.flow.services.ChangePasswordInfo;
import com.example.flow.services.RetrofitBuild;

import java.util.regex.Pattern;


import android.support.v4.app.FragmentTransaction;

import okhttp3.ResponseBody;

public class ChangePasswordFragment extends Fragment {

    private View Rootview;

    private EditText _oldPassword;
    private EditText _newPassword;
    private EditText _confirmPassword;
    private Button _doneButton;
    private Button _deleteButton;
    private OnFragmentInteractionListener mListener;

    private String mToken;
    private Handler mHandler;
    private Activity mActivity;
    private SharedPreferences mSharedPreferences;
    private View mView;
    private static final String LOG_TAG = AccountFragment.class.getSimpleName();

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Rootview = inflater.inflate(R.layout.fragment_changepassword, container, false);

        _oldPassword = Rootview.findViewById(R.id.old_password);
        _newPassword = Rootview.findViewById(R.id.new_password);
        _confirmPassword = Rootview.findViewById(R.id.connfirm_password);
        _doneButton = Rootview.findViewById(R.id.done_button);
        //-----------------

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);



        mHandler = new Handler(Looper.getMainLooper());

        _doneButton.setOnClickListener(v -> {
            hideKeyboard();
            if (checkEmptyText())
                checkPasswordMatch();
        });
        //-----------------

        return Rootview;
    }

    private boolean checkEmptyText() {
        if (!validate((_oldPassword))) {
            _oldPassword.setError(getString(R.string.required));
            _oldPassword.requestFocus();
            return false;
        }else if (!validate((_newPassword))) {
            _newPassword.setError(getString(R.string.required));
            _newPassword.requestFocus();
            return false;
        } else if (_confirmPassword.getText().toString().equals("") || !_confirmPassword.getText().toString().equals(_newPassword.getText().toString())) {
            _confirmPassword.setError(getString(R.string.required));
            _confirmPassword.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    public boolean validate(EditText password) {
        boolean valid = true;

        final Pattern PASSWORD_PATTERN =
                Pattern.compile("^" +
                        "(?=.*[0-9])" +         //at least 1 digit
                        "(?=.*[a-z])" +         //at least 1 lower case letter
                        "(?=.*[A-Z])" +         //at least 1 upper case letter
                        "(?=\\S+$)" +           //no white spaces
                        ".{4,}" +               //at least 4 characters
                        "$");


        if (password.getText().toString().isEmpty()) {
            password.setError("Field can't be empty");
            valid =false;
        } else if (!PASSWORD_PATTERN.matcher(password.getText().toString()).matches()) {
            password.setError("Password too weak");
            valid =false;
        } else {
            password.setError(null);
        }

        return valid;
    }

    private void checkPasswordMatch() {
        String oldPassword = _oldPassword.getText().toString();
        String newPassword = _newPassword.getText().toString();

        RetrofitBuild retrofit = RetrofitBuild.getInstance();
        retrofit2.Call<ResponseBody> call = retrofit.apiService.updatePassword("application/json", CurrentPerson.ApiKey, new ChangePasswordInfo(newPassword, oldPassword));
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                if(response.code() == 400){
                    Snackbar snackbar = Snackbar
                            .make(mActivity.findViewById(android.R.id.content),
                                    R.string.passwords_check, Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                OverviewFragment overviewFragment = new OverviewFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.relativelayout_for_fragment, overviewFragment);
                fragmentTransaction.addToBackStack(null); //when back button is pressed on next page, the app returns to this page
                fragmentTransaction.commit();
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
            }
        });

    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(Rootview.getWindowToken(), 0);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(String data);
    }
}

