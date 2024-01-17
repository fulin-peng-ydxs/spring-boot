package web.mvc.controller.postman;


import commons.model.web.response.Response;
import commons.utils.JsonUtils;
import commons.utils.ProxyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 服务方法调用-控制器
 *
 * @author pengshuaifeng
 * 2023/12/27
 */
@Api(tags = "后台控制-服务方法调用")
@Slf4j
@RequestMapping("/postman/service/method")
@RestController
public class MethodCallController {

    @Autowired
    private ApplicationContext context;


    @ApiOperation(value="方法调用",produces = "application/json",
            notes ="serviceName：为所要调用服务名称（必填）\n"
                    +"serviceMethod：为所要调用的服务的具体方法（必填）\n"
                    +"methodParam：为方法的参数值（数组格式，按照前后顺序匹配，没有参数则不用填！如果参数是对象，请用json方式传入)\n"
                    +"methodParamClass：为方法的参数类型（数组格式，按照前后顺序匹配，没有参数则不用填！例如：[String.class]、[String.class,Integer.class,{}]）"
    )
    @RequestMapping(value = { "/call/{proxyType}" }, method = { RequestMethod.POST })
    public Response methodCall(@PathVariable String proxyType, @RequestBody Map<String,Object> paramMap){
        log.debug("进入-服务方法调用控制器：{}/{}",proxyType,paramMap);
        try {
            String serviceName =paramMap.get("serviceName").toString();
            String serviceMethodName = paramMap.get("serviceMethod").toString();
            //获取服务对象
            Object serviceObjet = context.getBean(serviceName);
            if(proxyType.equals("basic")){   // 不用代理对象
                serviceObjet= ProxyUtils.getTarget(serviceObjet);
            }
            Class<?> serviceObjectClass = serviceObjet.getClass();
            //获取服务方法
            Method serviceMethod=null;
            List<String> methodParams = (List<String>)paramMap.get("methodParam");
            Class<?>[] methodParamClassesTypes=null;
            boolean notParamMethod = methodParams == null || methodParams.isEmpty();
            if(notParamMethod){
                serviceMethod=serviceObjectClass.getDeclaredMethod(serviceMethodName);
            }else{
                List<String> methodParamClasses=(List<String>)paramMap.get("methodParamClass");
                methodParamClassesTypes=new Class<?>[methodParamClasses.size()];
                for (int i = 0; i < methodParamClasses.size(); i++) {
                    methodParamClassesTypes[i]=Class.forName(methodParamClasses.get(i));
                }
                serviceMethod=serviceObjectClass.getDeclaredMethod(serviceMethodName,methodParamClassesTypes);
            }
            //调用服务方法
            serviceMethod.setAccessible(true);
            Object result=null;
            if(notParamMethod){
                result=serviceMethod.invoke(serviceObjet);
            }else{
                Object[] methodParamValues=new Object[methodParams.size()];
                for (int i = 0; i < methodParams.size(); i++) {
                    String methodParam = methodParams.get(i);
                    Class<?> methodParamClassesType = methodParamClassesTypes[i];
                    //TODO 仅支持引用数据类型，暂不支持基本数据类型
                    Object methodParamValue;
                    if (methodParamClassesType==String.class) {
                        methodParamValue= methodParam;
                    }else if(methodParamClassesType==Integer.class){
                        methodParamValue= Integer.valueOf(methodParam);
                    }else if(methodParamClassesType==Boolean.class){
                        methodParamValue= Boolean.valueOf(methodParam);
                    }else{
                        if (methodParamClassesType.isEnum()) {
                            methodParamValue= Enum.valueOf((Class<? extends Enum>) methodParamClassesType,methodParam);
                        }else{
                            //TODO 暂不支持Collection集合参数
                            methodParamValue= JsonUtils.getObject(methodParam,methodParamClassesType);
                        }
                    }
                    methodParamValues[i]=methodParamValue;
                }
                result=serviceMethod.invoke(serviceObjet,methodParamValues);
            }
            log.debug("退出-服务方法调用控制器：{}",result);
            return Response.success(result);
        }catch (InvocationTargetException e) {   //服务方法内部异常
            Throwable trowEx = e.getTargetException();
            log.error("服务方法内部异常：",trowEx);
            return exception(e,"服务方法内部异常");
        }catch (Exception e){ //服务方法调用异常
            log.error("服务方法调用异常：",e);
            return exception(e,"服务方法调用异常");
        }
    }

    /**
     * 异常统一处理
     * 2023/12/27 23:48
     * @author pengshuaifeng
     */
    private Response exception(Exception e,String defaultErrorMsg) {
        String errorMeg = defaultErrorMsg;
        if (e instanceof RuntimeException)
            errorMeg = e.getMessage();
        return Response.failure(errorMeg);
    }
}
