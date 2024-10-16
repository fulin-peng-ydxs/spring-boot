package commons.model.web.request.wrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

public class CustomRequestWrapper extends HttpServletRequestWrapper {

    private final Map<String, String[]> modifiedParams = new HashMap<>();

    public CustomRequestWrapper(HttpServletRequest request) {
        super(request);
        // 初始化时将原始请求参数放入自定义参数中
        modifiedParams.putAll(request.getParameterMap());
    }

    // 自定义方法：设置新的请求参数
    public void setParameter(String name, String value) {
        modifiedParams.put(name, new String[]{value});
    }

    @Override
    public String getParameter(String name) {
        String[] values = modifiedParams.get(name);
        return (values != null && values.length > 0) ? values[0] : null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return modifiedParams;
    }

    @Override
    public String[] getParameterValues(String name) {
        return modifiedParams.get(name);
    }
}
