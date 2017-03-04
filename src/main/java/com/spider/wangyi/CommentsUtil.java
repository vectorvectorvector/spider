package com.spider.wangyi;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhouchao on 2017/3/3.
 */
public class CommentsUtil {
    private Logger log = Logger.getLogger(CommentsUtil.class);

    public void getComments(String url) {
        String TargetURL = url;
//        TargetURL = "http://comment.news.163.com/news2_bbs/CEJ5BP5100018AOP.html";
        TargetURL = "http://comment.home.163.com/home_bbs/CEE21EO0001081EI.html";
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
            getHotAndMainReplies(hotReplies);
            Element mainReplies = doc.getElementById("mainReplies");//最新评论
            getHotAndMainReplies(mainReplies);

//            System.out.println(page.asXml());
        } catch (IOException e) {
            log.error("CommentsUtil IOException:" + e.getMessage());
        } finally {
            webClient.close();
        }
    }

    //获取热评和最新评论
    public void getHotAndMainReplies(Element replies) {
        Element hotRepliesList = replies.getElementsByClass("list").first();
        Elements lists = hotRepliesList.select("div.reply").select(".essence");
        for (Element element : lists) { //获取一条评论的详细内容
            Element author = element.getElementsByClass("author").first();
            String head = author.getElementsByClass("userFace").first().getElementsByTag("img").first().attr("src");

            Elements usernameElements = author.getElementsByTag("a");//有可能越界
            String username = usernameElements.size() >= 3 ? usernameElements.get(2).text() : "";

            Elements pos = author.getElementsByClass("from-logon");
            String position = pos.size() > 0 ? pos.first().text() : "";
            String postTime = element.getElementsByClass("postTime").first().text();//多了字符：举报

            Element body = element.getElementsByClass("body").first().getElementsByTag("div").first();
            Elements commentBox = body.getElementsByClass("commentBox");//表示是回复评论的
            String commentWithoutBox = "";//直接回复的没盖楼
            String commentWithBox = "";//盖楼回复
            List<String> boxList = new LinkedList<>();
            if (commentBox.size() == 0) {//表示是直接回复的
//                    commentWithoutBox = body.getElementsByTag("div").first().text();
                commentWithoutBox = body.text();
                System.out.println(commentWithoutBox);
            } else {
                //获取盖楼评论
                Elements contents = body.getElementsByClass("content");
                for (Element content : contents) {
                    boxList.add(content.text());
                    System.out.println(content.text());
                }
            }
        }
    }

}