package web.mvc.service.collect;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
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
public class ControlHandlerCollectService  implements ApplicationListener<ContextRefreshedEvent> {

    private final Map<String,HandlerMethod> controllerMethods=new HashMap<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            log.debug("正在收集系统接口信息");
            ApplicationContext context = event.getApplicationContext();
            Map<String, RequestMappingHandlerMapping> contextBeansOfType = context.getBeansOfType(RequestMappingHandlerMapping.class);
            RequestMappingHandlerMapping controllerMapping;
            if(contextBeansOfType.size()>0) {
                if(contextBeansOfType.size()>1){
                    controllerMapping=context.getBean("requestMappingHandlerMapping",RequestMappingHandlerMapping.class);
                }else{
                    controllerMapping=context.getBean(RequestMappingHandlerMapping.class);
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
    }

}
