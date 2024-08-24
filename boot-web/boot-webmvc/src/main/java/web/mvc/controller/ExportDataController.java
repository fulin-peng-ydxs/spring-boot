package web.mvc.controller;


import commons.model.annotations.excel.ExcelDownload;
import commons.model.entity.User;
import commons.model.web.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 导出数据控制器
 *
 * @author pengshuaifeng
 * 2024/1/21
 */
@Api(tags = "业务功能-导出数据演示")
@RestController
@RequestMapping("/mvc/export")
public class ExportDataController {

    @GetMapping("/excel")
    @ApiOperation(value = "excel导出-默认",notes = "数据导出excel")
    @ExcelDownload(fileName ="excel导出测试" )
    public Response<?> excel(){
        List<User> users = Arrays.asList(new User("test1", "test1-1", 1,"湖南",null),
                new User("test2", "test2-1", 12,"湖北",null),
                new User("test3", "test3-1", 99,"广东",null));
        return Response.success(users);
    }


    @GetMapping("/excel/custom")
    @ApiOperation(value = "excel导出-自定义",notes = "数据导出excel")
    @ExcelDownload(fileName ="excel导出测试",fieldNames = {"name","sex","age"})
    public Response<?> excelSort(){
        List<User> users = Arrays.asList(new User("test1", "test1-1", 1,"湖南",null),
                new User("test2", "test2-1", 12,"湖北",null),
                new User("test3", "test3-1", 99,"广东",null));
        return Response.success(users);
    }
}
