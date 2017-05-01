package com.ericsoft.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.ericsoft.bmob.bson.BSONObject;
import com.ericsoft.bmob.model.BmobInsertResult;
import com.ericsoft.bmob.model.BmobSearchNews;
import com.ericsoft.bmob.restapi.Bmob;
import com.spider.model.News;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BmobNewsUtils {

    public BmobNewsUtils() {
        initBmob();//初始化
    }

    /**
     * 向news表中插入数据
     *
     * @param news
     * @return String:
     * 插入失败 Not Found:(insert)https://api.bmob.cn/1/classes/GameScore
     * 插入成功 {"createdAt":"2017-04-29 19:45:35","objectId":"f2adb5ba0a"}
     */
    public static BmobInsertResult insert(News news) {
        BSONObject bson = new BSONObject();
        bson.put("url", !news.getUrl().equals("") ? news.getUrl() : "-");
        bson.put("title", !news.getTitle().equals("") ? news.getTitle() : "-");
        bson.put("time", !news.getTime().equals("") ? news.getTime() : new Date());
        bson.put("imgurl", !news.getImgurl().equals("") ? news.getImgurl() : "-");
        bson.put("id", news.getId());
        bson.put("content", !news.getContent().equals("") ? news.getContent() : "-");
        bson.put("comment", !news.getComment().equals("") ? news.getComment() : "-");
        bson.put("channelname", !news.getChannelname().equals("") ? news.getChannelname() : "-");
        String result = Bmob.insert("news", bson.toString());
        try {
            return JSON.parseObject(result, BmobInsertResult.class);
        } catch (JSONException e) {
            return new BmobInsertResult();
        }
    }

    //根据新闻标题查询新闻在数据库中的ID
    public static BmobSearchNews Search(String title, int limit) {
        //查询语句和查询条件
        String sql = "select channelname,comment,content,id,imgurl,time,title,url from news where title = ? limit ?";
        String value = "\"" + title + "\"," + limit;
        //String result =Bmob.findBQL("select objectId from news where title =\""+title+"\"");
        String result = Bmob.findBQL(sql, value);
        return JSON.parseObject(result, BmobSearchNews.class);
        //可使用JSON 或者 BSON 转换成Object
//        System.out.println(bmobSearch.getResults().length);
    }

    //使用RestAPI前必须先初始化，KEY可在Bmob应用信息里查询。
    private static void initBmob() {
        Bmob.initBmob("fe2d0963f4f6019ccb3a23a76de05397", "b2584e80f16b216bef83fe4640463354");
    }

    private static void update() {
        BSONObject bson = new BSONObject();
        bson.put("score", 100);
        //score 修改为100
        Bmob.update("Your TableName", "Your objectId", bson.toString());
    }

    private static void delete() {
        Bmob.delete("Your TableName", "Your objectId");
    }

    private static void count() {
        BSONObject where = new BSONObject();
        where.put("score", 100);
        Bmob.count("Your TableName", where.toString());
    }

    public static void main(String[] args) {
        //BSONObject 简单使用
//		CreateClassBSONObject();

        BmobNewsUtils bmobNewsUtils = new BmobNewsUtils();
//        initBmob();//初始化
//        Search("新闻标题-", 1);//查询
//		Search();//查询
//		update();//修改
//		delete();//删除
//		insert();//新增
        News news = new News();
        news.setTitle("新闻标题");
        news.setChannelname("-");
        news.setComment("-");
        news.setContent("-");
        news.setDocurl("--");
        news.setId(1);
        news.setImgurl("-");
        news.setNews_id(2);
        news.setTime(new Date());
        news.setUrl("");

        bmobNewsUtils.insert(news);//新增
//		callFunction();//调用云代码
//		findPayOrder();//查询支付订单
//		count();//计数
//		upload();//上传文件
//        requestSms();//发送短信
    }
}