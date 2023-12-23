package com.example.myapplication.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.HistoryAdapter;
import com.example.myapplication.adapters.HistorySearchAdapter;
import com.example.myapplication.listeners.OnItemHistorySearchClickListener;
import com.example.myapplication.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private EditText edtHome;
    private TextView txtMic;
    private ImageView imgMore, imgQRCode, imgUser;//, imgAvt;
    private TextView txtName;
    private HistorySearchAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Paper.init(this);
        if (Paper.book().read("first") == null) {
            Paper.book().delete("current");
            Paper.book().delete("search");
            Paper.book().write("first", false);
        }

        initView();
        setAdapter();
    }

    private void setAdapter() {
        List<String> searchList = Paper.book().read("search");
        if (searchList != null) {
            adapter = new HistorySearchAdapter(searchList, new OnItemHistorySearchClickListener() {
                @Override
                public void onclick(String search) {
                    Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                    intent.putExtra("success", "his");
                    intent.putExtra("search",search);
                    startActivity(intent);
                }
            });
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
        }
    }
    @SuppressLint("SetTextI18n")
    private void initView() {
        edtHome = findViewById(R.id.edt_home_search);
        txtMic = findViewById(R.id.txt_home_mic);
        imgMore = findViewById(R.id.img_custom_actionbar_home_more);
        imgQRCode = findViewById(R.id.img_custom_actionbar_home_qr);
        imgUser = findViewById(R.id.img_custom_actionbar_home_user);
        recyclerView = findViewById(R.id.rc_search_his);

        txtName = findViewById(R.id.txt_name_main_home);
        User user = Paper.book().read("current");
        if (user != null) {
            txtName.setText("Hi " + user.getName() + "!");
        }

        edtHome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(intent);
                    view.clearFocus();
                }
            }
        });
        txtMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });
        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
        imgQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ScanQRCodeActivity.class);
                startActivity(intent);
            }
        });
        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = Paper.book().read("current");
                if (user == null) {
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    showPopupMenu(v);
                }
            }

            private void showPopupMenu(View anchorView) {
                // Create a custom view for the popup menu
                View popupView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.custom_popup_menu_acc, null);

                // Initialize the popup window
                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                setEventToPopup(popupView, popupWindow);
                // Show the popup window
                popupWindow.showAsDropDown(anchorView, 0, 0);
            }

            private void setEventToPopup(View popupView, PopupWindow popupWindow) {
                popupView.findViewById(R.id.custom_menu_acc_change).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Paper.book().delete("current");
                        popupWindow.dismiss();
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("search", result.get(0));
                startActivity(intent);
            }
        }
    }

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(HomeActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showPopupMenu(View anchorView) {
        // Create a custom view for the popup menu
        View popupView = LayoutInflater.from(this).inflate(R.layout.custom_popup_menu_more, null);

        // Initialize the popup window
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        setEventToPopup(popupView, popupWindow);
        // Show the popup window
        popupWindow.showAsDropDown(anchorView, 0, 0);
    }

    private void setEventToPopup(View popupView, PopupWindow popupWindow) {
        User user = Paper.book().read("current");
        popupView.findViewById(R.id.menu_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (user != null) {
                    intent = new Intent(HomeActivity.this, HistoryActivity.class);
                } else {
                    intent = new Intent(HomeActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        popupView.findViewById(R.id.menu_favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (user != null) {
                    intent = new Intent(HomeActivity.this, BookmarkActivity.class);
                } else {
                    intent = new Intent(HomeActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
    }
}