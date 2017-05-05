package com.ericsoft.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.ericsoft.bmob.bson.BSONObject;
import com.ericsoft.bmob.model.BmobInsertResult;
import com.ericsoft.bmob.model.BmobSearchJokes;
import com.ericsoft.bmob.restapi.Bmob;
import com.spider.model.Joke;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BmobJokeUtils {

    public BmobJokeUtils() {
        initBmob();//初始化
    }

    /**
     * 向news表中插入数据
     *
     * @param joke
     * @return String:
     * 插入失败 Not Found:(insert)https://api.bmob.cn/1/classes/GameScore
     * 插入成功 {"createdAt":"2017-04-29 19:45:35","objectId":"f2adb5ba0a"}
     */
    public BmobInsertResult insert(Joke joke, String table) {
        BSONObject bson = new BSONObject();
        bson.put("username", !joke.getUsername().equals("") ? joke.getUsername() : "-");
        bson.put("head", !joke.getHead().equals("") ? joke.getHead() : "-");
        bson.put("content", !joke.getContent().equals("") ? joke.getContent() : new Date());
        bson.put("image", !joke.getImage().equals("") ? joke.getImage() : "-");
        bson.put("ShenHuiFu", !joke.getShenHuiFu().equals("") ? joke.getShenHuiFu() : "-");
        bson.put("comment", !joke.getComment().equals("") ? joke.getComment() : "-");
        bson.put("comefrom", !joke.getOrigin().equals("") ? joke.getOrigin() : "-");
//        String result = Bmob.insert("joke", bson.toString());
        String result = Bmob.insert(table, bson.toString());
        try {
            return JSON.parseObject(result, BmobInsertResult.class);
        } catch (JSONException e) {
            return new BmobInsertResult();
        }
    }

    //根据新闻标题查询新闻在数据库中的ID
    public BmobSearchJokes Search(String content, String image, int limit, String table) {
        //查询语句和查询条件
        if (content.equals("")) {
            content = "+";
        }
        if (image.equals("")) {
            image = "+";
        }
//        String sql = "select username,head,content,image,ShenHuiFu,comment,origin from joke where content = ? OR image=? limit ?";
        String sql = "select username,head,content,image,ShenHuiFu,comment,origin from " + table + " where content = ? OR image=? limit ?";
        String value = "\"" + content + "\",\"" + image + "\"," + limit;
        String result = Bmob.findBQL(sql, value);
        try {
            return JSON.parseObject(result, BmobSearchJokes.class);
        } catch (JSONException e) {
            return null;
        }
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

        BmobJokeUtils bmobJokeUtils = new BmobJokeUtils();
        Joke joke = new Joke();
        joke.setUsername("张三");
        joke.setImage("http://p3.pstatp.com/large/1e130002b1a58e7c72c1");
        joke.setComment("哈哈哈");
        joke.setOrigin("内涵社区");
        joke.setContent("内容");
        joke.setShenHuiFu("神回复");
        joke.setHead("http://p3.pstatp.com/large/1e130002b1a58e7c72c1");
        bmobJokeUtils.insert(joke,"jokeText");

        BmobSearchJokes bmobSearchJokes = bmobJokeUtils.Search("内容", "", 1,"jokeText");
        System.out.println();
//        initBmob();//初始化
//        Search("新闻标题-", 1);//查询
//		Search();//查询
//		update();//修改
//		delete();//删除
//		insert();//新增

//		callFunction();//调用云代码
//		findPayOrder();//查询支付订单
//		count();//计数
//		upload();//上传文件
//        requestSms();//发送短信
    }
}