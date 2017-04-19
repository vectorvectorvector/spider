package com.spider.wangyi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.spider.model.Comment;
import com.spider.model.News;
import com.spider.service.CommentService;
import com.spider.service.impl.NewsServiceImpl;
import com.spider.util.TxtUtil;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhouchao on 2017/4/17/0017.
 * 根据网易的网页获取文章标题，链接，评论地址等信息
 */
@Service
public class WangYiUtil {
    private News news;//保存爬取的信息
    private Logger log = Logger.getLogger(WangYiUtil.class);

    String[] newsClass = {"red", "gray", "rank"};

    @Autowired
    private NewsServiceImpl newsService;
    @Autowired
    private CommentService commentService;
    private int newsid;//新闻在数据库中存放的id
    private int displayCount;//评论数量

    @Value("#{configProperties['wangyi_page']}")
    private int wangyi_page;//新闻页数

    @Value("#{configProperties['wangyi_comment_num']}")
    private int commentNum;//每页显示的评论数量

    TxtUtil txtUtil = new TxtUtil();//测试
//    private int count = 0;

    @Value("#{configProperties['wangyi_rank_limit']}")
    private int wangyi_rank_limit;//排行榜限制条件

    @Value("#{configProperties['wangyi_commit_limit']}")
    private int wangyi_commit_limit;//评论数量限制条件

    public WangYiUtil() {
        news = new News();//保存爬取的信息
    }

