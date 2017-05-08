package com.spider.model;

/**
 * Created by 周超 on 2017/02/21.
 */
public class Health {
    private String imgurl;
    private String url;//网页的URL
    private String title;
    private String subTitle;//简要内容
    private String content;
    private String comment;
    private String origin;

    public Health() {
        init();
    }

    public void init() {
        imgurl = "";
        url = "";
        subTitle = "";
        title = "";
        content = "";
        comment = "";
        origin = "";
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
