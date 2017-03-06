package com.spider.model;

import java.util.Date;
import java.util.List;

/**
 * Created by zhouchao on 2017/3/5.
 */
public class Comment {
    private String author;//评论的用户名
    private String head;//评论者的头像
    private String position;//评论者的位置
    private Date commentDate;//评论时间
    private String commentWithoutBox;//没盖楼的回复
    private List<String> boxList;//盖楼的形式回复

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public String getCommentWithoutBox() {
        return commentWithoutBox;
    }

    public void setCommentWithoutBox(String commentWithoutBox) {
        this.commentWithoutBox = commentWithoutBox;
    }

    public List<String> getBoxList() {
        return boxList;
    }

    public void setBoxList(List<String> boxList) {
        this.boxList = boxList;
    }
}
