package com.spider.Joke;

import com.spider.model.Joke;
import com.spider.util.DataUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/5/5/0005.
 * 内涵社区
 */
@Service
public class NeiHan {
    private String picUrl = "http://neihanshequ.com/pic/";
    private String picText = "http://neihanshequ.com/";
    private Joke joke;

    public NeiHan() {
        joke = new Joke();
    }

    public void getNeiHanText() {
        String result = DataUtil.doGetCharset(picUrl, "utf-8");
        Document doc = Jsoup.parse(result);
    }

    public void getNeiHanPic() {
        String result = DataUtil.doGetCharset(picText, "utf-8");
        Document doc = Jsoup.parse(result);
        Elements pics = doc.getElementsByClass("detail-wrapper");
        for (Element e : pics) {
            Elements header = e.getElementsByClass("header");
            if (header.size() > 0) {
                Elements head = header.first().getElementsByTag("img");//头像
                if (head.size() > 0) {
                    joke.setHead(head.first().attr("data-src"));
                }
                Elements name = header.first().getElementsByClass("name");//用户名
                if (name.size() > 0) {
                    joke.setUsername(name.first().text());
                }
                head = null;
                name = null;
            }
            //内容和图片
            Elements title = e.getElementsByClass("title");
            if (title.size() > 0) {
                joke.setContent(title.first().getElementsByTag("p").first().text());
            }
            title=null;
            Elements images = e.getElementsByTag("img");
            if (images.size()>0){
                StringBuilder imageUrl=new StringBuilder();
                for(Element image:images){

                }
            }
        }
    }

    public static void main(String[] args) {
        NeiHan neiHan = new NeiHan();
        neiHan.getNeiHanPic();
    }
}
