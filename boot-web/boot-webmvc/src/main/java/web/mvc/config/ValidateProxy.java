package web.mvc.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Service;

/**
 * 校验代理
 * @author fulin peng
 * 2023/8/3 0003 16:45
 */
@Slf4j
@Aspect
@Service
public class ValidateProxy {

    /**
     * Controller层的参数校验（类有@controller注解）
     * @author fulin peng
     * 2023/8/3 0003 17:41
     */
    @Before("(@within(org.springframework.stereotype.Controller) " +
            "|| @within(org.springframework.web.bind.annotation.RestController))")
    public void beforeValidAnnotation(JoinPoint joinPoint) {
        log.info("进行Controller的参数校验：{}",joinPoint.getSignature().toLongString());
    }

}
