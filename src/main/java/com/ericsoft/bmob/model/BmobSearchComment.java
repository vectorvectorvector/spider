package com.ericsoft.bmob.model;

/**
 * 查询评论
 * Created by Administrator on 2017/4/30/0030.
 */
public class BmobSearchComment {
    private SearchModelComment[] results;

    public SearchModelComment[] getResults() {
        return results;
    }

    public void setResults(SearchModelComment[] results) {
        this.results = results;
    }
}
