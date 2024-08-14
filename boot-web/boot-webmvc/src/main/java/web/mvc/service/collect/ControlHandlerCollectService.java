package web.mvc.service.collect;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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
@Getter
@Slf4j
@Service
public class ControlHandlerCollectService {

    /**
     * 应用容器
     */
    private final ApplicationContext context;

    /**
     * 请求映射处理器映射
     */
    private final RequestMappingHandlerMapping requestMappingHandlerMapping ;

    /**
     * 控制器集
     */
    private final Map<String,HandlerMethod> controllerMethods=new HashMap<>();

    private final Map<Class<?>,Map<String,HandlerMethod>> controllerByTypeMethods=new HashMap<>();

    /**
     * 初始化构造
     * 2024/8/13 下午3:11
     * @author fulin-peng
     */
    public ControlHandlerCollectService(ApplicationContext context){
        this.context=context;
        this.requestMappingHandlerMapping=getRequestMappingHandlerMapping(context);
        collect(context,controllerMethods,controllerByTypeMethods);
    }

    /**
     * 收集控制器
     * 2024/1/8 22:40
     * @author pengshuaifeng
     * @param applicationContext web应用容器
     * <p>key为访问的url、value为HandlerMethod对象（封装了实际的控制器对象和方法描述信息）</p>
     */
    public void collect(ApplicationContext applicationContext,Map<String, HandlerMethod> controllerMethods,
                        Map<Class<?>,Map<String,HandlerMethod>> controllerByTypeMethods){
        try {
            log.debug("正在收集系统接口信息");
            getRequestMappingHandlerMapping(applicationContext).getHandlerMethods().forEach((key,value)->{
                String path = key.getPatternsCondition().getPatterns().stream().findFirst().get();
                log.debug("收集系统接口信息：{} -> {}",path,value.getBeanType());
                controllerMethods.put(path,value);
                Map<String, HandlerMethod> handlerMethodMap = controllerByTypeMethods.get(value.getBeanType());
                if(handlerMethodMap==null){
                    handlerMethodMap=new HashMap<>();
                }
                handlerMethodMap.put(path,value);
                controllerByTypeMethods.put(value.getBeanType(),handlerMethodMap);
            });
            log.debug("收集系统接口信息总数：{}",controllerMethods.size());
        } catch (Exception e) {
            log.error("收集系统接口资源异常:",e);
        }
    }

    /**
     * 获取请求映射处理器映射
     * 2024/8/13 下午2:49
     * @author fulin-peng
     */
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping(ApplicationContext applicationContext){
        RequestMappingHandlerMapping requestMappingHandlerMapping=null;
        Map<String, RequestMappingHandlerMapping> contextBeansOfType = applicationContext.getBeansOfType(RequestMappingHandlerMapping.class);
        if(!contextBeansOfType.isEmpty()) {
            if(contextBeansOfType.size()>1){
                requestMappingHandlerMapping=applicationContext.getBean("requestMappingHandlerMapping",RequestMappingHandlerMapping.class);
            }else{
                requestMappingHandlerMapping=applicationContext.getBean(RequestMappingHandlerMapping.class);
            }
        }
        return requestMappingHandlerMapping;
    }

}
