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
 */
@Service
public class QiuBai {
    private String Url = "http://www.qiushibaike.com/";
//    private String Url = "http://www.qiushibaike.com/8hr/page/2/?s=4980170";
    private Joke joke;
    @Resource
    private BmobJokeUtils bmobJokeUtils;

    public QiuBai() {
        joke = new Joke();
    }

    public void parseHtml(String result) {
        Document doc = Jsoup.parse(result);
        Elements lists = doc.select("div.article").select(".block").select(".untagged").select(".mb15");
        for (Element e : lists) {
            Elements author = e.select("div.author").select(".clearfix");
            if (author.size() > 0) {
                Elements head = author.first().getElementsByTag("img");
                if (head.size() > 0) {
                    joke.setHead(head.first().attr("src"));
                }
                Elements username = author.first().getElementsByTag("h2");
                if (username.size() > 0) {
                    joke.setUsername(username.first().text().replace("\"", "\\\""));
                }
            }
            Elements content = e.getElementsByClass("content");
            if (content.size() > 0) {
                joke.setContent(content.first().getElementsByTag("span").first().text().replace("\"", "\\\""));
            }

            Elements comment = e.getElementsByClass("main-text");
            if (comment.size() > 0) {
                joke.setShenHuiFu(comment.first().text().replace("\"", "\\\""));
            }
            Elements image = e.getElementsByClass("thumb");
            if (image.size() > 0) {
                joke.setImage(image.first().getElementsByTag("img").first().attr("src"));
            }

            if (!joke.getImage().equals("")) {
                insert("joke");
            } else if (!joke.getContent().equals("")) {
                insert("jokeText");
            }
            joke.init();
            joke.setOrigin("糗事百科");
        }
    }

    public void getQiubaiPic() {
        joke.init();
        joke.setOrigin("糗事百科");
        String result = DataUtil.doGetCharset(Url, "utf-8");
        if (result != null && !result.equals("")) {
            parseHtml(result);
        }
    }

    public void insert(String table) {
        BmobSearchJokes bmobSearchJokes = bmobJokeUtils.Search(joke.getContent(), joke.getImage(), 1, table);
        if (bmobSearchJokes != null && bmobSearchJokes.getResults().length == 0) {
            bmobJokeUtils.insert(joke, table);
        }
    }

    public void getQiubai() {
        getQiubaiPic();
    }

    public static void main(String[] args) {
        QiuBai neiHan = new QiuBai();
        neiHan.getQiubai();
    }
}
