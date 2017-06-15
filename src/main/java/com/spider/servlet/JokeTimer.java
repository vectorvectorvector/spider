package com.spider.servlet;

import com.spider.Joke.NeiHan;
import com.spider.Joke.PengFu;
import com.spider.Joke.QiQuBaiKe;
import com.spider.Joke.QiuBai;
import com.spider.wangyi.WangYiNews;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Random;
import java.util.TimerTask;


public class JokeTimer extends TimerTask implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private NeiHan neiHan;
    private QiQuBaiKe qiQuBaiKe;
    private PengFu pengFu;
    private QiuBai qiuBai;
    private Random random;

//    private Logger log = Logger.getLogger(SpiderTimer.class);

    public JokeTimer() {
        applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        neiHan = (NeiHan) applicationContext.getBean("neiHan");
        qiQuBaiKe = (QiQuBaiKe) applicationContext.getBean("qiQuBaiKe");
        pengFu = (PengFu) applicationContext.getBean("pengFu");
        qiuBai = (QiuBai) applicationContext.getBean("qiuBai");
        random = new Random();
    }

    public void run() {
        try {
            int time = random.nextInt(10) * 1000;
            System.out.println("笑话延迟：" + time / 1000 + "秒");
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
        neiHan.getNeiHan();
        qiQuBaiKe.getQiQu();
        pengFu.getPengfu();
        qiuBai.getQiubai();
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