package commons.utils;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;

/**
 * 代理工具类
 * @author Peng fu lin
 * 2021-12-22 11:30
 */
public class ProxyUtils {

    /**获取代理对象
     * 2023/8/6-0:30
     * @author pengfulin
     * @return 返回e对象的代理对象，如果e不是代理对象，则返回e本身
    */
    public static  Object getTarget(Object e) throws Exception {
        if( !isProxy(e)){ //不是代理对象，则返回原对象
            return e;
        }
        if(AopUtils.isJdkDynamicProxy(e)) {
            return getJdkDynamicProxyTargetObject(e);
        } else { //cglib
            return getCglibProxyTargetObject(e);
        }
    }


    /**判断proxy对象是否为代理对象
     * 2023/8/6-0:31
     * @author pengfulin
    */
    public static  boolean isProxy(Object proxy){
        return AopUtils.isAopProxy(proxy);
    }


    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);

        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        return ((AdvisedSupport)advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
    }

    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);

        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        return ((AdvisedSupport)advised.get(aopProxy)).getTargetSource().getTarget();
    }


}