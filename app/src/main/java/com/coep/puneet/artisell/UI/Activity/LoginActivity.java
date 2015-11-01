package com.coep.puneet.artisell.UI.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.coep.puneet.artisell.Global.AppConstants;
import com.coep.puneet.artisell.Global.SendUnicodeSms;
import com.coep.puneet.artisell.R;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity
{
    @Bind(R.id.loginButtonFrame) FrameLayout mLoginButtonFrame;
    @Bind(R.id.loginButton) Button mLoginButton;
    @Bind(R.id.editText_phone_no) EditText mPhoneEditText;
    @Bind(R.id.editText_password) EditText editTextOTP;
    @Bind(R.id.progress_login) ProgressBar mProgressLogin;


    @OnClick(R.id.loginButton)
    void login()
    {
        if(mLoginButton.getText().toString().equals("Send OTP")) {
            if(mPhoneEditText.getText().toString().length() == 10) {
                sendOtpSMS();
                showOtpField();
                Toast.makeText(LoginActivity.this, "Your OTP has been sent, please enter it below", Toast.LENGTH_SHORT).show();
                mLoginButton.setText("Login");
            }
        }
        else
        {
            if (editTextOTP.getText().toString().equals("5490"))
            {
                mLoginButton.setText("");
                mProgressLogin.setVisibility(View.VISIBLE);
                manager.loginArtisan(mPhoneEditText.getText().toString());
            }
            else
            {
                Toast.makeText(LoginActivity.this, "Please enter the correct OTP", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(ParseUser.getCurrentUser() != null) {
            manager.getAllProductsFromCurrentArtisanOffline();
            manager.getAllArtisansLocal();
            openNextActivity();
        }
    }


    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_login;
    }

    @Override
    protected void setupToolbar()
    {

    }

    @Override
    protected void setupLayout()
    {
        manager.delegate = this;
        mLoginButton.setText("Send OTP");
        /*mPhoneEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.length() == 10)
                {
                    //Validate phone number
                    if (validatePhoneNumber())
                    {
                        sendOtpSMS();
                        showOtpField();
                    }
                }
            }
        });*/

        manager.getAllCategoryLocal();
        manager.getAllEventsLocal();
        manager.getAllRequestsLocal();

    }

    boolean validatePhoneNumber()
    {
        return true;
    }

    void sendOtpSMS()
    {
        SendUnicodeSms.sendSms("9819954448", "OTP is 5490");
        SendUnicodeSms.sendSms("7507118432", "OTP is 5490");
    }

    void showOtpField()
    {

        editTextOTP.setVisibility(View.VISIBLE);

    }

    @Override
    public void processFinish(String result, String type)
    {
        switch (type)
        {
            case AppConstants.RESULT_LOGIN_SUCCESS:
                manager.getAllProductsFromCurrentArtisan();
                manager.getAllArtisans();
                openNextActivity();
                break;
            case AppConstants.RESULT_LOGIN_FAIL:
                Toast.makeText(LoginActivity.this, "Error in Logging In", Toast.LENGTH_SHORT).show();
                resetButton();
                break;
        }
    }

    void openNextActivity()
    {
        navigator.openNewActivity(LoginActivity.this, new HomeActivity());
        mProgressLogin.setVisibility(View.INVISIBLE);
        mLoginButton.setText(getString(R.string.login));
    }

    void resetButton()
    {
        mProgressLogin.setVisibility(View.INVISIBLE);
        mLoginButton.setText(getString(R.string.login));
    }
}
