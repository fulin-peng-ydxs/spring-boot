package commons.path;


import lombok.Getter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import java.util.Collection;

/**
 * AntPathMatcher：路径匹配工具类
 * author: pengshuaifeng
 * 2023/11/1
 */
@Getter
public class AntPathMatcherUtil {


    private static final PathMatcher matcher = new AntPathMatcher();

    /**
     * path是否能够匹配
     * 2024/1/9 22:50
     * @author pengshuaifeng
     * @param path 需要匹配的路径
     * @param sources 匹配路径源
     */
    public static boolean match(String path, Collection<String> sources){
        return sources.stream().anyMatch((x) -> matcher.match(x, path));
    }

}
