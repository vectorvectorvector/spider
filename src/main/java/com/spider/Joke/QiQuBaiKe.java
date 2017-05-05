package com.spider.Joke;

import com.ericsoft.bmob.model.BmobSearchJokes;
import com.ericsoft.util.BmobJokeUtils;
import com.spider.model.Joke;
import com.spider.util.DataUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/5/5/0005.
 * 内涵社区
 */
@Service
public class QiQuBaiKe {
    private String picUrl = "http://qiqu.uc.cn/?uc_param_str=frpfvedncpssntnwbipreime#!/index/index";
    private Joke joke;
    @Resource
    private BmobJokeUtils bmobJokeUtils;

    public QiQuBaiKe() {
        joke = new Joke();
    }

    //解析内涵社区的文档内容：文字版的和图片版的
    public void parseHtml(String result) {
        int jingxuanfirst = result.indexOf("\"jingxuan\"");
        int jingxuanend = result.indexOf("}}]}],");
        int jokefirst = result.indexOf("\"joke\"");
        int jokeend = result.indexOf("\"newDaily\"");
        if (jingxuanfirst < 0 || jingxuanend < 0 || jokefirst < 0 || jokeend < 0) {
            return;
        }
        String jingxuan_json = "{\"jingxuan\":[{" + result.substring(jingxuanfirst + 13, jingxuanend + 5) + "}";
        String joke_json = "{\"joke\":[" + result.substring(jokefirst + 8, jokeend - 2) + "]}";
        try {
            JSONTokener jsonParser = new JSONTokener(joke_json);
            JSONObject resultJson = (JSONObject) jsonParser.nextValue();
            JSONArray dataList = resultJson.getJSONArray("joke");
            for (int i = 0; i < dataList.length(); i++) {
                JSONObject object = (JSONObject) dataList.get(i);
                joke.setHead(object.getString("avatar"));
                joke.setUsername(object.getString("user_name"));
                joke.setContent(object.getString("content").replace("\"", "\\\""));
                insert("jokeText");
            }

            jsonParser = new JSONTokener(jingxuan_json);
            resultJson = (JSONObject) jsonParser.nextValue();
            dataList = resultJson.getJSONArray("jingxuan");
            for (int i = 0; i < dataList.length(); i++) {
                JSONObject object = (JSONObject) dataList.get(i);
                joke.setHead(object.getString("avatar"));
                joke.setUsername(object.getString("user_name"));
                joke.setContent(object.getString("title").replace("\"", "\\\""));
                JSONArray media_data = (JSONArray) object.get("media_data");
                if (media_data.length() > 0) {
                    JSONObject media = media_data.getJSONObject(0).getJSONObject("origin_img_url");
                    joke.setImage(media.getString("origin_pic_url"));
                }
                insert("joke");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getQiQuPic() {
        joke.init();
        joke.setOrigin("奇趣百科");
        String result = DataUtil.doGetCharset(picUrl, "utf-8");
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

    public void getQiQu() {
        getQiQuPic();
    }

    public static void main(String[] args) {
        QiQuBaiKe neiHan = new QiQuBaiKe();
        neiHan.getQiQuPic();
    }
}
