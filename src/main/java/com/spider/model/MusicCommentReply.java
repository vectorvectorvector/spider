package com.spider.model;

/**
 * Created by zhouchao on 2017/3/5.
 */
public class MusicCommentReply {
    private MusicUser user;
    private String content;//评论内容

    public MusicUser getUser() {
        return user;
    }

    public void setUser(MusicUser user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
