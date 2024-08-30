package web.mvc.proxy;

import commons.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import web.mvc.proxy.after.ProxyAfterExecutor;
import web.mvc.proxy.before.ProxyBeforeExecutor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 控制器代理
 * @author fulin peng
 * 2023/8/3 0003 16:45
 */
@Slf4j
@Aspect
@Service
@ConditionalOnProperty(name = "web.custom.proxy.controller.enable",havingValue = "true",matchIfMissing = true)
public class ControllerProxy {

    private Collection<ProxyAfterExecutor> afterExecutors= Collections.emptyList();

    private Collection<ProxyBeforeExecutor> beforeExecutors=Collections.emptyList();

    @Autowired
    public void setExecutor(ObjectProvider<Collection<ProxyAfterExecutor>> afterExecutors,ObjectProvider<Collection<ProxyBeforeExecutor>> beforeExecutors){
        this.afterExecutors=CollectionUtils.isEmptyOrDefault(afterExecutors.getIfAvailable());
        this.beforeExecutors=CollectionUtils.isEmptyOrDefault(beforeExecutors.getIfAvailable());
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
        for (ProxyBeforeExecutor beforeExecutor : beforeExecutors) {
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
        for (ProxyAfterExecutor afterExecutor : afterExecutors) {
            Object after = afterExecutor.after(joinPoint,result);
            if (after==null) return null;
        }
        return result;
    }
}
