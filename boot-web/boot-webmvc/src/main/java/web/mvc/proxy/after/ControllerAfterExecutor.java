package web.mvc.proxy.after;


import org.aspectj.lang.JoinPoint;

/**
 * 控制器后置处理器
 *
 * @author pengshuaifeng
 * 2024/1/20
 */
public class ControllerAfterExecutor {

    /**
     * 后置处理
     * 2024/1/20 21:01
     * @author pengshuaifeng
     */
    public Object after(JoinPoint joinPoint, Object result){
        return execute(joinPoint,result);
    }

    /**
     * 后置处理执行
     * 2024/1/21 15:03
     * @author pengshuaifeng
     */
    protected Object execute(JoinPoint joinPoint, Object result){ return null;}


}
