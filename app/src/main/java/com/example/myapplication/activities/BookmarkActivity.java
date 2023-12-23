package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.BookmarkAdapter;
import com.example.myapplication.dbhandler.MyDBBookmarkHandler;
import com.example.myapplication.dialogs.ConfirmationDialog;
import com.example.myapplication.listeners.ConfirmationDialogListener;
import com.example.myapplication.listeners.OnItemBookmarkClickListener;
import com.example.myapplication.model.User;
import com.example.myapplication.model.Website;

import java.util.List;

import io.paperdb.Paper;

public class BookmarkActivity extends AppCompatActivity implements OnItemBookmarkClickListener {
    private MyDBBookmarkHandler myDBBookmarkHandler = new MyDBBookmarkHandler(this, null, null, 1);
    private RecyclerView recyclerView;
    private List<Website> bookmarks;
    private BookmarkAdapter adapter;
    private ImageView imgBack, imgClear;
    private TextView txtTitle;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        Paper.init(this);

        initView();
    }

    private void initView() {
        imgBack = findViewById(R.id.custom_actionbar_title_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgClear = findViewById(R.id.custom_actionbar_title_clear);
        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmationDialog.showConfirmationDialog(BookmarkActivity.this, "Delete all favorite", "Are you sure you want to delete the entire favorites list?", new ConfirmationDialogListener() {
                    @Override
                    public void onConfirm() {
                        User user = Paper.book().read("current");
                        if (user != null) {
                            myDBBookmarkHandler.clearBookmark(user.getId());
                            Toast.makeText(BookmarkActivity.this, "Deleted all favorite", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        txtTitle = findViewById(R.id.custom_actionbar_title_name);
        txtTitle.setText("Bookmarks");

        recyclerView = findViewById(R.id.rc_bookmark);

        setAdapter();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setAdapter() {
        User user = Paper.book().read("current");
        if (user != null) {
            bookmarks = myDBBookmarkHandler.databaseToString(user.getId());
            adapter = new BookmarkAdapter(bookmarks, this);

            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onOpen(String url) {
        Intent intent = new Intent(BookmarkActivity.this, BrowserActivity.class);
        intent.putExtra("success", "favorite");
        intent.putExtra("url", url);
        startActivity(intent);
    }

    @Override
    public void onDelete(String url) {
        User user = Paper.book().read("current");
        if (user != null) {
            myDBBookmarkHandler.deleteUrl(url, user.getId());
            setAdapter();
            Toast.makeText(this, "Removed a page path from favorites", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onShare(String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String body = "Sharing";
        String sub = url;
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra(Intent.EXTRA_TEXT, sub);
        startActivity(Intent.createChooser(intent, "share using"));
    }
}