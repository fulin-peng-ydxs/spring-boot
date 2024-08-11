package md5;


import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.jupiter.api.Test;

/**
 * @author pengshuaifeng
 * 2024/8/10
 */
public class Md5Test {

    @Test

    public void testMd5(){
        //生成随机盐
        //md5 + salt + hash散列
        Md5Hash md5Hash = new Md5Hash("123456","28qr0xu%",1024);
        System.out.println(md5Hash.toHex());
    }

}
