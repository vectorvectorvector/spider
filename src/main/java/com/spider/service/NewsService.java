package com.spider.service;


import com.spider.model.News;

import java.util.List;

public interface NewsService {

    List<News> getAllNews();

    News selectNewsByChannelName(String channelName);

    News selectNewsById(int newsId);

    int insertNews(News news);
}
