package com.example.myapplication.model;

import android.widget.ImageView;

public class Website {
    private int _id;
    private String _url;
    private ImageView image;
    private String title;

    public Website(int _id, String _url, ImageView image, String title) {
        this._id = _id;
        this._url = _url;
        this.image = image;
        this.title = title;
    }

    public Website(String url, String title) {
        this._url = url;
        this.title = title;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_url() {
        return _url;
    }

    public void set_url(String _url) {
        this._url = _url;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
