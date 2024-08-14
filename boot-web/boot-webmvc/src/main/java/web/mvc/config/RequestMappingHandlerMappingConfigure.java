package web.mvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import web.mvc.controller.ValidateController;
import web.mvc.service.collect.ControlHandlerCollectService;

/**
 * 请求映射处理器配置
 *
 * @author fulin-peng
 * 2024-08-13  14:17
 */
@Configuration
public class RequestMappingHandlerMappingConfigure {

    /**
     * 设置映射器映射使用的默认处理器
     */
    public RequestMappingHandlerMappingConfigure(ControlHandlerCollectService collectService, RequestMappingHandlerMapping requestMappingHandlerMapping){
        //设置默认的处理器为DataCollectController
        requestMappingHandlerMapping.
                setDefaultHandler(collectService.getControllerByTypeMethods()
                        .get(ValidateController.class).get("/data/collect").createWithResolvedBean());
    }
}
