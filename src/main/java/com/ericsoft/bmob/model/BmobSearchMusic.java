package com.ericsoft.bmob.model;

/**
 * 查询音乐
 * Created by Administrator on 2017/4/30/0030.
 */
public class BmobSearchMusic {
    private SearchModelMusic[] results;

    public SearchModelMusic[] getResults() {
        return results;
    }

    public void setResults(SearchModelMusic[] results) {
        this.results = results;
    }
}
