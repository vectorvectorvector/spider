package com.spider.model;

/**
 * Created by zhouchao on 2017/3/5.
 */
public class MusicComment {
    private MusicUser user;
    private MusicCommentReply[] beReplied;
    private int likedCount;
    private String content;//评论内容

    public MusicUser getUser() {
        return user;
    }

    public void setUser(MusicUser user) {
        this.user = user;
    }

    public MusicCommentReply[] getBeReplied() {
        return beReplied;
    }

    public void setBeReplied(MusicCommentReply[] beReplied) {
        this.beReplied = beReplied;
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
}
