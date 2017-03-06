package com.spider.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouchao on 2017/3/6.
 */
public class HotAndMainComments {
    private List<Comment> hotComment;
    private List<Comment> mainComment;

    public HotAndMainComments() {
        hotComment = new ArrayList<Comment>();
        mainComment = new ArrayList<Comment>();
    }

    public List<Comment> getHotComment() {
        return hotComment;
    }

    public void setHotComment(List<Comment> hotComment) {
        this.hotComment = hotComment;
    }

    public List<Comment> getMainComment() {
        return mainComment;
    }

    public void setMainComment(List<Comment> mainComment) {
        this.mainComment = mainComment;
    }
}
