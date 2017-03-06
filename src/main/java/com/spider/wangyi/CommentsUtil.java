package com.spider.wangyi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.spider.model.Comment;
import com.spider.model.HotAndMainComments;
import com.spider.model.News;
import com.spider.service.NewsService;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhouchao on 2017/3/3.
 */
public class CommentsUtil {
    private Logger log = Logger.getLogger(CommentsUtil.class);
    private News news = new News();//保存爬取的信息
    private HotAndMainComments hotAndMainComments = new HotAndMainComments();

    @Resource
    private NewsService newsService ;

    public void getComments(String url) {
        String TargetURL = url;
        TargetURL = "http://comment.news.163.com/news2_bbs/CEJ5BP5100018AOP.html";
//        TargetURL = "http://comment.home.163.com/home_bbs/CEE21EO0001081EI.html";

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

            //获取标题和原网址
            Elements Etitle = doc.select("div.wrapper").select(".origPost");
            String title = Etitle.first().getElementsByTag("a").first().text();
            news.setTitle(title);
            String Url = Etitle.first().getElementsByTag("a").first().attr("href");
            news.setDocurl(Url);

            Element hotReplies = doc.getElementById("hotReplies");//热评
            getHotAndMainReplies(hotReplies, true);
            Element mainReplies = doc.getElementById("mainReplies");//最新评论
            getHotAndMainReplies(mainReplies, false);

            JSONObject comment = (JSONObject) JSON.toJSON(hotAndMainComments);
            news.setComment(comment.toJSONString());

            newsService.insertNews(news);

//            System.out.println(page.asXml());
        } catch (IOException e) {
            log.error("CommentsUtil IOException:" + e.getMessage());
        } finally {
            webClient.close();
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
            comment.setAuthor(username);

            Elements pos = author.getElementsByClass("from-logon");
            String position = pos.size() > 0 ? pos.first().text() : "";
            comment.setPosition(position);
            String postTime = element.getElementsByClass("postTime").first().text();//多了字符：举报
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
                Date date = sdf.parse(postTime);
                comment.setCommentDate(date);
            } catch (ParseException e) {
                System.out.println(e.getMessage());
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
                comment.setBoxList(boxList);
            }
            if (hotOrMain) {//热评
                hotAndMainComments.getHotComment().add(comment);
            } else {//最新评论
                hotAndMainComments.getMainComment().add(comment);
            }
        }
    }
}