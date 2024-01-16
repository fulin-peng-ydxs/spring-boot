package swagger.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import swagger.entity.User;

import java.util.Collections;
import java.util.Map;

/**
 * swagger接口服务配置演示
 * @author pengshuaifeng
 * 2024/1/13
 */
@Api(tags = "快速开始")
@RestController
@RequestMapping("quick")
public class QuickController {

    @ApiOperation(value = "post服务api",notes = "post方法调用")
    @RequestMapping(value = "/post",method = RequestMethod.POST)
    public Map<String,Object> post(@ApiParam("请求参数") @RequestBody User user){
        return Collections.singletonMap("body",user);
    }

    @ApiOperation(value = "get服务api",notes = "get方法调用")
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public Map<String,Object> post(@ApiParam("请求参数")  @RequestParam String user){
        return Collections.singletonMap("body",user);
    }
}
