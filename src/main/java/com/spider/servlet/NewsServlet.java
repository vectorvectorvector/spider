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

    public NewsServlet() {
        timer = new Timer();
    }

    public void init() throws ServletException {
        timer.schedule(new SpiderTimer(), 1000, 6*60*60*1000); // 2秒执行一次
    }

    public void doGet(HttpServletRequest httpservletrequest,
                      HttpServletResponse httpservletresponse) throws ServletException,
            IOException {
    }

    public void destory() {
        timer.cancel();
    }
}