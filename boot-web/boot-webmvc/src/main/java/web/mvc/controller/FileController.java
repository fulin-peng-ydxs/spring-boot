package web.mvc.controller;


import commons.holder.ServletHolder;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
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
    public void download(@RequestParam String classPath){
        InputStream inputStream = FileController.class.getResourceAsStream(classPath);
        String fileName = classPath.substring(classPath.lastIndexOf("/") + 1);
        ServletHolder.responseToOutStream(null,inputStream,0,fileName,null);
    }
}
