package com.ericsoft.bmob.model;

/**
 * 查询新闻标题
 * Created by Administrator on 2017/4/30/0030.
 */
public class BmobSearchNews {
    private SearchModelNews[] results;

    public SearchModelNews[] getResults() {
        return results;
    }

    public void setResults(SearchModelNews[] results) {
        this.results = results;
    }
}
