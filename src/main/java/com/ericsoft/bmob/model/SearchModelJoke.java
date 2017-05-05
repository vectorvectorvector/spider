package com.ericsoft.bmob.model;

import java.util.Date;

/**
 * Created by Administrator on 2017/5/5/0005.
 */
public class SearchModelJoke {
    private String objectId;
    private String username;//发表的用户名
    private String head;//用户头像
    private String content;//内容
    private String image;//图片
    private String ShenHuiFu;//神回复
    private String comment;//用户评论
    private String from;//来源

    private Date createdAt;
    private Date updatedAt;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShenHuiFu() {
        return ShenHuiFu;
    }

    public void setShenHuiFu(String shenHuiFu) {
        ShenHuiFu = shenHuiFu;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
