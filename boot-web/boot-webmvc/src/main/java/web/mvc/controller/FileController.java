package web.mvc.controller;


import commons.web.ServletHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.InputStream;

/**
 * 文件处理控制器
 *
 * @author pengshuaifeng
 * 2023/12/30
 */
@Api(tags = "业务功能-文件处理演示")
@RestController
@RequestMapping("/mvc/multipart")
public class FileController {

    @GetMapping("/download")
    @ApiOperation(value = "文件下载",notes = "服务器路径的文件下载")
    public void download(@ApiParam("类路径") @RequestParam String classPath){
        InputStream inputStream = FileController.class.getResourceAsStream(classPath);
        String fileName = classPath.substring(classPath.lastIndexOf("/") + 1);
        ServletHolder.responseToOutStream(null,inputStream,0,fileName,null,false);
    }
}
