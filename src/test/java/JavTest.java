import org.junit.Test;

import java.util.Random;

/**
 * Created by zhouchao on 2017/5/25/0025.
 */
public class JavTest {

    @Test
    public void javaTest() {
        Random random = new Random();
        while (true){
            System.out.println(random.nextInt(20));
        }
    }
}
