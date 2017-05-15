/**
 * Created by 周超 on 2017/02/28.
 */

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.gargoylesoftware.htmlunit.*;
import com.spider.model.MusicComment;
import com.spider.model.MusicCommentReply;
import com.spider.model.MusicComments;
import com.spider.util.DataUtil;
import com.spider.util.EncryptTools;
import com.spider.wangyi.CommentsUtil;
import org.apache.commons.codec.binary.Hex;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
//参考博文
//1.http://blog.csdn.net/zstu_cc/article/details/39250903
//2.http://blog.csdn.net/cslie/article/details/48735261

public class HtmlUnitAndJsoup {

	/*
     * 首先说说HtmlUnit相对于HttpClient的最明显的一个好处,
	 * 是HtmlUnit不仅保存了这个网页对象，更难能可贵的是它还存有这个网页的所有基本操作甚至事件。
	 * 现在很多网站使用大量ajax，普通爬虫无法获取js生成的内容。
	 */

    /*
     * 依赖的jar包 commons-lang3-3.1.jar htmlunit-2.13.jar htmlunit-core-js-2.13.jar
     * httpclient-4.3.1.jar httpcore-4.3.jar httpmime-4.3.1.jar sac-1.3.jar
     * xml-apis-1.4.01.jar commons-collections-3.2.1.jar commons-io-2.4.jar
     * xercesImpl-2.11.0.jar xalan-2.7.1.jar cssparser-0.9.11.jar
     * nekohtml-1.9.19.jar
     */
    // 百度新闻高级搜索
    @Test
    public void HtmlUnitBaiduAdvanceSearch() {
        try {
            // 得到浏览器对象，直接New一个就能得到，现在就好比说你得到了一个浏览器了
            WebClient webclient = new WebClient();

            // 这里是配置一下不加载css和javaScript,配置起来很简单，是不是
            webclient.getOptions().setCssEnabled(false);
            webclient.getOptions().setJavaScriptEnabled(false);

            // 做的第一件事，去拿到这个网页，只需要调用getPage这个方法即可
            HtmlPage htmlpage = webclient
                    .getPage("http://news.baidu.com/advanced_news.html");

            // 根据名字得到一个表单，查看上面这个网页的源代码可以发现表单的名字叫“f”
            final HtmlForm form = htmlpage.getFormByName("f");
            System.out.println(form);
            // 同样道理，获取”百度一下“这个按钮
            final HtmlSubmitInput button = form.getInputByValue("百度一下");
            System.out.println(button);
            // 得到搜索框
            final HtmlTextInput textField = form.getInputByName("q1");

            System.out.println(textField);

            // 最近周星驰比较火呀，我这里设置一下在搜索框内填入”周星驰“
            textField.setValueAttribute("周星驰");
            // 输入好了，我们点一下这个按钮
            final HtmlPage nextPage = button.click();
            // 我把结果转成String
            System.out.println(nextPage);

            String result = nextPage.asXml();

            System.out.println(result);
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    @Test
    public void HtmlUnitTencentNews() {
        try {

            /**HtmlUnit请求web页面*/
            WebClient wc = new WebClient();
            wc.getOptions().setJavaScriptEnabled(true); //启用JS解释器，默认为true
            wc.getOptions().setCssEnabled(false); //禁用css支持
            wc.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常
            wc.getOptions().setTimeout(10000); //设置连接超时时间 ，这里是10S。如果为0，则无限期等待
            HtmlPage page = wc.getPage("http://cq.qq.com/baoliao/detail.htm?294064");
            String pageXml = page.asXml(); //以xml的形式获取响应文本

            /**jsoup解析文档*/
            Document doc = Jsoup.parse(pageXml, "http://cq.qq.com");
//            Element pv = doc.select("#feed_content span").get(1);
            Element pv = doc.select("#detail-content").get(1);
            System.out.println(pv.text());
            Assert.assertTrue(pv.text().contains("浏览"));

            System.out.println("Thank God!");


//            WebClient webclient = new WebClient(BrowserVersion.CHROME);
//
//            // 得到浏览器对象，直接New一个就能得到，现在就好比说你得到了一个浏览器了
////            WebClient webclient = new WebClient();
//
//            // 这里是配置一下不加载css和javaScript,配置起来很简单，是不是
//            webclient.getOptions().setCssEnabled(false);
//            webclient.getOptions().setJavaScriptEnabled(true);
//
//            // 做的第一件事，去拿到这个网页，只需要调用getPage这个方法即可
//            HtmlPage htmlpage = webclient.getPage("http://coral.qq.com/1787868177");
////            HtmlPage htmlpage = webclient.getPage("http://news.baidu.com/advanced_news.html");
//            System.out.println(htmlpage.asText());
//
//            htmlpage.executeJavaScript("");


//            // 根据名字得到一个表单，查看上面这个网页的源代码可以发现表单的名字叫“f”
//            final HtmlForm form = htmlpage.getFormByName("f");
//            System.out.println(form);
//            // 同样道理，获取”百度一下“这个按钮
//            final HtmlSubmitInput button = form.getInputByValue("百度一下");
//            System.out.println(button);
//            // 得到搜索框
//            final HtmlTextInput textField = form.getInputByName("q1");
//
//            System.out.println(textField);
//
//            // 最近周星驰比较火呀，我这里设置一下在搜索框内填入”周星驰“
//            textField.setValueAttribute("周星驰");
//            // 输入好了，我们点一下这个按钮
//            final HtmlPage nextPage = button.click();
//            // 我把结果转成String
//            System.out.println(nextPage);
//
//            String result = nextPage.asXml();
//
//            System.out.println(result);
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    // 测试天涯论坛登陆界面 HtmlUnit 页面JS的自动跳转（响应码是200，但是响应的页面就是一个JS）
    // httpClient就麻烦了
    @Test
    public void TianyaTestByHtmlUnit() {

        try {
            WebClient webClient = new WebClient();

            // The ScriptException is raised because you have a syntactical
            // error in your javascript.
            // Most browsers manage to interpret the JS even with some kind of
            // errors
            // but HtmlUnit is a bit inflexible in that sense.
            // 加载的页面有js语法错误会抛出异常

            webClient.getOptions().setJavaScriptEnabled(true); // 启用JS解释器，默认为true
            webClient.getOptions().setCssEnabled(false); // 禁用css支持
            // 设置Ajax异步处理控制器即启用Ajax支持
            webClient
                    .setAjaxController(new NicelyResynchronizingAjaxController());
            // 当出现Http error时，程序不抛异常继续执行
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            // 防止js语法错误抛出异常
            webClient.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常

            // 拿到这个网页
            HtmlPage page = webClient
                    .getPage("http://passport.tianya.cn/login.jsp");

            // 填入用户名和密码
            HtmlInput username = (HtmlInput) page.getElementById("userName");
            username.type("u_110486326");
            HtmlInput password = (HtmlInput) page.getElementById("password");
            password.type("X0up4d65");

            // 提交
            HtmlButton submit = (HtmlButton) page.getElementById("loginBtn");
            HtmlPage nextPage = submit.click();
            System.out.println(nextPage.asXml());
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // jsoup解析文档
    @Test
    public void jsoupParse() {

        try {
            /** HtmlUnit请求web页面 */
            // 模拟chorme浏览器，其他浏览器请修改BrowserVersion.后面
            WebClient wc = new WebClient(BrowserVersion.CHROME);

            wc.getOptions().setJavaScriptEnabled(true); // 启用JS解释器，默认为true
            wc.getOptions().setCssEnabled(false); // 禁用css支持
            wc.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常
            wc.getOptions().setTimeout(10000); // 设置连接超时时间 ，这里是10S。如果为0，则无限期等待
            HtmlPage page = wc.getPage("http://passport.tianya.cn/login.jsp");
            String pageXml = page.asXml(); // 以xml的形式获取响应文本
            // text只会获取里面的文本,网页html标签和script脚本会被去掉
            String pageText = page.asText();
            System.out.println(pageText);

            // 方法一，通过get方法获取
            HtmlButton submit = (HtmlButton) page.getElementById("loginBtn");

            // 方法二，通过XPath获取，XPath通常用于无法通过Id搜索，或者需要更为复杂的搜索时
            HtmlDivision div = (HtmlDivision) page.getByXPath("//div").get(0);

            // 网络爬虫中主要目的就是获取页面中所有的链接

            java.util.List<HtmlAnchor> achList = page.getAnchors();
            for (HtmlAnchor ach : achList) {
                System.out.println(ach.getHrefAttribute());
            }

            System.out.println("-------jsoup部分------");
            // 服务器端进行校验并清除有害的HTML代码,防止富文本提交有害代码
            Jsoup.clean(pageXml, Whitelist.basic());
            /** jsoup解析文档 */
            // 把String转化成document格式
            Document doc = Jsoup.parse(pageXml);
            Element loginBtn = doc.select("#loginBtn").get(0);
            System.out.println(loginBtn.text());
            Assert.assertTrue(loginBtn.text().contains("登录"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // htmlunit设置代理上网
    @Test
    public void proxy() {
        String proxyHost = "192.168.0.1";
        int port = 80;
        WebClient webClient = new WebClient(BrowserVersion.CHROME, proxyHost,
                port);

        webClient.setAjaxController(new NicelyResynchronizingAjaxController());

        DefaultCredentialsProvider credentialsProvider = (DefaultCredentialsProvider) webClient
                .getCredentialsProvider();
        String username = "account";
        String password = "password";
        credentialsProvider.addCredentials(username, password);
    }

    // jsoup请求并解析

    @Test
    public void jsoupCrawl() throws IOException {

        String url = "http://passport.tianya.cn/login.jsp";
        Connection con = Jsoup.connect(url);// 获取请求连接
        // 浏览器可接受的MIME类型。
        con.header("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        con.header("Accept-Encoding", "gzip, deflate");
        con.header("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        con.header("Connection", "keep-alive");
        con.header("Host", url);
        con.header("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0");
        Document doc = con.get();
        Elements loginBtn = doc.select("#loginBtn");
        System.out.println(loginBtn.text());// 获取节点中的文本，类似于js中的方法
    }

    @Test
    public void testHomePage_Firefox() throws Exception {
//        String TargetURL="http://person.sac.net.cn/pages/registration/sac-publicity.html";
//        String TargetURL="http://comment.news.163.com/news2_bbs/CEBLDGEF0001899N.html";
//        String TargetURL="http://news.163.com/";
//        String TargetURL="http://news.163.com/17/0303/13/CEJUHPM8000187V9.html";
//        String TargetURL = "http://comment.news.163.com/news2_bbs/CEJOE7RO000181TI.html";
//        String TargetURL = "http://www.toutiao.com/ch/funny/";
        String TargetURL = "http://music.163.com/#/song?id=476630320";
//		String TargetURL="http://www.baidu.com";
        //模拟一个浏览器
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
//		final WebClient webClient=new WebClient(BrowserVersion.FIREFOX_10,"http://myproxyserver",8000);   //使用代理
        //设置webClient的相关参数
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        //模拟浏览器打开一个目标网址
        final HtmlPage page = webClient.getPage(TargetURL);
//        System.out.println(page.asText());
//        System.out.println(page.asXml());
        String xml = page.asXml();
        System.out.println("------------------");
        System.out.println(page.getUrl());
        //page.executeJavaScript("javascript:searchFinishPerson('6655',2);");
//        ScriptResult sr=page.executeJavaScript("javascript:searchFinishPerson('6655',2);");
//        HtmlPage newPage=(HtmlPage)sr.getNewPage();
//        System.out.println(newPage.asText());
//        System.out.println(newPage.getUrl());
    }

    @Test
    public void testNextPage() throws Exception {
        //tiePage.showPage(3);
        String TargetURL = "http://comment.news.163.com/news2_bbs/CEJ5BP5100018AOP.html";
        //模拟一个浏览器
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        //设置webClient的相关参数
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        //模拟浏览器打开一个目标网址
        final HtmlPage page = webClient.getPage(TargetURL);
//        ScriptResult sr=page.executeJavaScript("tiePage.showPage(3);");
        ScriptResult sr = page.executeJavaScript("javascript:searchFinishPerson('6655',2);");
        HtmlPage newPage = (HtmlPage) sr.getNewPage();
        System.out.println(newPage.asText());
    }

    @Test
    public void testCommentsUtil() {
        CommentsUtil commentsUtil = new CommentsUtil();
//        commentsUtil.getComments("");
    }

    @Test
    public void javaTest() {
        Integer[] num = {0, 2, 3, 4, 5, 6, 8, 9, 10, 12};
        List<Integer> list = Arrays.asList(num);
        Collections.shuffle(list);
        for (Integer i : list) {
            System.out.print(i + " ");
            System.out.println(i == 2);
        }
    }

    @Test
    public void dataUtilTest() {
        DataUtil dataUtil = new DataUtil();
//        String result = DataUtil.doGetCharset("http://neihanshequ.com/pic/","utf-8");
        String result = DataUtil.doGetCharset("http://news.163.com/17/0505/23/CJN8TCVO000187VE.html", "gb2312");
        Document doc = Jsoup.parse(result);
        System.out.println(result);
    }

    @Test
    public void test163() throws Exception {
//        Jsoup.connect("http://music.163.com/playlist?id=317113395")
//                .header("Referer", "http://music.163.com/")
//                .header("Host", "music.163.com").get().select("ul[class=f-hide] a")
//                .stream().map(w-> w.text() + "-->" + w.attr("href"))
//                .forEach(System.out::println);
//        Document doc = Jsoup.connect("http://music.163.com/discover/toplist")
//                .header("Referer", "http://music.163.com/")
//                .header("Host", "music.163.com").get();
        Document doc = Jsoup.connect("http://music.163.com/song?id=468517654")
                .header("Referer", "http://music.163.com/")
                .header("Host", "music.163.com").get();
        Elements divs = doc.getElementsByClass("cntwrap");
        System.out.println();
    }

    @Test
    public void test163Comment() throws Exception {
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
        Document document = Jsoup.connect("http://music.163.com/weapi/v1/resource/comments/R_SO_4_478029412/").cookie("appver", "1.5.0.75771")
                .header("Referer", "http://music.163.com/").data("params", params).data("encSecKey", encSecKey)
                .ignoreContentType(true).post();
        System.out.println("评论：" + document.text());
        try {
            MusicComments musicComment = JSON.parseObject(document.text(), MusicComments.class);
            MusicComment[] musicComments = musicComment.getComments();
            for (MusicComment comment : musicComments) {
                MusicCommentReply[] repliy = comment.getBeReplied();
                if (comment.getBeReplied() != null && comment.getBeReplied().length > 0) {
                    System.out.println(repliy[0].getContent()+"==");
                }
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

}