package web.mvc.error;

import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import java.util.Map;

/**
 * 异常属性自定义
 * 2023/12/20 00:09
 * @author pengshuaifeng
 */
@Component
public class MyErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
        Map<String, Object> map = super.getErrorAttributes(requestAttributes, includeStackTrace);
        map.put("customer", requestAttributes.getAttribute("customer", 0));
        return map;
    }
}