    public void getNewsUrl(String url, String type) {
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

            Elements tables = doc.getElementsByTag("table");
//            for (Element e : tables) {
            for (int i = 0; i < tables.size(); i += 3) {
                Element e = tables.get(i);
                Elements trs = e.getElementsByTag("tr");
                for (Element tr : trs) {
                    for (String newsclass : newsClass) {
                        Elements td = tr.getElementsByClass(newsclass);
                        if (td.size() > 0) {

                            long startTime = System.currentTimeMillis();    //获取开始时间
                            Element num = tr.getElementsByClass("cBlue").first();//参与评论的数量
                            int nums = Integer.parseInt(num.text());
                            String Url = td.first().getElementsByTag("a").first().attr("href");
                            if (nums >= wangyi_rank_limit) {
                                getNewsCommentUrl(Url, type);
                            }
                            long endTime = System.currentTimeMillis();    //获取结束时间
                            txtUtil.appendInfoToTxt((endTime - startTime) / 1000 + ":" + Url);//运行时间
                            break;
                        }
                    }

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
     * @param type    //类型
     */
    public void getNewsCommentUrl(String newsUrl, String type) {
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

            Element commBody = post_content_main.getElementsByClass("post_comment_toolbar").first();
            Element commLink = commBody.getElementsByClass("post_comment_tiecount").first().getElementsByTag("a").first();
            String commUrl = commLink.attr("href");
            int commNum = Integer.parseInt(commLink.text());
//            Element commBody = post_content_main.getElementsByClass("post_topshare_wrap").first();
//            Element commLink = commBody.getElementsByClass("post_tie_top").first().getElementsByTag("a").get(1);
//            String commUrl = commLink.attr("href");
//            int commNum = Integer.parseInt(commLink.text());

            if (commNum >= wangyi_commit_limit) {//爬取评论
                news.setUrl(newsUrl);

                String time = post_content_main.getElementsByClass("post_time_source").first().text().substring(0, 19);
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = sdf.parse(time);
                    news.setTime(date);
                } catch (ParseException e) {
                    log.error("Date ParseException:" + e.getMessage());
                }
                news.setChannelname(type);//类型
                news.setContent("");
                news.setComment("");
                news.setImgurl("-");

//                newsid = newsService.selectNewsId(news.getUrl());
//                if (newsid == 0) {
//                    newsService.insertNews(news);
//                    newsid = news.getNews_id();
//                }
                getComments(commUrl);
            }
        } catch (HttpHostConnectException e) {
            log.error("WangYiUtil HttpHostConnectException:" + e.getMessage());
        } catch (IOException e) {
            log.error("WangYiUtil IOException:" + e.getMessage());
        } catch (Exception e) {
            log.error("WangYiUtil Exception:" + e.getMessage());
        } finally {
//            webClient.close();
        }
    }


    public void getComments(String url) {
        String TargetURL = url;

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

            Element hotReplies = doc.getElementById("hotReplies");//热评
            //获取评论数量
           /* Elements ECount = hotReplies.getElementsByClass("displayCount");
            if (ECount.size() > 0) {
                displayCount = Integer.parseInt(ECount.first().getElementsByTag("em").text());
            }

            if (displayCount > 1000)*/
            {

                //将新闻保存到数据库中
                newsid = newsService.selectNewsId(news.getUrl());
                if (newsid == 0) {
                    newsService.insertNews(news);
                    newsid = news.getNews_id();
                }
                getHotAndMainReplies(hotReplies, true);
                Element mainReplies = doc.getElementById("mainReplies");//最新评论
                getHotAndMainReplies(mainReplies, false);

                //翻页获取最新评论
                if (displayCount > 0) {
//                    int pageNum = displayCount / commentNum < wangyi_page ? displayCount / commentNum : wangyi_page;
                    int pageNum = wangyi_page;
                    if (displayCount / commentNum < pageNum) {
                        pageNum = displayCount / commentNum;
                    }
                    for (int i = 2; i < pageNum; i++) {
                        String script = "javascript:tiePage.showPage(" + i + ");";
                        page.executeJavaScript(script);
                        String xml = page.asXml(); // 以xml的形式获取响应文本
                        Document document = Jsoup.parse(xml);
                        mainReplies = document.getElementById("mainReplies");//最新评论
                        getHotAndMainReplies(mainReplies, false);
                    }
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

    //获取热评和最新评论
    public void getHotAndMainReplies(Element replies, boolean hotOrMain) {
        Element hotRepliesList = replies.getElementsByClass("list").first();
        Elements lists = hotRepliesList.select("div.reply").select(".essence");
        for (Element element : lists) { //获取一条评论的详细内容
            Comment comment = new Comment();

            Element author = element.getElementsByClass("author").first();

            String head = author.getElementsByClass("userFace").first().getElementsByTag("img").first().attr("src");
            comment.setHead(head);

            Elements usernameElements = author.getElementsByTag("a");//有可能越界
            String username = usernameElements.size() >= 3 ? usernameElements.get(2).text() : "";
            comment.setUsername(username);

            Elements pos = author.getElementsByClass("from-logon");
            String position = pos.size() > 0 ? pos.first().text() : "";
            comment.setPositions(position);
            String postTime = element.getElementsByClass("postTime").first().text().substring(3);//多了字符：举报
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date = sdf.parse(postTime);
                comment.setCommentDate(date);
            } catch (ParseException e) {
                log.error("ParseException:" + e.getMessage());
            }

            Element body = element.getElementsByClass("body").first().getElementsByTag("div").first();
            Elements commentBox = body.getElementsByClass("commentBox");//表示是回复评论的
            String commentWithoutBox = "";//直接回复的没盖楼
            List<String> boxList = new LinkedList<>();
            if (commentBox.size() == 0) {//表示是直接回复的
//                    commentWithoutBox = body.getElementsByTag("div").first().text();
                commentWithoutBox = body.text();
                comment.setCommentWithoutBox(commentWithoutBox);
                System.out.println(commentWithoutBox);
            } else {
                //获取盖楼评论
                Elements contents = body.getElementsByClass("content");
                for (Element content : contents) {
                    boxList.add(content.text());
                    System.out.println(content.text());
                }
                String jsonString = JSON.toJSONString(boxList);
                JSONArray boxlist = JSONArray.parseArray(jsonString);
                comment.setBoxList(boxlist.toJSONString());
            }
            if (newsid != 0) {
                try {
                    comment.setNews_id(newsid);
                    Comment tmp = commentService.selectCommentByComment(comment);
                    if (tmp == null) {
                        commentService.insertComment(comment);
                    }
                } catch (Exception e) {
                    log.error("Exception:" + e.getMessage());
                }
            }
        }
    }

}
