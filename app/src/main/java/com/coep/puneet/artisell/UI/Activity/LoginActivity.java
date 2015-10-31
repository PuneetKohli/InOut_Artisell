package com.coep.puneet.artisell.UI.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.coep.puneet.artisell.Global.AppConstants;
import com.coep.puneet.artisell.Global.SendUnicodeSms;
import com.coep.puneet.artisell.R;

import butterknife.Bind;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity
{
    @Bind(R.id.loginButtonFrame) FrameLayout mLoginButtonFrame;
    @Bind(R.id.loginButton) Button mLoginButton;
    @Bind(R.id.editText_phone_no) EditText mPhoneEditText;
    @Bind(R.id.progress_login) ProgressBar mProgressLogin;


    @OnClick(R.id.loginButton)
    void login()
    {
        mLoginButton.setText("");
        mProgressLogin.setVisibility(View.VISIBLE);
        manager.loginArtisan(mPhoneEditText.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

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

        mPhoneEditText.addTextChangedListener(new TextWatcher()
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
        });

        manager.getAllCategory();

    }

    boolean validatePhoneNumber()
    {
        return true;
    }

    void sendOtpSMS()
    {
        /*SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("+919819954448", null, "OTP is 5490", null, null);
        SendUnicodeSms.sendSms("9819954448");*/
    }

    void showOtpField()
    {
/*        mImageValidate.setVisibility(View.VISIBLE);
        mProgressValidate.setVisibility(View.GONE);
        mPasswordEditText.setVisibility(View.VISIBLE);
        mLoginButtonFrame.setVisibility(View.VISIBLE);*/

    }

    @Override
    public void processFinish(String result, String type)
    {
        switch (type)
        {
            case AppConstants.RESULT_LOGIN_SUCCESS:
                manager.getAllProductsFromCurrentArtisan();
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
        //navigator.openNewActivity(LoginActivity.this, new HomeActivity());
        mProgressLogin.setVisibility(View.INVISIBLE);
        mLoginButton.setText(getString(R.string.login));
    }

    void resetButton()
    {
        mProgressLogin.setVisibility(View.INVISIBLE);
        mLoginButton.setText(getString(R.string.login));
    }
}
