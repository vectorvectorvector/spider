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

import java.util.Random;
import java.util.TimerTask;


public class MusicTimer extends TimerTask implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private WangyiMusic wangyiMusic;
    private Random random;

//    private Logger log = Logger.getLogger(SpiderTimer.class);

    public MusicTimer() {
        applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        wangyiMusic = (WangyiMusic) applicationContext.getBean("wangyiMusic");
        random = new Random();
    }

    public void run() {
        try {
            int time = random.nextInt(10) * 1000;
            System.out.println("音乐延迟：" + time / 1000 + "秒");
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
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