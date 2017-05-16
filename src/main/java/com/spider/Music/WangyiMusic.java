package com.spider.Music;

import com.alibaba.fastjson.JSON;
import com.ericsoft.bmob.model.*;
import com.ericsoft.util.BmobJokeUtils;
import com.ericsoft.util.BmobMusicUtils;
import com.spider.model.*;
import com.spider.util.DataUtil;
import com.spider.util.EncryptTools;
import org.apache.commons.codec.binary.Hex;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;

/**
 * Created by Administrator on 2017/5/5/0005.
 * 内涵社区
 */
@Service
public class WangyiMusic {
    private String musicURL = "http://music.163.com";
    private String BiaoSheng = "http://music.163.com/discover/toplist?id=19723756";//云音乐飙升榜
    private Music music;
    private SearchModelMusicComment sqlComment;
    private String musicID;//音乐在数据库中的id

    public WangyiMusic() {
        music = new Music();
        music.init();
        sqlComment = new SearchModelMusicComment();
        sqlComment.init();
        musicID = "";
    }

    @Resource
    private BmobMusicUtils musicUtils;

    public void getMusicURL(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .header("Referer", "http://music.163.com/")
                    .header("Host", "music.163.com").get();
            Elements links = doc.select("ul[class=f-hide] a");
            if (links.size() > 0) {
                for (Element e : links) {
                    music.setUrl(musicURL + e.attr("href"));
                    music.setTitle(e.text());
                    String id = music.getUrl().substring(music.getUrl().lastIndexOf("id=") + 3);

                    BmobSearchMusic dbMusic = musicUtils.Search(music.getUrl(), 1);
                    if (dbMusic != null && dbMusic.getResults().length > 0) {
                        musicID = dbMusic.getResults()[0].getObjectId();
                        getMusicComments(id);
                    } else {
                        BmobInsertResult insertResult = musicUtils.insert(music);
                        if (!insertResult.getObjectId().equals("")) {
                            getMusicComments(id);
                        }
                    }
                    //重置数据
                    music.init();
                    musicID = "";
                }
            }
        } catch (IOException e) {

        } catch (Exception e) {

        }
    }

    //获取网易新闻评论
    //http://music.163.com/weapi/v1/resource/comments/R_SO_4_478029412/
    public void getMusicComments(String id) {
        try {
            String url = "http://music.163.com/weapi/v1/resource/comments/R_SO_4_" + id + "/";
            //私钥，随机16位字符串（自己可改）
            String secKey = "cd859f54539b24b7";
//        String text = "{\"username\": \"\", \"rememberLogin\": \"true\", \"password\": \"\",\"offset\": \"2\"}";
            String text = "{\"username\": \"\", \"rememberLogin\": \"true\", \"password\": \"\",\"offset\": \"0\",\"limit\": \"20\"}";
            String modulus = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7";
            String nonce = "0CoJUm6Qyw8W8jud";
            String pubKey = "010001";
            //2次AES加密，得到params
            String params = EncryptTools.encrypt(EncryptTools.encrypt(text, nonce), secKey);
            StringBuffer stringBuffer = new StringBuffer(secKey);
            //逆置私钥
            secKey = stringBuffer.reverse().toString();
            String hex = Hex.encodeHexString(secKey.getBytes());
            BigInteger bigInteger1 = new BigInteger(hex, 16);
            BigInteger bigInteger2 = new BigInteger(pubKey, 16);
            BigInteger bigInteger3 = new BigInteger(modulus, 16);
            //RSA加密计算
            BigInteger bigInteger4 = bigInteger1.pow(bigInteger2.intValue()).remainder(bigInteger3);
            String encSecKey = Hex.encodeHexString(bigInteger4.toByteArray());
            //字符填充
            encSecKey = EncryptTools.zfill(encSecKey, 256);
            //评论获取
            Document document = Jsoup.connect(url).cookie("appver", "1.5.0.75771")
                    .header("Referer", "http://music.163.com/").data("params", params).data("encSecKey", encSecKey)
                    .ignoreContentType(true).post();
//            System.out.println("评论：" + document.text());
            try {
                MusicComments musicComment = JSON.parseObject(document.text(), MusicComments.class);
                MusicComment[] musicComments = musicComment.getComments();
                for (MusicComment comment : musicComments) {
                    if (!musicID.equals("")) {
                        sqlComment.setAvatarUrl(comment.getUser().getAvatarUrl());
                        sqlComment.setNickname(comment.getUser().getNickname());
                        sqlComment.setLikedCount(comment.getLikedCount());
                        sqlComment.setContent(comment.getContent());
                        sqlComment.setMusicId(musicID);
                        MusicCommentReply[] repliy = comment.getBeReplied();
                        if (comment.getBeReplied() != null && comment.getBeReplied().length > 0) {
                            sqlComment.setBeRepliedContent(comment.getBeReplied()[0].getContent());
                            sqlComment.setBeRepliedAvatarUrl(comment.getBeReplied()[0].getUser().getAvatarUrl());
                            sqlComment.setBeRepliedNickname(comment.getBeReplied()[0].getUser().getNickname());
                        }

                        //数据库操作
                        BmobSearchMusicComment dbComment = musicUtils.SearchComment(sqlComment.getContent(), 1);
                        if (dbComment != null && dbComment.getResults().length > 0) {
                            //重置
                            sqlComment.init();
                        } else {
                            //插入数据库
                            musicUtils.insertComment(sqlComment);
                            sqlComment.init();
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
//            System.out.println();
        } catch (IOException e) {

        } catch (Exception e) {

        }
    }

    public void getMusicComments() {
        getMusicURL(BiaoSheng);
    }

    public static void main(String[] args) {
        WangyiMusic wangyiMusic = new WangyiMusic();
        wangyiMusic.getMusicComments();
    }
}
