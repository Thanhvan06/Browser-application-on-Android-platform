package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.User;
import com.example.myapplication.utils.Hash;
import com.example.myapplication.viewmodel.UserViewModel;

import io.paperdb.Paper;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtPhone, edtPass, edtName;
    private TextView txtActionSignup, txtActionCancel, txtActionToLogin;
    private ProgressBar progressBar;
    private TextView txtInfo;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();
    }

    private void initView() {
        progressBar = findViewById(R.id.progress_signup);
        progressBar.setVisibility(View.INVISIBLE);

        edtName = findViewById(R.id.edt_signup_name);
        edtPass = findViewById(R.id.edt_signup_pass);
        edtPhone = findViewById(R.id.edt_signup_phone);

        txtActionCancel = findViewById(R.id.txt_action_signup_cancel);
        txtActionSignup = findViewById(R.id.txt_action_signup);
        txtActionToLogin = findViewById(R.id.txt_action_to_login);

        txtInfo = findViewById(R.id.txt_info_signup);
        txtInfo.setVisibility(View.INVISIBLE);

        txtActionToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        txtActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPass.setText("");
                edtPhone.setText("");
                setStatusSearchBar(edtPass, false);
            }
        });

        txtActionSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edtPhone.getText().toString();
                String name = edtName.getText().toString();
                String pass = edtPass.getText().toString();
                StringBuilder info = new StringBuilder();
                if (phone.isEmpty() || name.isEmpty() || pass.isEmpty()) {
                    info.append("Data fields cannot be left blank!");
                }
                if (info.toString().trim().isEmpty()) {
                    txtInfo.setText("");
                    txtInfo.setVisibility(View.INVISIBLE);
                    signup();
                } else {
                    txtInfo.setText(info);
                    txtInfo.setVisibility(View.VISIBLE);
                }
            }

            private void signup() {
                progressBar.setVisibility(View.VISIBLE);
                String phone = edtPhone.getText().toString().trim();
                String name = edtName.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();
                String passSHA_256 = Hash.sha256(pass);
                userViewModel = new ViewModelProvider(SignUpActivity.this).get(UserViewModel.class);
                userViewModel.userModelMutableLiveDataSignup(phone, name, passSHA_256).observe(SignUpActivity.this, userModel -> {
                    if (userModel != null) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (userModel.isSuccess()) {
                            txtInfo.setText("");
                            txtInfo.setVisibility(View.INVISIBLE);

                            User user = userModel.getResult().get(0);
                            Paper.book().write("current", user);

                            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            txtInfo.setText("The account already exists in the system!");
                            txtInfo.setVisibility(View.VISIBLE);
                        }
                    } else {
                        txtInfo.setText("The account already exists in the system!");
                        txtInfo.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setStatusSearchBar(EditText editText, boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            editText.requestFocus();
            imm.showSoftInput(editText, 0);
        } else {
            editText.clearFocus();
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }
}