package com.spider.servlet;

import com.spider.Joke.NeiHan;
import com.spider.Joke.PengFu;
import com.spider.Joke.QiQuBaiKe;
import com.spider.Joke.QiuBai;
import com.spider.Music.WangyiMusic;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.TimerTask;


public class MusicTimer extends TimerTask implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private WangyiMusic wangyiMusic;

//    private Logger log = Logger.getLogger(SpiderTimer.class);

    public MusicTimer() {
        applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        wangyiMusic = (WangyiMusic) applicationContext.getBean("wangyiMusic");
    }

    public void run() {
//        qiuBai.getQiubai();
        wangyiMusic.getMusicComments();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
//        log.info("初始化ApplicationContext...........");
//        System.out.println("初始化ApplicationContext...........");
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}