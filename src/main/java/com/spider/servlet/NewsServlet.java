package com.spider.servlet;

import java.io.IOException;
import java.util.Timer;
import javax.servlet.ServletException;
import javax.servlet.http.*;

/**
 * 每个一段时间爬取新闻保存到数据库中
 */

public class NewsServlet extends HttpServlet {

    private Timer timer;
    private Timer timer2;
    private Timer timer3;
    private Timer timer4;

    public NewsServlet() {
        timer = new Timer();
        timer2 = new Timer();
        timer3 = new Timer();
        timer4 = new Timer();
    }

    public void init() throws ServletException {
        timer.schedule(new SpiderTimer(), 1000, 1 * 60 * 60 * 1000); // 一小时执行一次
        timer2.schedule(new JokeTimer(), 500, 1 * 10 * 60 * 1000); //10分钟执行一次
        timer3.schedule(new HealthTimer(), 1000, 1 * 60 * 60 * 1000); //1小时执行一次
        timer4.schedule(new MusicTimer(), 1000, 1 * 5 * 60 * 1000); //10分钟执行一次
    }

    public void doGet(HttpServletRequest httpservletrequest,
                      HttpServletResponse httpservletresponse) throws ServletException,
            IOException {
    }

    public void destory() {
        timer.cancel();
    }
}