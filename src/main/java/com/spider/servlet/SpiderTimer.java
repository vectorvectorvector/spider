package com.spider.servlet;

import com.spider.wangyi.WangYiNews;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Random;
import java.util.TimerTask;


public class SpiderTimer extends TimerTask implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private WangYiNews wangYiNews;
    private Random random;

//    private Logger log = Logger.getLogger(SpiderTimer.class);

    public SpiderTimer() {
        applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        wangYiNews = (WangYiNews) applicationContext.getBean("wangYiNews");
        random = new Random();
    }

    public void run() {
//        log.info("网易新闻---------");
//        System.out.println("网易新闻---------");
//        wangYiNews.getNews();
        try {
            int time = random.nextInt(30) * 1000;
            System.out.println("新闻延迟：" + time / 1000 + "秒");
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
        wangYiNews.getWangyiComments();
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