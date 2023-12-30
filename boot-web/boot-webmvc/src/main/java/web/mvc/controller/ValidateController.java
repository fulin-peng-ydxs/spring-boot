package web.mvc.controller;

import commons.validator.ValidatorService;
import commons.validator.model.ValidateResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.mvc.model.annotations.proxy.EntityValid;
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
@Api(tags = "业务功能-校验处理演示")
@RestController
@RequestMapping("/mvc/validate")
public class ValidateController {

    @Autowired
    private ValidatorService validatorService;


    @PostMapping("/basic")
    public Response basic(@Valid @RequestBody User user, BindingResult bindingResult){
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


    @PostMapping("/validatorService")
    public Response validatorService(@RequestBody User user){
//        ValidateResult validate = validatorService.validate(user);
//        ValidateResult validate = validatorService.validate(user,true);
        ValidateResult validate = validatorService.validate(user,",",false);
        boolean status = validate.isStatus();
        if(!status){
            System.out.println(validate.getMessage());
            for (ValidateResult.ValidateError error : validate.getErrors()) {
                System.out.println(error);
            }
            return Response.custom(ResponseStatus.PARAMS_CHECK_FAILURE,validate);
        }
        return Response.success();
    }

    @PostMapping("/validatorService/list")
    public Response validatorService(@RequestBody List<User> users){
        List<ValidateResult> validates = validatorService.validates(users);
//        List<ValidateResult> validates = validatorService.validates(users,true);
        if (!validates.isEmpty()) {
            for (ValidateResult validate : validates) {
                System.out.println(validate.getMessage());
                for (ValidateResult.ValidateError error : validate.getErrors()) {
                    System.out.println(error);
                }
            }
            return Response.custom(ResponseStatus.PARAMS_CHECK_FAILURE,validates);
        }
        return Response.success();
    }


    @PostMapping("/validatorService/proxy")
    public Response validatorServiceProxy(@EntityValid @RequestBody User user){
        return Response.success();
    }

    @PostMapping("/validatorService/proxy/list")
    public Response validatorServiceProxyList(@EntityValid @RequestBody  List<User> users){
        return Response.success();
    }

    @PostMapping("/validatorService/proxy/null")
    public Response validatorServiceProxyNull(@EntityValid User user){
        return Response.success();
    }
}
