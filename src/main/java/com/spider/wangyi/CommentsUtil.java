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

/**
 * Created by zhouchao on 2017/3/3.
 */
public class CommentsUtil {
    private Logger log = Logger.getLogger(CommentsUtil.class);

    public void getComments(String url) {
        String TargetURL = url;
        TargetURL = "http://comment.news.163.com/news2_bbs/CEJ5BP5100018AOP.html";
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
            Element hotReplies = doc.getElementById("hotReplies");
            Element hotRepliesList = hotReplies.getElementsByClass("list").first();
            Elements lists = hotRepliesList.select("div.reply").select(".essence");
            for (Element element : lists) { //获取一条评论的详细内容
                Element author = element.getElementsByClass("author").first();
                String head = author.getElementsByClass("userFace").first().getElementsByTag("img").first().attr("src");

                Elements usernameElements = author.getElementsByTag("a");//有可能越界
                String username = usernameElements.size()>=3?usernameElements.get(2).text():"";

                String position = author.getElementsByClass("from-logon").first().text();
                String postTime = element.getElementsByClass("postTime").first().text();//多了字符：举报
                Element body = element.getElementsByClass("body").first().getElementsByTag("div").first();
                String commentWithoutBox = body.text();
                System.out.println(commentWithoutBox);//还有问题
            }

            System.out.println(page.asXml());
        } catch (IOException e) {
            log.error("CommentsUtil IOException:" + e.getMessage());
        } finally {
            webClient.close();
        }
    }
}