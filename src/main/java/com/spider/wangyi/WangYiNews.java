package com.spider.wangyi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.spider.model.News;
import com.spider.service.NewsService;
import com.spider.util.HttpUtil;
import com.spider.util.TxtUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by 周超 on 2017/02/21.
 */
@Service
public class WangYiNews {
//    private Logger log = Logger.getLogger(WangYiNews.class);

    @Resource
    private NewsService newsService;


    @Resource
    private WangYiUtil wangYiUtil;

    @Resource
    private HttpUtil httpUtil;

    @Value("#{configProperties['wangyinews.url']}")
    private String wangyi_news;

    @Value("#{configProperties['wangyinews.details']}")
    private String wangyi_news_details;

    @Value("#{configProperties['wangyinews.page']}")
    private int page;//当前页数

    @Value("#{configProperties['wangyinews.limit']}")
    private int limit;//新闻条数

    @Value("#{configProperties['wangyi_rank']}")
    private String wangyi_rank;//包含各种类型新闻的排行榜

    @Value("#{configProperties['wangyi_news_rank']}")
    private String wangyi_news_rank;//新闻排行榜

    @Value("#{configProperties['wangyi_ent_rank']}")
    private String wangyi_ent_rank;//娱乐排行榜

    @Value("#{configProperties['wangyi_sports_rank']}")
    private String wangyi_sports_rank;//体育排行榜

    @Value("#{configProperties['wangyi_finance_rank']}")
    private String wangyi_finance_rank;//财经排行榜

    @Value("#{configProperties['wangyi_tech_rank']}")
    private String wangyi_tech_rank;//科技排行榜

    @Value("#{configProperties['wangyi_car_rank']}")
    private String wangyi_car_rank;//汽车排行榜

    @Value("#{configProperties['wangyi_lady_rank']}")
    private String wangyi_lady_rank;//女人排行榜

    @Value("#{configProperties['wangyi_house_rank']}")
    private String wangyi_house_rank;//房产排行榜

    @Value("#{configProperties['wangyi_travel_rank']}")
    private String wangyi_travel_rank;//旅游排行榜

    @Value("#{configProperties['wangyi_edu_rank']}")
    private String wangyi_edu_rank;//教育排行榜

    @Value("#{configProperties['wangyi_whole_rank']}")
    private String wangyi_whole_rank;//全站排行榜


    // 0 新闻; 1 war 军事; 2	sport 体育; 3 tech 科技; 4 edu 教育; 5 ent 娱乐; 6 money 财经;
    // 7 gupiao 股票; 8 travel 旅游; 9 lady	女人; 10 汽车; 11 房产; 12 全站
    private String type;//新闻类型
    private String[] types = {"news", "war", "sport", "tech", "edu", "ent", "money", "gupiao", "travel", "lady", "car", "house", "whole"};
    private int simpleId;//查看详细内容的

    private ApplicationContext ac;

    /**
     * 爬取新闻保存到数据库中
     *
     * @return
     */
    public void getNews() {
//        log.info("WangYiNews...");
//        System.out.println("WangYiNews...");
        for (String type : types) {
            String jsonString = httpUtil.get(wangyi_news + "type=" + type + "&page=" + page + "&limit=" + limit);
            JSONObject object = JSON.parseObject(jsonString);
            JSONArray array = object.getJSONArray("list");
            List<News> lists = JSON.parseArray(array.toJSONString(), News.class);
            for (News news : lists) {
                newsService.insertNews(news);
            }
        }
    }

    public void getWangyiComments() {
////        wangYiUtil.getNewsUrl(wangyi_rank, types[0]);//各类排行榜
//        wangYiUtil.getNewsUrl(wangyi_news_rank, types[0]);//新闻排行榜
//        wangYiUtil.getNewsUrl(wangyi_sports_rank, types[2]);//体育排行榜
//        wangYiUtil.getNewsUrl(wangyi_tech_rank, types[3]);//科技排行榜
//        wangYiUtil.getNewsUrl(wangyi_edu_rank, types[4]);//教育排行榜
//        wangYiUtil.getNewsUrl(wangyi_ent_rank, types[5]);//娱乐排行榜
//        wangYiUtil.getNewsUrl(wangyi_finance_rank, types[6]);//财经排行榜
//        wangYiUtil.getNewsUrl(wangyi_travel_rank, types[8]);//旅游排行榜
//        wangYiUtil.getNewsUrl(wangyi_lady_rank, types[9]);//女人排行榜
//        wangYiUtil.getNewsUrl(wangyi_car_rank, types[10]);//汽车排行榜
//        wangYiUtil.getNewsUrl(wangyi_house_rank, types[11]);//房产排行榜
//        wangYiUtil.getNewsUrl(wangyi_whole_rank, types[12]);//全站排行榜

//        TxtUtil txtUtil = new TxtUtil();//测试
//        txtUtil.appendInfoToTxt("Started--" + new Date().toString());
        wangYiUtil.getNewsUrl(wangyi_news_rank, types[0]);//新闻排行榜
        Integer[] num = {0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        List<Integer> list = Arrays.asList(num);
        Collections.shuffle(list);
        for (int i = 0; i < 3; i++) {
            switch (list.get(i)) {
                case 0:
                case 1:
                    wangYiUtil.getNewsUrl(wangyi_news_rank, types[0]);//新闻排行榜
                    break;
                case 2:
                case 7:
                    wangYiUtil.getNewsUrl(wangyi_sports_rank, types[2]);//体育排行榜
                    break;
                case 3:
                case 13:
                    wangYiUtil.getNewsUrl(wangyi_ent_rank, types[5]);//娱乐排行榜
                    break;
                case 4:
                    wangYiUtil.getNewsUrl(wangyi_edu_rank, types[4]);//教育排行榜
                    break;
                case 5:
                    wangYiUtil.getNewsUrl(wangyi_tech_rank, types[3]);//科技排行榜
                    break;
                case 6:
                    wangYiUtil.getNewsUrl(wangyi_finance_rank, types[6]);//财经排行榜
                    break;
                case 8:
                    wangYiUtil.getNewsUrl(wangyi_travel_rank, types[8]);//旅游排行榜
                    break;
                case 9:
                    wangYiUtil.getNewsUrl(wangyi_lady_rank, types[9]);//女人排行榜
                    break;
                case 10:
                    wangYiUtil.getNewsUrl(wangyi_car_rank, types[10]);//汽车排行榜
                    break;
                case 11:
                    wangYiUtil.getNewsUrl(wangyi_house_rank, types[11]);//房产排行榜
                    break;
                case 12:
                    wangYiUtil.getNewsUrl(wangyi_whole_rank, types[12]);//全站排行榜
                    break;
            }
        }
//        txtUtil.appendInfoToTxt("Stopped--" + new Date().toString());
    }


}
