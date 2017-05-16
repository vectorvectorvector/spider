package com.ericsoft.bmob.model;

/**
 * 查询音乐评论
 * Created by Administrator on 2017/4/30/0030.
 */
public class BmobSearchMusicComment {
    private SearchModelMusicComment[] results;

    public SearchModelMusicComment[] getResults() {
        return results;
    }

    public void setResults(SearchModelMusicComment[] results) {
        this.results = results;
    }
}
