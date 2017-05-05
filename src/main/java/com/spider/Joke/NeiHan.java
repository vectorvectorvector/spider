package com.spider.Joke;

import com.ericsoft.bmob.model.BmobSearchJokes;
import com.ericsoft.util.BmobJokeUtils;
import com.spider.model.Joke;
import com.spider.util.DataUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/5/5/0005.
 * 内涵社区
 */
@Service
public class NeiHan {
    private String picUrl = "http://neihanshequ.com/pic/";
    private String picText = "http://neihanshequ.com/";
    private Joke joke;
    @Resource
    private BmobJokeUtils bmobJokeUtils;

    public NeiHan() {
        joke = new Joke();
    }

    //解析内涵社区的文档内容：文字版的和图片版的
    public void parseNeiHan(String result, String table) {
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
                joke.setContent(title.first().getElementsByTag("p").first().text().replace("\"", "\\\""));
            }
            title = null;
            Elements images = e.getElementsByClass("content-wrapper");
            if (images.size() > 0) {
                String url = images.first().getElementsByTag("a").attr("href");
                getNeiHanPicDetail(url, table);
            }
        }
    }

    public void getNeiHanText() {
        joke.init();
        joke.setOrigin("内涵社区");
        String result = DataUtil.doGetCharset(picText, "utf-8");
        if (result != null && !result.equals("")) {
            parseNeiHan(result, "jokeText");
        }
    }

    public void getNeiHanPic() {
        joke.init();
        joke.setOrigin("内涵社区");
        String result = DataUtil.doGetCharset(picUrl, "utf-8");
        if (result != null && !result.equals("")) {
            parseNeiHan(result, "joke");
        }
    }

    public void insert(String table) {
        BmobSearchJokes bmobSearchJokes = bmobJokeUtils.Search(joke.getContent(), joke.getImage(), 1, table);
        if (bmobSearchJokes != null && bmobSearchJokes.getResults().length == 0) {
            bmobJokeUtils.insert(joke, table);
        }
    }


    public void getNeiHan() {
        getNeiHanPic();
        getNeiHanText();
    }

    //无法直接在当前页获取图片
    public void getNeiHanPicDetail(String Url, String table) {
        String result = DataUtil.doGetCharset(Url, "utf-8");
        if (result != null && !result.equals("")) {
            Document doc = Jsoup.parse(result);
            Elements content = doc.getElementsByClass("img-wrapper");
            if (content.size() > 0) {
                Elements images = content.first().getElementsByTag("img");
                if (images.size() > 0) {
                    StringBuilder imageUrl = new StringBuilder();
                    for (Element e : images) {
                        imageUrl.append(e.attr("src") + ";");
                    }
                    joke.setImage(imageUrl.toString());
                }
                content = null;
            }
            Elements comments = doc.getElementsByClass("comment-list");
            if (comments.size() > 0) {
                Elements commentItems = comments.first().getElementsByClass("comment-item");
                StringBuilder comm = new StringBuilder();
                for (Element e : commentItems) {
                    comm.append(e.getElementsByClass("indent").first().text() + "{}");
                }
                joke.setComment(comm.toString());
            }
            insert(table);
        }
    }

    public static void main(String[] args) {
        NeiHan neiHan = new NeiHan();
        neiHan.getNeiHanPic();
    }
}
