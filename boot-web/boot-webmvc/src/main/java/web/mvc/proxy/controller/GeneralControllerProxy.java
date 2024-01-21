package web.mvc.proxy.controller;

import commons.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.mvc.proxy.after.ControllerAfterExecutor;
import web.mvc.proxy.before.ControllerBeforeExecutor;
import java.util.Collections;
import java.util.List;

/**
 * 通用的控制器代理
 * @author fulin peng
 * 2023/8/3 0003 16:45
 */
@Slf4j
@Aspect
@Service
public class GeneralControllerProxy{

    @Autowired
    private List<ControllerAfterExecutor> afterExecutors= Collections.emptyList();

    private List<ControllerBeforeExecutor> beforeExecutors=Collections.emptyList();

    @Autowired
    public void setExecutor(List<ControllerAfterExecutor> afterExecutors,List<ControllerBeforeExecutor> beforeExecutors){
        if (CollectionUtils.isNotEmpty(afterExecutors)) {
            this.afterExecutors=afterExecutors;
        }
        if (CollectionUtils.isNotEmpty(beforeExecutors)) {
            this.beforeExecutors=beforeExecutors;
        }
    }

    @Around(value = "(@within(org.springframework.stereotype.Controller) ||" +
            " @within(org.springframework.web.bind.annotation.RestController))")
    public Object execute(ProceedingJoinPoint point)throws Throwable{
        //前置处理
        Object beforeResult = before(point);
        if(beforeResult!=null)
            return beforeResult;
        //方法调用
        Object result = point.proceed();
        //后置处理
        return after(point,result);
    }


    /**
     * 前置处理
     * @author fulin peng
     * 2023/8/3 0003 17:41
     */
    public Object before(JoinPoint joinPoint) {
        for (ControllerBeforeExecutor beforeExecutor : beforeExecutors) {
            Object before = beforeExecutor.before(joinPoint);
            if (before!=null)
                return before;
        }
        return null;
    }


    /**
     * 后置处理
     * 2024/1/20 21:01
     * @author pengshuaifeng
     */
    public Object after(JoinPoint joinPoint,Object result){
        for (ControllerAfterExecutor afterExecutor : afterExecutors) {
            Object after = afterExecutor.after(joinPoint,result);
            if (after==null) return null;
        }
        return result;
    }
}
