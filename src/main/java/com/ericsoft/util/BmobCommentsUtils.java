package com.ericsoft.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.ericsoft.bmob.bson.BSONObject;
import com.ericsoft.bmob.model.BmobInsertResult;
import com.ericsoft.bmob.model.BmobSearchComment;
import com.ericsoft.bmob.model.BmobSearchNews;
import com.ericsoft.bmob.restapi.Bmob;
import com.spider.model.Comment;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class BmobCommentsUtils {

    public BmobCommentsUtils() {
        initBmob();//初始化
    }

    /**
     * 向news表中插入数据
     *
     * @param comment
     * @return String:
     * 插入失败 Not Found:(insert)https://api.bmob.cn/1/classes/GameScore
     * 插入成功 {"createdAt":"2017-04-29 19:45:35","objectId":"f2adb5ba0a"}
     */
    public BmobInsertResult insert(Comment comment) {
        BSONObject bson = new BSONObject();
        bson.put("news_id", comment.getNews_id().equals("") ? "-" : comment.getNews_id());
        bson.put("username", comment.getUsername().equals("") ? "-" : comment.getUsername());
        bson.put("head", comment.getHead().equals("") ? "-" : comment.getHead());
        bson.put("positions", comment.getPositions().equals("") ? "-" : comment.getPositions());
        bson.put("commentWithoutBox", comment.getCommentWithoutBox().equals("") ? "-" : comment.getCommentWithoutBox());
        bson.put("boxList", comment.getBoxList().equals("") ? "-" : comment.getBoxList());
//        if (!comment.getBoxList().equals("")) {
//            System.out.println("-------->" + comment.getBoxList());
//        }
        if (comment.getCommentDate().equals("")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            comment.setCommentDate(sdf.format(new Date()));
        }
        String result = Bmob.insert("comment", bson.toString());
        try {
            return JSON.parseObject(result, BmobInsertResult.class);
        } catch (JSONException e) {
            return new BmobInsertResult();
        }
    }

    //根据评论内容查询评论
    public BmobSearchComment Search(String commentWithoutBox, String boxList, int limit) {
        //查询语句和查询条件
        if (commentWithoutBox.equals("")) {
            commentWithoutBox = "+";
        }
        if (boxList.equals("")) {
            boxList = "+";
        }
        String sql = "select news_id,username,head,positions,commentDate,commentWithoutBox,boxList from comment where commentWithoutBox = ? OR boxList=? limit ?";
        String value = "\"" + commentWithoutBox + "\",\"" + boxList + "\"," + limit;
        String result = Bmob.findBQL(sql, value);
        try {
            return JSON.parseObject(result, BmobSearchComment.class);
        } catch (JSONException e) {
            return null;
        }
        //可使用JSON 或者 BSON 转换成Object
    }

    //使用RestAPI前必须先初始化，KEY可在Bmob应用信息里查询。
    private void initBmob() {
        Bmob.initBmob("fe2d0963f4f6019ccb3a23a76de05397", "b2584e80f16b216bef83fe4640463354");
    }

    private void update() {
        BSONObject bson = new BSONObject();
        bson.put("score", 100);
        //score 修改为100
        Bmob.update("Your TableName", "Your objectId", bson.toString());
    }

    private void delete() {
        Bmob.delete("Your TableName", "Your objectId");
    }

    private void count() {
        BSONObject where = new BSONObject();
        where.put("score", 100);
        Bmob.count("Your TableName", where.toString());
    }

    public static void main(String[] args) {
        BmobCommentsUtils bmobCommentsUtils = new BmobCommentsUtils();

        Comment comment = new Comment();
        comment.setUsername("123");
        comment.setBoxList("456");
        comment.setNews_id("00000000");
        List<String> boxList = new LinkedList<>();
        boxList.add("1楼,22");
        boxList.add("2楼");
        boxList.add("3楼");
//        String []s={"[1楼, 2楼, 3楼]"};
//        System.out.println();
        String jsonString = JSON.toJSONString(boxList);
        JSONArray boxlist = JSONArray.parseArray(jsonString);
        String s = boxlist.toJSONString();
        s = s.replace("\"", "'");
        String s1 = boxlist.toString();
        comment.setBoxList(boxlist.toString().replace("\"", "\\\""));
//        comment.setBoxList(s);
//        bmobCommentsUtils.insert(comment);
        bmobCommentsUtils.Search("4", "456", 1);

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