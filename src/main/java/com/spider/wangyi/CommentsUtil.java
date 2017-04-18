package com.spider.wangyi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.spider.model.Comment;
import com.spider.model.News;
import com.spider.service.CommentService;
import com.spider.service.impl.NewsServiceImpl;
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
 * Created by zhouchao on 2017/3/3.
 */
@Service
public class CommentsUtil {

//    private Logger log = Logger.getLogger(CommentsUtil.class);
//    @Autowired
//    private NewsServiceImpl newsService;
//    @Autowired
//    private CommentService commentService;
//    private int newsid;//新闻在数据库中存放的id
//    private int displayCount;//评论数量
//
//    @Value("#{configProperties['wangyi_page']}")
//    private int wangyi_page;//新闻页数
//
//    @Value("#{configProperties['wangyi_comment_num']}")
//    private int commentNum;//每页显示的评论数量
//
//    public void getComments(String url) {
//        String TargetURL = url;
////        TargetURL = "http://comment.news.163.com/news2_bbs/CEJ5BP5100018AOP.html";
////        TargetURL = "http://comment.news.163.com/news2_bbs/CI82IBA80001899O.html";
//
//        //模拟一个浏览器
//        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
//        //设置webClient的相关参数
//        webClient.getOptions().setJavaScriptEnabled(true);
//        webClient.getOptions().setActiveXNative(false);
//        webClient.getOptions().setCssEnabled(false);
//        webClient.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常
//        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
//        //模拟浏览器打开一个目标网址
//        final HtmlPage page;
//        try {
//            page = webClient.getPage(TargetURL);
//            String pageXml = page.asXml(); // 以xml的形式获取响应文本
//            Document doc = Jsoup.parse(pageXml);
//
//            Element hotReplies = doc.getElementById("hotReplies");//热评
//            //获取评论数量
//            Elements ECount = hotReplies.getElementsByClass("displayCount");
//            if (ECount.size() > 0) {
//                displayCount = Integer.parseInt(ECount.first().getElementsByTag("em").text());
//            }
//
//            getHotAndMainReplies(hotReplies, true);
//            Element mainReplies = doc.getElementById("mainReplies");//最新评论
//            getHotAndMainReplies(mainReplies, false);
//
//            //翻页获取最新评论
//            if (displayCount > 0) {
//                int pageNum = displayCount / commentNum < wangyi_page ? displayCount / commentNum : wangyi_page;
//                for (int i = 2; i < pageNum; i++) {
//                    String script = "javascript:tiePage.showPage(" + i + ");";
//                    page.executeJavaScript(script);
////                    ScriptResult sr = page.executeJavaScript(script);
////                    final HtmlPage newPage = (HtmlPage) sr.getNewPage();
//                    String xml = page.asXml(); // 以xml的形式获取响应文本
//                    Document document = Jsoup.parse(xml);
//                    mainReplies = document.getElementById("mainReplies");//最新评论
//                    getHotAndMainReplies(mainReplies, false);
////                    getNextPageContent(newPage);
//                }
//            }
//
//        } catch (IOException e) {
//            log.error("CommentsUtil IOException:" + e.getMessage());
//        } catch (Exception e) {
//            log.error("CommentsUtil Exception:" + e.getMessage());
//        } finally {
////            webClient.close();
//        }
//    }
//
//    public void getNextPageContent(HtmlPage page) {
//        String pageXml = page.asXml(); // 以xml的形式获取响应文本
//        Document doc = Jsoup.parse(pageXml);
//        Element mainReplies = doc.getElementById("mainReplies");//最新评论
//        getHotAndMainReplies(mainReplies, false);
//    }
//
//    //获取热评和最新评论
//    public void getHotAndMainReplies(Element replies, boolean hotOrMain) {
//        Element hotRepliesList = replies.getElementsByClass("list").first();
//        Elements lists = hotRepliesList.select("div.reply").select(".essence");
//        for (Element element : lists) { //获取一条评论的详细内容
//            Comment comment = new Comment();
//
//            Element author = element.getElementsByClass("author").first();
//
//            String head = author.getElementsByClass("userFace").first().getElementsByTag("img").first().attr("src");
//            comment.setHead(head);
//
//            Elements usernameElements = author.getElementsByTag("a");//有可能越界
//            String username = usernameElements.size() >= 3 ? usernameElements.get(2).text() : "";
//            comment.setUsername(username);
//
//            Elements pos = author.getElementsByClass("from-logon");
//            String position = pos.size() > 0 ? pos.first().text() : "";
//            comment.setPositions(position);
//            String postTime = element.getElementsByClass("postTime").first().text().substring(3);//多了字符：举报
//            try {
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                Date date = sdf.parse(postTime);
//                comment.setCommentDate(date);
//            } catch (ParseException e) {
//                System.out.println(e.getMessage());
//            }
//
//            Element body = element.getElementsByClass("body").first().getElementsByTag("div").first();
//            Elements commentBox = body.getElementsByClass("commentBox");//表示是回复评论的
//            String commentWithoutBox = "";//直接回复的没盖楼
//            List<String> boxList = new LinkedList<>();
//            if (commentBox.size() == 0) {//表示是直接回复的
////                    commentWithoutBox = body.getElementsByTag("div").first().text();
//                commentWithoutBox = body.text();
//                comment.setCommentWithoutBox(commentWithoutBox);
//                System.out.println(commentWithoutBox);
//            } else {
//                //获取盖楼评论
//                Elements contents = body.getElementsByClass("content");
//                for (Element content : contents) {
//                    boxList.add(content.text());
//                    System.out.println(content.text());
//                }
//                String jsonString = JSON.toJSONString(boxList);
//                JSONArray boxlist = JSONArray.parseArray(jsonString);
//                comment.setBoxList(boxlist.toJSONString());
//            }
//            if (newsid != 0) {
//                try {
//                    comment.setNews_id(newsid);
//                    Comment tmp = commentService.selectCommentByComment(comment);
//                    if (tmp == null) {
//                        commentService.insertComment(comment);
//                    }
//                } catch (Exception e) {
//                    log.error("Exception:" + e.getMessage());
//                }
//            }
//        }
//    }
}