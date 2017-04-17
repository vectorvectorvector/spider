package com.spider.wangyi;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.spider.model.News;
import com.spider.service.impl.NewsServiceImpl;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhouchao on 2017/4/17/0017.
 * 根据网易的网页获取文章标题，链接，评论地址等信息
 */
@Service
public class WangYiUtil {
    private News news;//保存爬取的信息
    private Logger log = Logger.getLogger(WangYiUtil.class);
    @Autowired
    private NewsServiceImpl newsService;
    private int newsid;//新闻在数据库中存放的id

    @Resource
    private CommentsUtil commentsUtil;

    String[] newsClass = {"red", "gray", "rank"};

    public WangYiUtil() {
        news = new News();//保存爬取的信息
    }

    public void getNewsUrl(String url) {
        String TargetURL = url;
//        TargetURL = "http://news.163.com/special/0001386F/rank_news.html";

        //模拟一个浏览器
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        //设置webClient的相关参数
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        //模拟浏览器打开一个目标网址
        final HtmlPage page;
        try {
            page = webClient.getPage(TargetURL);
            String pageXml = page.asXml(); // 以xml的形式获取响应文本
            Document doc = Jsoup.parse(pageXml);

            for (String newsclass : newsClass) {
                Elements newsLists = doc.getElementsByClass(newsclass);
                for (Element element : newsLists) {
                    String Url = element.getElementsByTag("a").first().attr("href");
                    getNewsCommentUrl(Url);
                }
            }
        } catch (IOException e) {
            log.error("WangYiUtil IOException:" + e.getMessage());
        } catch (Exception e) {
            log.error("WangYiUtil Exception:" + e.getMessage());
        } finally {
//            webClient.close();
        }
    }

    /**
     * 根据新闻网址获取评论地址
     *
     * @param newsUrl
     */
    public void getNewsCommentUrl(String newsUrl) {
        String TargetURL = newsUrl;
//        TargetURL = "http://news.163.com/17/0417/12/CI7OFK8D0001899O.html";

        //模拟一个浏览器
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        //设置webClient的相关参数
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        //模拟浏览器打开一个目标网址
        final HtmlPage page;
        try {
            page = webClient.getPage(TargetURL);
            String pageXml = page.asXml(); // 以xml的形式获取响应文本
            Document doc = Jsoup.parse(pageXml);

            Element post_content_main = doc.getElementsByClass("post_content_main").first();


            String title = post_content_main.getElementsByTag("h1").first().text();
            news.setTitle(title);

            Element commBody = post_content_main.getElementsByClass("post_topshare_wrap").first();
            Element commLink = commBody.getElementsByClass("post_tie_top").first().getElementsByTag("a").get(1);
            String commUrl = commLink.attr("href");
            int commNum = Integer.parseInt(commLink.text());

            if (commNum >= 1000) {//爬取评论
                news.setUrl(newsUrl);

                String time = post_content_main.getElementsByClass("post_time_source").first().text().substring(0, 19);
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = sdf.parse(time);
                    news.setTime(date);
                } catch (ParseException e) {
                    log.error("Date ParseException:" + e.getMessage());
                }
                news.setChannelname("news");
                news.setContent("");
                news.setComment("");
                news.setImgurl("-");

                newsid = newsService.selectNewsId(news.getUrl());
                if (newsid == 0) {
                    newsService.insertNews(news);
                    newsid = news.getNews_id();
                }

                commentsUtil.getComments(commUrl);

            }


        } catch (IOException e) {
            log.error("WangYiUtil IOException:" + e.getMessage());
        } catch (Exception e) {
            log.error("WangYiUtil Exception:" + e.getMessage());
        } finally {
//            webClient.close();
        }
    }

}
