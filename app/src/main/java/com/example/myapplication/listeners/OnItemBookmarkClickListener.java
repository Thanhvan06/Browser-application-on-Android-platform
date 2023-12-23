package com.example.myapplication.listeners;

public interface OnItemBookmarkClickListener {
    void onOpen(String url);
    void onDelete(String url);
    void onShare(String url);
}
