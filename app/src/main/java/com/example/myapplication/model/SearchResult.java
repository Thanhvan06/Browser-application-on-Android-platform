package com.example.myapplication.model;

public class SearchResult {
    private String title;
    private String url;
    private String imgUrl;
    private String snippet;
    private String domain;

    public SearchResult(String title, String url, String imgUrl, String snippet, String domain) {
        this.title = title;
        this.url = url;
        this.imgUrl = imgUrl;
        this.snippet = snippet;
        this.domain = domain;
    }

    public SearchResult() {

    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }
}
