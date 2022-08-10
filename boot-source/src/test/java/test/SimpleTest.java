package test;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author PengFuLin
 * @description:
 * @date 2022/4/19 20:43
 */
public class SimpleTest {
    @Test
    public void test(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(new Date());
        System.out.println(format);
    }
}
