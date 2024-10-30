package plus.proxy;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import commons.utils.ClassUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * 数据查询权限处理
 *
 * @author fulin-peng
 * 2024-08-29  18:08
 */
@Slf4j
@Aspect
@Component
public class DataSelectAuthorityProxy {

    /**
     * 对com.gzzn.product.prevention.service包下及其子包里的所有的类的以list、get开头或page的方法进行处理
     * 2024/8/29 下午6:24 
     * @author fulin-peng
     */
    @Around(value = "execution(* plus..*.get*(..)) " +
            "|| execution(* plus..*.page(..)) "+"|| execution(* plus..*.list*(..)) ")
    public Object execute(ProceedingJoinPoint point) throws Throwable {
        String methodName = point.getSignature().getName(); //方法名
        log.debug("判断是否设置数据查询权限：{}", methodName);
        Object[] args = point.getArgs();  //方法参数
        boolean hasOperateAnyData = true;
        if(!hasOperateAnyData){  //查询当前用户所属机构数据
            if(args != null){
                for (Object arg : args) {
                    //方法中有QueryWrapper或LambdaQueryWrapper类型的参数，且对应的实体字段有topOrgCode属性
                    if(arg instanceof QueryWrapper || arg instanceof LambdaQueryWrapper){
                        AbstractWrapper wrapper = (AbstractWrapper) arg;
                        Class<?> proxyClass = point.getTarget().getClass();
                        List<Class<?>> paramTypes = ClassUtils.getParamTypes(proxyClass);
                        if(paramTypes != null ){
                            Class<?> entityClass = paramTypes.get(1);
                            if (ClassUtils.hasField("topOrgCode",entityClass)) {
                                Long orgId=1L;
                                wrapper.apply("top_org_code={0}",orgId);
                            }
                        }
                        break;
                    }
                }
            }
        }
        return point.proceed();
    }
}
