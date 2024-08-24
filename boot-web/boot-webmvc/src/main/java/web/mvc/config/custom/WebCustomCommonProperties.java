package web.mvc.config.custom;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * web自定义组件属性配置
 *
 * @author pengshuaifeng
 * 2024/8/24
 */
@Data
@ConfigurationProperties(prefix = "web.custom")
public class WebCustomCommonProperties {

    private Proxy proxy;

    private Proxy.Controller controller;
    /**
     * 代理组件配置类
     * 2024/8/24 12:20
     * @author pengshuaifeng
     */
    @Data
    public static class Proxy {
        //日志配置
        private Log log;
        //controller配置
        private Controller controller;

        @Data
       public static class  Log {
            //日志开关
            private boolean enable;
        }

        @Data
        public static class Controller {
            //controller代理开关
            private boolean enable;
        }
    }
}
