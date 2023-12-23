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

public class LoginActivity extends AppCompatActivity {
    private EditText edtPhone, edtPass;
    private TextView txtActionLogin, txtActionCancel, txtActionToSignup;
    private TextView txtInfo;
    private UserViewModel userViewModel;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Paper.init(this);
        Paper.book().delete("current");

        initView();
    }

    private void initView() {
        progressBar = findViewById(R.id.progress_login);
        progressBar.setVisibility(View.INVISIBLE);

        edtPhone = findViewById(R.id.edt_login_phone);
        edtPass = findViewById(R.id.edt_login_pass);

        txtActionCancel = findViewById(R.id.txt_action_login_cancel);
        txtActionLogin = findViewById(R.id.txt_action_login);
        txtActionToSignup = findViewById(R.id.txt_action_to_signup);

        txtInfo = findViewById(R.id.txt_info_login);

        txtActionToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        txtActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPass.setText("");
                edtPhone.setText("");
                txtInfo.setText("");
                txtInfo.setVisibility(View.INVISIBLE);
                setStatusSearchBar(edtPass, false);
            }
        });
        txtActionLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edtPhone.getText().toString();
                String pass = edtPass.getText().toString();
                StringBuilder info = new StringBuilder();
                if (phone.isEmpty() || pass.isEmpty()) {
                    info.append("Data fields cannot be left blank!");
                }
                if (info.toString().isEmpty()) {
                    txtInfo.setText("");
                    txtInfo.setVisibility(View.INVISIBLE);
                    login();
                } else {
                    txtInfo.setText(info);
                    txtInfo.setVisibility(View.VISIBLE);
                }
            }

            private void login() {
                progressBar.setVisibility(View.VISIBLE);
                String phone = edtPhone.getText().toString().trim();
                userViewModel = new ViewModelProvider(LoginActivity.this).get(UserViewModel.class);
                userViewModel.userModelMutableLiveDataLogin(phone).observe(LoginActivity.this, userModel -> {
                    if (userModel != null) {
                        if (userModel.isSuccess()) {
                            String pass = edtPass.getText().toString();
                            String passSHA_256 = Hash.sha256(pass);
                            User user = userModel.getResult().get(0);
                            String passReal = userModel.getResult().get(0).getPassword();
                            if (passSHA_256.equals(passReal)) {
                                txtInfo.setText("");
                                txtInfo.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);

                                Paper.book().write("current", user);

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                edtPass.setText("");
                                txtInfo.setText("The password is wrong!");
                                txtInfo.setVisibility(View.VISIBLE);
                            }
                        } else {
                            txtInfo.setText("The account does not exist on the system!");
                            txtInfo.setVisibility(View.VISIBLE);
                        }
                    } else {
                        txtInfo.setText("The account does not exist on the system!");
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