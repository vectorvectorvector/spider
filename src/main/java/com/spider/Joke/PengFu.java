package com.spider.Joke;

import com.ericsoft.bmob.model.BmobSearchJokes;
import com.ericsoft.util.BmobJokeUtils;
import com.spider.model.Joke;
import com.spider.util.DataUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/5/5/0005.
 * 捧腹网
 */
@Service
public class PengFu {
    private String Url = "https://www.pengfu.com/";
    private String ShenHuifuUrl = "https://www.pengfu.com/shen_1_1.html";
    private String QutuUrl = "https://www.pengfu.com/qutu_1.html";
    private String DuanziUrl = "https://www.pengfu.com/xiaohua_1.html";
    private Joke joke;
    @Resource
    private BmobJokeUtils bmobJokeUtils;

    public PengFu() {
        joke = new Joke();
    }

    //解析内涵社区的文档内容：文字版的和图片版的
    public void parseHtml(String result, String table) {
        Document doc = Jsoup.parse(result);
        Elements lists = doc.select("div.list-item").select(".bg1").select(".b1").select(".boxshadow");
        for (Element e : lists) {
            Elements head = e.getElementsByTag("dt");
            if (head.size() > 0) {
                joke.setHead(head.first().getElementsByTag("img").first().attr("src"));
            }
            Elements username = e.getElementsByClass("user_name_list");
            if (username.size() > 0) {
                joke.setUsername(username.first().getElementsByTag("a").first().text());
            }
            Elements title = e.getElementsByClass("dp-b");
            if (title.size() > 0) {
                joke.setContent(title.first().getElementsByTag("a").text().replace("\"", "\\\""));
            }
            Elements image = e.select("div.content-img").select(".clearfix").select(".pt10").select(".relative");
            if (image.size() > 0) {
                Elements img = image.first().getElementsByTag("img");
                if (img.size() > 0) {
                    String gifsrc = img.first().attr("gifsrc");
                    String src = img.first().attr("src");
                    String jpgsrc = img.first().attr("jpgsrc");
                    if (gifsrc != null && !gifsrc.equals("")) {
                        joke.setImage(gifsrc);
                    } else if (src != null && !src.equals("")) {
                        joke.setImage(src);
                    } else if (jpgsrc != null && !jpgsrc.equals("")) {
                        joke.setImage(jpgsrc);
                    }
                }
                if (table.equals("jokeText")) {
                    String Content = image.text().replace("\"", "\\\"");
                    joke.setContent(Content);
                }
            }
            Elements shen = e.getElementsByClass("shenhf-con");
            if (shen.size() > 0) {
                joke.setShenHuiFu(shen.first().text().replace("\"", "\\\""));
            }
            if (table.equals("joke")) {
                if (!joke.getImage().equals("")) {
                    insert(table);
                }
            } else {
                if (!joke.getContent().equals("")) {
                    insert(table);
                }
            }
            joke.init();
            joke.setOrigin("捧腹网");
        }
    }

    public void getPengfuPic() {
        joke.init();
        joke.setOrigin("捧腹网");
        String result = DataUtil.doGetCharset(Url, "utf-8");
        if (result != null && !result.equals("")) {
            parseHtml(result,"joke");
        }
        result = DataUtil.doGetCharset(ShenHuifuUrl, "utf-8");
        if (result != null && !result.equals("")) {
            parseHtml(result,"joke");
        }
        result = DataUtil.doGetCharset(QutuUrl, "utf-8");
        if (result != null && !result.equals("")) {
            parseHtml(result,"joke");
        }
        result = DataUtil.doGetCharset(DuanziUrl, "utf-8");
        if (result != null && !result.equals("")) {
            parseHtml(result, "jokeText");
        }
    }

    public void insert(String table) {
        BmobSearchJokes bmobSearchJokes = bmobJokeUtils.Search(joke.getContent(), joke.getImage(), 1, table);
        if (bmobSearchJokes != null && bmobSearchJokes.getResults().length == 0) {
            bmobJokeUtils.insert(joke, table);
        }
    }

    public void getPengfu() {
        getPengfuPic();
    }

    public static void main(String[] args) {
        PengFu neiHan = new PengFu();
        neiHan.getPengfuPic();
    }
}
