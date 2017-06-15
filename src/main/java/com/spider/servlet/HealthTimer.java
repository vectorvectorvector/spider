package com.spider.servlet;

import com.spider.Health.WangyiHealth;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Random;
import java.util.TimerTask;


public class HealthTimer extends TimerTask implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private WangyiHealth wangyiHealth;
    private Random random;

//    private Logger log = Logger.getLogger(SpiderTimer.class);

    public HealthTimer() {
        applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        wangyiHealth = (WangyiHealth) applicationContext.getBean("wangyiHealth");
        random = new Random();
    }

    public void run() {
        try {
            int time = random.nextInt(30) * 1000;
            System.out.println("健康延迟：" + time / 1000 + "秒");
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
        wangyiHealth.getHealth();
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