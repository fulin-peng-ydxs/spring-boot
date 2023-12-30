package commons.path;


import org.junit.Test;
import org.springframework.util.AntPathMatcher;

/**
 * 路径工具类测试
 * author: pengshuaifeng
 * 2023/11/1
 */
public class PathTest {

    @Test
    public void test(){
        AntPathMatcher matcher = new AntPathMatcher();
        // ?
        System.out.println(matcher.match("/test/a?c", "/test/abc"));// true
        // *
        System.out.println(matcher.match("/test/*", "/test/"));// true
        System.out.println(matcher.match("/test/*", "/test/aa"));// true
        System.out.println(matcher.match("/test/*.html", "/test/aa"));// false
        // **
        System.out.println(matcher.match("/test/**", "/test/"));// true
        System.out.println(matcher.match("/test/**", "/test/aa"));// true
        System.out.println(matcher.match("/test/**", "/test/aa/bb"));// true
        // {id}
        System.out.println(matcher.match("/test/{id}", "/test/111"));// true
        System.out.println(matcher.match("/test/a{id}", "/test/a111"));// true
        System.out.println(matcher.match("/test/{id}/aa", "/test/111/aa"));// true
        System.out.println(matcher.match("/test/{id}-{name}/aa", "/test/111-haha/aa"));// true
        // {id:[a-z]+}
        System.out.println(matcher.match("/test/{id:[a-z]+}", "/test/111"));// false
        System.out.println(matcher.match("/test/{id:[a-z]+}", "/test/abc"));// true
        System.out.println(matcher.match("/test/{id:\\w+}", "/test/1a_"));// true
        System.out.println(matcher.match("/test/{id:\\w+}", "/test/--"));// false
    }
}
