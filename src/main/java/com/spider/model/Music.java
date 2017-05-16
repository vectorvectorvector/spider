package com.spider.model;

/**
 * Created by Administrator on 2017/5/15/0015.
 */
public class Music {
    private String title;
    private String url;
    private String author;
    private String comment;

    public void init() {
        title = "";
        url = "";
        author = "";
        comment = "";
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
