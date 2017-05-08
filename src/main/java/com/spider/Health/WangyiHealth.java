package com.spider.Health;

import com.ericsoft.bmob.model.BmobSearchHealth;
import com.ericsoft.util.BmobHealthUtils;
import com.spider.model.Health;
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
public class WangyiHealth {
    private String Yinshi = "http://jiankang.163.com/special/shipin_n/?2";//饮食
    private String Yangsheng = "http://jiankang.163.com/special/yangsheng_n/?1";//养生
    private Health health;
    @Resource
    private BmobHealthUtils bmobHealthUtils;

    public WangyiHealth() {
        health = new Health();
    }

    //解析内涵社区的文档内容：文字版的和图片版的
    public void parseHtml(String result, String table) {
        Document doc = Jsoup.parse(result);
        Elements news_main_info = doc.getElementsByClass("news_main_info");
        if (news_main_info.size() > 0) {
            for (Element e : news_main_info) {
                Elements links = e.getElementsByTag("a");
                if (links.size() > 0) {
                    health.setUrl(links.first().attr("href"));
                    health.setTitle(links.first().text().replace("\"", "\\\""));
                }
                Elements img = e.getElementsByTag("img");
                if (img.size() > 0) {
                    health.setImgurl(img.first().attr("href"));
                }
                Elements subTitle = e.getElementsByTag("p");
                if (subTitle.size() > 0) {
                    health.setSubTitle(subTitle.first().text().replace("\"", ""));
                }
                getDetails();
            }
        }
    }

    //获取详细信息
    public void getDetails() {
        if (health.getUrl() != null && !health.getUrl().equals("")) {
            String result = DataUtil.doGetCharset(health.getUrl(), "gb2312");
            Document doc = Jsoup.parse(result);
            Elements contents = doc.getElementsByClass("post_text");
            if (contents.size() > 0) {
                health.setContent(contents.toString().replace("\n", "").replace("\"", "\\\""));//设置内容，包含Html标签

                //获取图片
                if (health.getImgurl().equals("")) {
                    Elements pics = contents.first().getElementsByTag("img");
                    StringBuilder images = new StringBuilder();
                    if (pics.size() > 0 && pics.size() < 3) {
                        images.append(pics.first().getElementsByTag("img").first().attr("src"));
                    } else if (pics.size() >= 3) {
                        for (int i = 0; i < 3; i++) {
                            Element e = pics.get(i);
                            images.append(e.getElementsByTag("img").first().attr("src") + ";");
                        }
                    }
                    health.setImgurl(images.toString());
                }
                insert("health");
                health.init();
                health.setOrigin("网易健康");
            }
        }
    }

    public void getYangsheng() {
        health.setOrigin("网易健康");
        String result = DataUtil.doGetCharset(Yangsheng, "gbk");
        if (result != null && !result.equals("")) {
            parseHtml(result, "health");
        }
    }

    public void getYinshi() {
        health.setOrigin("网易健康");
        String result = DataUtil.doGetCharset(Yinshi, "gbk");
        if (result != null && !result.equals("")) {
            parseHtml(result, "health");
        }
    }

    public void insert(String table) {
        BmobSearchHealth bmobSearchHealth = bmobHealthUtils.Search(health.getUrl(), 1, table);
        if (bmobSearchHealth != null && bmobSearchHealth.getResults().length == 0) {
            bmobHealthUtils.insert(health, table);
        }
    }


    public void getHealth() {
        getYinshi();
        getYangsheng();
    }

    public static void main(String[] args) {
        WangyiHealth neiHan = new WangyiHealth();
        neiHan.getHealth();
    }
}
