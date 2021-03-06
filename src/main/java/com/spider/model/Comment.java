package com.spider.model;

import java.util.Date;
import java.util.List;

/**
 * Created by zhouchao on 2017/3/5.
 */
public class Comment {
    private int id;
    private String news_id;
    private String username;//评论的用户名
    private String head;//评论者的头像
    private String positions;//评论者的位置
    private String commentDate;//评论时间
    private String commentWithoutBox;//没盖楼的回复
    private String boxList;//盖楼的形式回复

    public Comment() {
        username = "";
        head = "";
        positions = "";
        commentWithoutBox = "";
        boxList = "";
        news_id = "";
        commentDate = "";
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
