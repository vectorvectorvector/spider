package com.ericsoft.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.ericsoft.bmob.bson.BSONObject;
import com.ericsoft.bmob.model.BmobInsertResult;
import com.ericsoft.bmob.model.BmobSearchHealth;
import com.ericsoft.bmob.model.BmobSearchJokes;
import com.ericsoft.bmob.restapi.Bmob;
import com.spider.model.Health;
import com.spider.model.Joke;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BmobHealthUtils {

    public BmobHealthUtils() {
        initBmob();//初始化
    }

    /**
     * 向表中插入数据
     *
     * @return String:
     * 插入失败 Not Found:(insert)https://api.bmob.cn/1/classes/GameScore
     * 插入成功 {"createdAt":"2017-04-29 19:45:35","objectId":"f2adb5ba0a"}
     */
    public BmobInsertResult insert(Health health, String table) {
        BSONObject bson = new BSONObject();
        bson.put("imgurl", !health.getImgurl().equals("") ? health.getImgurl() : "-");
        bson.put("url", !health.getUrl().equals("") ? health.getUrl() : "-");
        bson.put("title", !health.getTitle().equals("") ? health.getTitle() : "-");
        bson.put("subtitle", !health.getSubTitle().equals("") ? health.getSubTitle() : "-");
        bson.put("content", !health.getContent().equals("") ? health.getContent() : "-");
        bson.put("comment", !health.getComment().equals("") ? health.getComment() : "-");
        bson.put("origin", !health.getOrigin().equals("") ? health.getOrigin() : "-");
        String result = Bmob.insert(table, bson.toString());
        try {
            return JSON.parseObject(result, BmobInsertResult.class);
        } catch (JSONException e) {
            return new BmobInsertResult();
        }
    }

    //根据新闻标题查询新闻在数据库中的ID
    public BmobSearchHealth Search(String url, int limit, String table) {
        //查询语句和查询条件
        if (url.equals("")) {
            url = "+";
        }
        String sql = "select imgurl,url,title,subtitle,content,comment,origin from " + table + " where url = ? limit ?";
        String value = "\"" + url + "\"," + limit;
        String result = Bmob.findBQL(sql, value);
        try {
            return JSON.parseObject(result, BmobSearchHealth.class);
        } catch (JSONException e) {
            return null;
        }
    }

    //使用RestAPI前必须先初始化，KEY可在Bmob应用信息里查询。
    private static void initBmob() {
        Bmob.initBmob("fe2d0963f4f6019ccb3a23a76de05397", "b2584e80f16b216bef83fe4640463354");
    }

    private static void update() {
        BSONObject bson = new BSONObject();
        bson.put("score", 100);
        //score 修改为100
        Bmob.update("Your TableName", "Your objectId", bson.toString());
    }

    private static void delete() {
        Bmob.delete("Your TableName", "Your objectId");
    }

    private static void count() {
        BSONObject where = new BSONObject();
        where.put("score", 100);
        Bmob.count("Your TableName", where.toString());
    }

    public static void main(String[] args) {
        //BSONObject 简单使用
//		CreateClassBSONObject();

        BmobHealthUtils bmobJokeUtils = new BmobHealthUtils();
        Health health = new Health();
        health.setImgurl("http://p3.pstatp.com/large/1e130002b1a58e7c72c1");
        health.setComment("哈哈哈");
        health.setOrigin("网易健康");
        String content = "<div class=\\\"post_text\\\" id=\\\"endText\\\">  <p class=\\\"otitle\\\"> （原标题：春天的8种营养果！再不吃就晚了） </p>  <p>春天来了，部分新鲜水果上市了！有8种不能错过的营养果，给身体补充动力，还能刮油!</p> <p><strong>蛇皮果：高钾低钠的力量之果</strong></p> <p>蛇皮果产于印尼等热带地区，棕褐色的外皮上覆盖一层密密麻麻的鳞片，像是蛇皮，因此形象地称为“蛇皮果”。蛇皮果的果胶和钾含量居水果之首，对身体十分有益。</p> <p class=\\\"f_center\\\"><img alt=\\\"\\\\\" border=\\\"0\\\" src=\\\"http://cms-bucket.nosdn.127.net/catchpic/2/25/2536c1ff658ed0a340d6f5d5eb39eb5e.jpg?imageView&amp;thumbnail=550x0\\\" width=\\\"375\\\" style=\\\"border-width: initial; border-style: none; border-; vertical-align: middle;\\\" /></p> <p>蛇皮果的果仁如同大蒜头，大概有3~4个瓣儿，果肉白中带黄，肉质板实，类似板栗。蛇皮果既有苹果的爽脆，又有菠萝的香气，气味馥郁芬芳，口感酸甜。</p> <p>蛇皮果含有丰富的有机酸，可以美容护肤，印尼本地的姑娘都把它当作“美容果”。蛇皮果富含磷脂，有健脑的作用，又被称为“记忆果”。　　蛇皮果还被印尼人称为“力量之果”，这是由于热带地区气温普遍较高，人体的钾很容易随尿液和汗液流失，身体缺乏钾，就会疲乏无力，提不起精神。而这时吃几个含钾丰富的蛇皮果，身体也会觉得有力气了。春日外出郊游，活动量较大，也容易出汗，带点蛇皮果作为野餐水果非常不错。</p> <p>另外，饮食口味比较重的人及高血压人群也适合多吃些蛇皮果。长期摄入过多的钠盐会损伤血管，增加高血压的发病风险，而蛇皮果中丰富的钾可以结合游离的钠将其排出体外，从而减少高钠对身体的伤害，维持血压的稳定。</p> <p><strong>人参果：低糖多维C的减肥果</strong></p> <p>说起人参果，都会想起西游记中“胖阿福”的造型，正宗的人参果成熟时果皮呈金黄色，外形似人的心脏，清香淡雅，柔滑爽口。</p> <p class=\\\"f_center\\\"><img alt=\\\"春天的8种营养果 再不吃就晚了！\\\" src=\\\"http://cms-bucket.nosdn.127.net/ad851645ce00414195bd81914e72859d20170406113910.jpeg?imageView&amp;thumbnail=550x0\\\" /><br /></p> <p>国家二级公共营养师 王涛介绍，人参果含有丰富的维生素C，能够增强人体的免疫力，缓解感冒、鼻炎等症状。春天时节建议抵抗力差和容易感冒的人群可以常吃。由于元素硒有很好的抗癌效果，因此常吃人参果有利于心血管健康，增强人体的免疫力，还能帮助抗衰老、降血压和降血糖。</p> <p>人参果的果肉淡黄柔滑，爽口多汁。其皮略带紫色，含有少量的花青素，因此如果连皮吃更有营养。</p> <p>不仅能作为水果，人参果也可以入菜，人参果炒肉片就是一道颇具特色的菜肴。其中的维生素C可以促进瘦肉中铁的吸收，同时还可以促进肉中的蛋白质合成胶原蛋白，帮助美白养颜和改善皮肤弹性，是爱美女性的一道美容菜。</p> <p>人参果含糖量低，味道偏清淡，甜度不浓郁，虽然口感上并不受普通人群欢迎，但其高营养低糖分的特点恰好能满足肥胖者、糖尿病患者、减重节食人群的需要，可以选择人参果作为每日的水果。</p> <p><strong>枇杷：润肺的好食材</strong></p> <p>金色的小果子，味道甜中带酸，一走进水果��市，香气浓郁的枇杷一下子能把人吸引住。枇杷的表皮黄而多蜡质，因此又被称为黄蜡丸，剥开外皮，里面的果肉味道香浓、柔软多汁、酸甜适口。</p> <p class=\\\"f_center\\\"><img alt=\\\"春天的8种营养果 再不吃就晚了！\\\" src=\\\"http://cms-bucket.nosdn.127.net/f423cab26b114c0c9d755d23dab7736720170406113910.jpeg?imageView&amp;thumbnail=550x0\\\" /><br /></p> <p>枇杷被誉为“果中之皇”，是一种低能量高营养的水果(能量含量较低，39千卡每百克)，其中含有人体所需的8种氨基酸以及多种有机酸类物质，而且枇杷果肉中还含有具有抗癌活性的苦杏仁苷。</p> <p>枇杷是润肺的好食材，《本草纲目》中记载：“枇杷能润五脏，滋心肺”，枇杷叶也有缓解肺热干咳的作用，川贝枇杷膏就是以枇杷叶为主要原料加工而成的。我一般感冒干咳时会吃一些枇杷，每天吃上四五颗，能有一定的润肺养肺、止咳化痰的作用。</p> <p>枇杷挑选和食用时都是有一定讲究的，我一般喜欢挑选果皮橙黄，茸毛完整(果实耳背处)，个头大而匀称的果实，这样的吃起来味道才好。另外，成熟的枇杷皮和头部的把都是很容易去掉的，但是尾部的茸毛和赃物比较多，吃前一定要清洗干净。注意新鲜枇杷不易存放，一次不要买得太多。</p> <p><strong>腰芒：护肤好�锸�</strong></p> <p>颜色黄灿灿，鸡蛋般大小，一头尖一头圆，汁液多特别甜，春天来了，芒果又如约而至，有一种叫做腰芒的小芒果，尤其香甜。</p> <p class=\\\"f_center\\\"><img alt=\\\"春天的8种营养果 再不吃就晚了！\\\" src=\\\"http://cms-bucket.nosdn.127.net/bcb8fc8c890745c9a1e4c32f18e2e1b020170406113910.jpeg?imageView&amp;thumbnail=550x0\\\" /><br /></p> <p>“可能因为它小巧纤细，让人联想到杨柳细腰而得名的吧。”北京朝阳医院营养科营养师宋新解释。芒果被称为热带果王，味道香甜，而小腰芒的口感在芒果家族中可是数一数二的。虽然芒果纤维较多，但腰芒吃起来特别细腻、顺滑，再加上股浓浓的香甜味，真是让人欲罢不能。</p> <p>“单从芒果的颜色来看，就知道它富含胡萝卜素。”宋新说，胡萝卜素能够在体内转化为维生素A，不仅能保护眼睛，对皮肤也很有好处，爱美的女士们可不要错过。很多人吃完芒果，嘴角或嗓子眼发痒，这是过敏了。宋新说，可以把芒果肉片下来，切成小块吃，这样导致过敏的芒果汁就不会流到嘴角里了，如果是嗓子过敏，可以赶快喝点白开水。</p> <p>国家二级公共营养师王静说，可以选些自己爱吃的水果和芒果、酸奶一起拌个水果沙拉，因为芒果有提味的作用，所以会更好吃。</p> <p><strong>草莓：润肺生津</strong></p> <p>说起草莓，中国保健协会食物营养与安全专业委员会会长孙树侠赞不绝口，“就说这诱人的红色，其花青素含量就少不了，难怪是蓝莓出现前的‘抗氧化冠军’”。而且，草莓的维生素C含量也不低，每100克为47毫克，比橙子还高。此外，草莓还富含有机酸、果胶等，真可以说是水果里的“综合冠军”了。</p> <p class=\\\"f_center\\\"><img alt=\\\"\\\\\" src=\\\"http://cms-bucket.nosdn.127.net/catchpic/9/9a/9a19c7169a49a22483503549ca8e5c37.jpg?imageView&amp;thumbnail=550x0\\\" style=\\\"border-width: 0px; border-; vertical-align: middle; width: 454px;\\\" /></p> <p>草莓要挑个头适中、颜色均匀、软硬适中的。个头太大，尤其是有裂口的草莓，很可能是使用过多的膨大素。有大块青或黄色的草莓也不好，花青素含量相对较低。</p> <p>中医认为，草莓味甘性凉，有润肺生津，利尿消肿的作用，有肺火、小便不利的人不妨适当多吃一些。但胃肠功能不佳的人不宜多吃。牙口不好的老人，也可用打浆机将草莓打成糊糊吃，或者把糊糊抹在面包片上吃。妈妈们也可以给孩子做草莓沙拉、草莓馅饼吃。</p> <p><strong>小柑橘：补充维生素</strong></p> <p class=\\\"f_center\\\"><img alt=\\\"春天的8种营养果 再不吃就晚了！\\\" src=\\\"http://cms-bucket.nosdn.127.net/f59a42c1cfd34525a804107d2916390620170406113910.jpeg?imageView&amp;thumbnail=550x0\\\" /><br /></p> <p>华中科技大学同济医学院营养与食品卫生学系教授黄连珍介绍，柑橘类水果是大家非常喜爱的一类水果，其富含维生素A、B1、B2、C以及胡萝卜素等维生素，而且其中还有丰富的有机酸(柠檬酸等)，可以有效调节人体新陈代谢。但是注意吃橘子要适量，吃得过多可能会引起皮肤上的黄色素沉着。另外，橘子不宜空腹食用，因为其富含有机酸，空腹吃会刺激胃黏膜，使脾胃满闷嗝酸。</p> <p><strong>柠檬：天然美容品</strong></p> <p class=\\\"f_center\\\"><img alt=\\\"春天的8种营养果 再不吃就晚了！\\\" src=\\\"http://cms-bucket.nosdn.127.net/0f46327eb0c14ba8b103625244ef4ba920170406113910.jpeg?imageView&amp;thumbnail=550x0\\\" /><br /></p> <p>柠檬由于味道较酸，一般不直接生吃，而是作为配菜或调味品。柠檬有“西餐之王”的美誉，凉菜、主菜、饮料中都能派上用场。</p> <p>国家高级营养配餐师魏立军说，柠檬特殊的香气能祛除肉类和水产品的腥膻气味，并能使肉质更加细嫩。吃煎鳕鱼时将柠檬汁挤出来滴在鳕鱼上，巴掌大的鳕鱼滴4~5滴就可以。鳕鱼遇到柠檬汁，不但腥味没了，肉也更加鲜香;在烤好后的鱼、鸡翅上滴少许柠檬汁，味道也更香;还可以将柠檬的果肉切碎，和海鲜酱油一起调成汁儿，配着海鲜吃。</p> <p>42柠檬富含维生素C，是种天然美容佳品，每天一杯柠檬水，能美白滋润皮肤。但应注意，柠檬味道酸，所以胃溃疡、胃酸分泌过多者应少吃柠檬。</p> <p><strong>荸荠：润燥之果</strong></p> <p>荸荠被称为地下雪梨、江南人参，是很好的润燥之果，初春时节，天气仍然比较干燥，不妨吃点荸荠。荸荠皮薄，肉脆嫩多汁，清香爽口，是清肺燥的好食物，《本草纲目》上记载，荸荠有降火、补肺凉肝、消食化痰之效。而中医上有去燥止咳作用的五汁饮，就是用新鲜荸荠、莲藕、甘蔗、麦冬及芦根一起榨汁。自己在家也可以榨汁喝，种类不定，雪梨、萝卜之类的配料也可以入选。</p> <p class=\\\"f_center\\\"><img alt=\\\"春天的8种营养果 再不吃就晚了！\\\" src=\\\"http://cms-bucket.nosdn.127.net/7524455e6f7d4e8e97d2eb5f4208c7a720170406113911.jpeg?imageView&amp;thumbnail=550x0\\\" /><br /></p> <p>说荸荠是“润燥之果”，还有一个原因，就是荸荠中的粗蛋白、抗性淀粉、膳食纤维等能促进大肠蠕动、调节肠胃功能，防止便秘和大便干燥。而荸荠也属于高钾低钠食物，有利于降低高血压、利尿利湿。</p> <p>  <!-- AD200x300_2 --> </p> <div class=\\\"gg200x300\\\">   <div style=\\\"position:relative;height:250px;\\\">    <a href=\\\"http://gb.corp.163.com/gb/legal.html\\\" class=\\\"ad_hover_href\\\"></a>    <iframe src=\\\"http://g.163.com/r?site=netease&amp;affiliate=jiankang&amp;cat=article&amp;type=logo300x250&amp;location=1\\\" width=\\\"300\\\" height=\\\"250\\\" frameborder=\\\"no\\\" border=\\\"0\\\" marginwidth=\\\"0\\\" marginheight=\\\"0\\\" scrolling=\\\"no\\\"> </iframe>   </div>  </div> <p>不过，荸荠还是尽量熟吃，生吃可能会有感染姜片虫病的风险(菱角、茭白、荸荠等水生植物容易寄生姜片吸虫，尽量不要生吃。)但姜片吸虫的囊蚴不耐高温和干燥，在沸水中煮一两分钟或在阳光下暴晒一天即可死亡。将荸荠吊起来晒干一些，去皮，在沸水中烫1分钟，安全放心。</p> <p>荸荠�斐裕�方法多样。煮荸荠是很好的天然零食，如果当蔬菜吃，因淀粉含量高，可替代部分主食(三四个荸荠热量约为60千卡，相当于一两米饭)。切片炒食，也可切块炖煮，很多人也喜欢把荸荠切成碎丁，做丸子或包子、饺子的馅料，清新爽脆;荸荠还能做马蹄糕、玫瑰马蹄饼等点心;用荸荠煮糖水喝也不错，和雪梨、甘蔗、藕等一起煮汤，口感好，又能滋养身体。(综合自健康时报)</p>  <p></p>  <div class=\\\"ep-source cDGray\\\">   <span class=\\\"left\\\"><a href=\\\"http://jiankang.163.com/\\\"><img src=\\\"http://img1.cache.netease.com/f2e/health/post1301_ad/images/end_health.png\\\" alt=\\\"耿乙文\\\" width=\\\"13\\\" height=\\\"12\\\" class=\\\"icon\\\" /></a> 本文来源：健康时报 </span>   <!--耿乙文_NJ6040-->  <span class=\\\"ep-editor\\\">责任编辑：耿乙文_NJ6040</span>  </div> </div>";
        content = content.replace("\n", "");
//        content = content.replace("\"", "\\\"");
        health.setContent(content);
//        health.setContent("内容内容内容内容内容内容内容内容内容内容内容");
        health.setTitle("标题");
        bmobJokeUtils.insert(health, "health");

        BmobSearchHealth bmobSearchJokes = bmobJokeUtils.Search("标题", 1, "health");
        System.out.println();
//        initBmob();//初始化
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