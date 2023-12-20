package web.mvc.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import web.mvc.model.entity.User;
import web.mvc.model.web.response.Response;
import web.mvc.model.web.response.ResponseStatus;

import javax.validation.Valid;
import java.util.List;

/**
 * 校验处理控制器
 * author: pengshuaifeng
 * 2023/12/20
 */
@RestController
@RequestMapping("/mvc/validate")
public class ValidateController {

    @PostMapping("/user/save")
    public Response userSave(@Valid @RequestBody User user, BindingResult bindingResult){
        //获取校验错误集合，为空则没有错误
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            StringBuilder errorBuilder = new StringBuilder();
            fieldErrors.forEach(value->{
                errorBuilder.append(value.getDefaultMessage()).append("|");
            });
            return Response.custom(ResponseStatus.PARAMS_CHECK_FAILURE,errorBuilder.toString());
        }
        System.out.println(user);
        return Response.success();
    }
}
