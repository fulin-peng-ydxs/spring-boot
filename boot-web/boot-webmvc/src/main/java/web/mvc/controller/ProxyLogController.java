package web.mvc.controller;


import commons.model.annotations.log.LogSave;
import commons.model.entity.User;
import commons.model.web.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志控制器
 *
 * @author pengshuaifeng
 * 2024/8/24
 */
@Api(tags = "业务功能-日志代理演示")
@RestController
@RequestMapping("/proxy/log" )
public class ProxyLogController {


    /**
     * 日志代理
     * 2024/8/24 12:20
     * @param user 日志
     * @return void
     */
    @LogSave(logType = "测试",logDescriptionTemplate = "测试日志代理操作：用户名：{name}-年龄：{res.body.age}-汽车：{car.name}-外接参数：%s", logParamIndex ="1" )
    @ApiOperation(value = "日志代理",notes = "日志代理")
    @RequestMapping("/test")
    public Response<?> log(@RequestBody  User user,String param1){
        user.setName("222");
        user.setAge(22);
        return Response.success(user);
    }

}
