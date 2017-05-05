package com.ericsoft.bmob.model;

import java.util.Date;

/**
 * Created by Administrator on 2017/4/30/0030.
 */
public class SearchModelComment {
    private String objectId;
    private String news_id;
    private String username;//评论的用户名
    private String head;//评论者的头像
    private String positions;//评论者的位置
    private String commentDate;//评论时间
    private String commentWithoutBox;//没盖楼的回复
    private String boxList;//盖楼的形式回复
    private Date createdAt;
    private Date updatedAt;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
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

    public String getPositions() {
        return positions;
    }

    public void setPositions(String positions) {
        this.positions = positions;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getCommentWithoutBox() {
        return commentWithoutBox;
    }

    public void setCommentWithoutBox(String commentWithoutBox) {
        this.commentWithoutBox = commentWithoutBox;
    }

    public String getBoxList() {
        return boxList;
    }

    public void setBoxList(String boxList) {
        this.boxList = boxList;
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
}
