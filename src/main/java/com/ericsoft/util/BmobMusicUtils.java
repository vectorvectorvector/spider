package com.ericsoft.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.ericsoft.bmob.bson.BSONObject;
import com.ericsoft.bmob.model.*;
import com.ericsoft.bmob.restapi.Bmob;
import com.spider.model.Joke;
import com.spider.model.Music;
import com.spider.model.MusicComment;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BmobMusicUtils {

    public BmobMusicUtils() {
        initBmob();//初始化
    }

    /**
     * 向音乐表中插入数据
     */
    public BmobInsertResult insert(Music music) {
        BSONObject bson = new BSONObject();
        bson.put("title", !music.getTitle().equals("") ? music.getTitle() : "-");
        bson.put("url", !music.getUrl().equals("") ? music.getUrl() : "-");
        bson.put("author", !music.getAuthor().equals("") ? music.getAuthor() : "-");
        bson.put("comment", !music.getComment().equals("") ? music.getComment() : "-");
        String result = Bmob.insert("music", bson.toString());
        try {
            return JSON.parseObject(result, BmobInsertResult.class);
        } catch (JSONException e) {
            return new BmobInsertResult();
        }
    }

    //查询音乐
    public BmobSearchMusic Search(String url, int limit) {
        //查询语句和查询条件
        if (url.equals("")) {
            url = "+";
        }
        String sql = "select title,url,author,comment from " + "music" + " where url = ? limit ?";
        String value = "\"" + url + "\"," + limit;
        String result = Bmob.findBQL(sql, value);
        try {
            return JSON.parseObject(result, BmobSearchMusic.class);
        } catch (JSONException e) {
            return null;
        }
    }
    /**
     * 向音乐评论表中插入数据
     */
    public BmobInsertResult insertComment(SearchModelMusicComment comment) {
        BSONObject bson = new BSONObject();
        bson.put("musicId", !comment.getMusicId().equals("") ? comment.getMusicId() : "-");
        bson.put("avatarUrl", !comment.getAvatarUrl().equals("") ? comment.getAvatarUrl() : "-");
        bson.put("nickname", !comment.getNickname().equals("") ? comment.getNickname() : "-");
        bson.put("likedCount", comment.getLikedCount());
        bson.put("content", !comment.getContent().equals("") ? comment.getContent() : "-");
        bson.put("beRepliedContent", !comment.getBeRepliedContent().equals("") ? comment.getBeRepliedContent() : "-");
        bson.put("beRepliedAvatarUrl", !comment.getBeRepliedAvatarUrl().equals("") ? comment.getBeRepliedAvatarUrl() : "-");
        bson.put("beRepliedNickname", !comment.getBeRepliedNickname().equals("") ? comment.getBeRepliedNickname() : "-");
        String result = Bmob.insert("musiccomment", bson.toString());
        try {
            return JSON.parseObject(result, BmobInsertResult.class);
        } catch (Exception e) {
            return new BmobInsertResult();
        }
    }
    //查询音乐
    public BmobSearchMusicComment SearchComment(String content, int limit) {
        //查询语句和查询条件
        if (content.equals("")) {
            content = "+";
        }
        String sql = "select musicId,avatarUrl,nickname,likedCount,content,beRepliedContent,beRepliedAvatarUrl,beRepliedNickname from " + "musiccomment" + " where content = ? limit ?";
        String value = "\"" + content + "\"," + limit;
        String result = Bmob.findBQL(sql, value);
        try {
            return JSON.parseObject(result, BmobSearchMusicComment.class);
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

        BmobMusicUtils bmobMusicUtils = new BmobMusicUtils();
        Music music = new Music();
        music.setTitle("标题");
        music.setUrl("163.com");
        music.setAuthor("薛之谦");
        music.setComment("网易");
//        bmobJokeUtils.insert(music);

//        BmobSearchMusic searchModelMusic = bmobMusicUtils.Search(music.getUrl(), 1);

        SearchModelMusicComment comment = new SearchModelMusicComment();
        comment.setAvatarUrl("www");
        comment.setBeRepliedAvatarUrl("sss");
        comment.setBeRepliedContent("回复我");
        comment.setBeRepliedNickname("nickname");
        comment.setContent("回复你咋滴");
        comment.setLikedCount(10000);
        comment.setMusicId("1234567");
        comment.setNickname("张三");
//        bmobMusicUtils.insertComment(comment);
        BmobSearchMusicComment searchMusicComment = bmobMusicUtils.SearchComment(comment.getContent(),1);
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