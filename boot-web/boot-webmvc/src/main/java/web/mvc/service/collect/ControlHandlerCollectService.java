package web.mvc.service.collect;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制器收集器
 *
 * @author pengshuaifeng
 * 2023/12/28
 */
@Slf4j
@Service
public class ControlHandlerCollectService{

    @Autowired
    private ApplicationContext context;

    /**
     * 控制器集
     */
    private volatile Map<String,HandlerMethod> controllerMethods;

    /**
     * 获取控制器集
     * 2024/1/8 22:40
     * @return 控制器集合
     * <p>key为访问的url、value为HandlerMethod对象（封装了实际的控制器对象和方法描述信息）</p>
     * @author pengshuaifeng
     */
    public Map<String,HandlerMethod> getControllerMethods(){
        if(controllerMethods==null){
            synchronized (ControlHandlerCollectService.class) {
                if(controllerMethods == null) {
                    controllerMethods = collect(context);
                }
            }
        }
        return controllerMethods;
    }

    /**
     * 收集控制器
     * 2024/1/8 22:40
     * @author pengshuaifeng
     * @param applicationContext web应用容器
     * @return 控制器集合
     * <p>key为访问的url、value为HandlerMethod对象（封装了实际的控制器对象和方法描述信息）</p>
     */
    public Map<String,HandlerMethod> collect(ApplicationContext applicationContext){
        HashMap<String, HandlerMethod> controllerMethods = new HashMap<>();
        try {
            log.debug("正在收集系统接口信息");
            Map<String, RequestMappingHandlerMapping> contextBeansOfType = applicationContext.getBeansOfType(RequestMappingHandlerMapping.class);
            RequestMappingHandlerMapping controllerMapping;
            if(contextBeansOfType.size()>0) {
                if(contextBeansOfType.size()>1){
                    controllerMapping=applicationContext.getBean("requestMappingHandlerMapping",RequestMappingHandlerMapping.class);
                }else{
                    controllerMapping=applicationContext.getBean(RequestMappingHandlerMapping.class);
                }
                controllerMapping.getHandlerMethods().forEach((key,value)->{
                    String path = key.getPatternsCondition().getPatterns().stream().findFirst().get();
                    log.debug("收集系统接口信息：{} -> {}",path,value.getBeanType());
                    controllerMethods.put(path,value);
                });
            }
            log.debug("收集系统接口信息总数：{}",controllerMethods.size());
        } catch (Exception e) {
            log.error("收集系统接口资源异常:",e);
        }
        return controllerMethods;
    }

}
