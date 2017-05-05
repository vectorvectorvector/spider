import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.ericsoft.bmob.model.BmobInsertResult;
import com.ericsoft.bmob.model.BmobSearchNews;
import org.junit.Test;

/**
 * Created by Administrator on 2017/4/29/0029.
 */
public class BmobTest {
    @Test
    public void insertTest(){
        try {
//        String result="{\"createdAt\":\"2017-04-29 22:03:41\",\"objectId\":\"66405df987\"}";
            String result="Not Found:(insert)https://api.bmob.cn/1/classes/GameScore";
            BmobInsertResult bmobInsertResult = JSON.parseObject(result,BmobInsertResult.class);
            System.out.println(bmobInsertResult.getCreatedAt().toString());
            System.out.println(bmobInsertResult.getObjectId());
        }catch (JSONException e){
            System.out.println(e.getMessage());
        }
    }
    @Test
    public void searchTest(){
        try {
            String result="{\"results\":[{\"channelname\":\"-\",\"comment\":\"-\",\"content\":\"-\",\"createdAt\":\"2017-04-30 11:24:45\",\"id\":0,\"imgurl\":\"-\",\"objectId\":\"bc096b2a19\",\"time\":{\"__type\":\"Date\",\"iso\":\"2017-04-30 11:24:19\"},\"title\":\"新闻标题\",\"updatedAt\":\"2017-04-30 11:24:45\",\"url\":\"-\"}]}";
            BmobSearchNews bmobSearchNews = JSON.parseObject(result,BmobSearchNews.class);
            System.out.println();
        }catch (JSONException e){
            System.out.println(e.getMessage());
        }
    }

}
