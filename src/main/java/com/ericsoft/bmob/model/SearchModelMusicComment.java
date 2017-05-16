package com.ericsoft.bmob.model;

import java.util.Date;

/**
 * Created by Administrator on 2017/5/5/0005.
 */
public class SearchModelMusicComment {
    private String objectId;
    private String avatarUrl;
    private String nickname;
    private int likedCount;
    private String content;//评论内容
    private String musicId;//对应的音乐的ID

    //这是回复某人的评论的
    private String beRepliedContent;//针对该用户的评论内容
    private String beRepliedAvatarUrl;//针对的用户头像
    private String beRepliedNickname;//针对的用户名

    private Date createdAt;
    private Date updatedAt;

    public void init() {
        avatarUrl = "";
        nickname = "";
        likedCount = 0;
        content = "";
        musicId = "";
        beRepliedAvatarUrl = "";
        beRepliedContent = "";
        beRepliedNickname = "";
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(int likedCount) {
        this.likedCount = likedCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBeRepliedContent() {
        return beRepliedContent;
    }

    public void setBeRepliedContent(String beRepliedContent) {
        this.beRepliedContent = beRepliedContent;
    }

    public String getBeRepliedAvatarUrl() {
        return beRepliedAvatarUrl;
    }

    public void setBeRepliedAvatarUrl(String beRepliedAvatarUrl) {
        this.beRepliedAvatarUrl = beRepliedAvatarUrl;
    }

    public String getBeRepliedNickname() {
        return beRepliedNickname;
    }

    public void setBeRepliedNickname(String beRepliedNickname) {
        this.beRepliedNickname = beRepliedNickname;
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
