package com.example.myapplication.listeners;

public interface OnItemHistoryClickListener {
    void onOpen(String url);
    void onDelete(String url);
    void onShare(String url);
    void onBookmark(String url,String title);
}
