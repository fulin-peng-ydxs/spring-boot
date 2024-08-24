package web.mvc.proxy;

import commons.model.annotations.log.LogSave;
import commons.model.entity.log.SysLog;
import commons.model.enums.GeneralOperateStatus;
import commons.utils.ClassUtils;
import commons.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志代理
 *
 * @author pengshuaifeng
 * 2024/8/24
 */
@Slf4j
@Aspect
@Service
@ConditionalOnProperty(name = "web.custom.proxy.log.enable",havingValue = "true")
public class LogProxy {

    /**
     * 日志处理
     * 2024/8/24 13:39
     * @author pengshuaifeng
     */
    @Around(value = "@annotation(commons.model.annotations.log.LogSave)")
    public Object execute(ProceedingJoinPoint point)throws Throwable{
        SysLog sysLog = new SysLog();
        String eventDescription="";
        try {
            MethodSignature methodSignature = (MethodSignature) point.getSignature();
            Method method = methodSignature.getMethod();
            Object[] args = point.getArgs();
            LogSave logSave = method.getAnnotation(LogSave.class);
            sysLog.setEvent(logSave.logType());
            Parameter[] parameters = method.getParameters();
            String logDescriptionTemplate = logSave.logDescriptionTemplate();
            List<String> resVars = null;
            List<String> resVarTemplates = null;
            if(parameters != null && parameters.length > 0 ){
                List<String> vars = StringUtils.regexExtract(logDescriptionTemplate, "\\{(.*?)\\}",1);
                List<String> varTemplates = StringUtils.regexExtract(logDescriptionTemplate, "\\{(.*?)\\}");
                Object arg = args[logSave.logObjectParamIndex()];
                //处理对象参数
                if(!vars.isEmpty() && arg!=null){  //处理{}
                    Class<?> aClass = arg.getClass();
                    for (int i = 0; i < vars.size(); i++) {
                        String var = vars.get(i);
                        if (var.startsWith("res.")) {
                            if(resVars==null)
                                resVars = new ArrayList<>();
                            if(resVarTemplates==null)
                                resVarTemplates = new ArrayList<>();
                            resVars.add(var);
                            resVarTemplates.add(varTemplates.get(i));
                            continue;
                        }
                        Object fieldValue = ClassUtils.getFieldValueWithMultistage(var, arg);
                        logDescriptionTemplate= logDescriptionTemplate.replace(varTemplates.get(i),
                                fieldValue==null?"null":fieldValue.toString());
                    }
                    eventDescription=logDescriptionTemplate;
                }
                if(logDescriptionTemplate.contains("%s")){ //处理%s
                    String[] paramIndex = logSave.logParamIndex().split(",");
                    List<Object> params = new ArrayList<>(paramIndex.length);
                    for (String index : paramIndex) {
                        params.add(args[Integer.parseInt(index)]);
                    }
                    eventDescription=String.format(logDescriptionTemplate, params);
                }
            }
            //方法调用
            Object proceed = point.proceed();
            //处理返回值
            if(resVars!=null){
                for (int i = 0; i < resVars.size(); i++) {
                    String resVar = resVars.get(i);
                    String returnField = resVar.replace("res.", "");
                    Object returnDataFieldValue = ClassUtils.getFieldValueWithMultistage(returnField, proceed);
                    eventDescription = eventDescription.replace(resVarTemplates.get(i), returnDataFieldValue==null?"null"
                            :returnDataFieldValue.toString());
                }
            }
            String statusField = logSave.logObjectReturnStatusField();
            if (StringUtils.isNotEmpty(statusField)) {
                Object returnDataFieldValue = ClassUtils.getFieldValue(statusField, proceed);
                sysLog.setEventStatus(returnDataFieldValue.toString());
            } else {
                sysLog.setEventStatus(logSave.logObjectReturnStatusValue());
            }
            return proceed;
        } catch (Throwable e) {
            sysLog.setEventStatus(GeneralOperateStatus.FAILURE);
            throw e;
        }finally {
            sysLog.setEventDescription(eventDescription);
            addLog(sysLog);
        }
    }

    /**
     * 保存日志
     * 2024/8/24 13:54
     * @author pengshuaifeng
     */
    //TODO 保存日志由具体业务实现
    public void addLog(SysLog sysLog) {
        //保存日志
        System.out.println(sysLog);
    }

}
