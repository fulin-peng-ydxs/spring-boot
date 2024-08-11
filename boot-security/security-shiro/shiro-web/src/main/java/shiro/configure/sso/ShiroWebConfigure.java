package shiro.configure.sso;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shiro.cache.RedisCacheManager;
import shiro.filter.TokenFilter;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * shiro-web配置
 * 2024/8/4 18:47
 * @author pengshuaifeng
 */
@Configuration
public class ShiroWebConfigure {


    /**
     * 配置shiro-filter
     * 2024/8/4 18:49
     * @author pengshuaifeng
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //设置安全管理器：
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //设置认证、授权处理策略：
        Map<String,String> authStrategy = new LinkedHashMap<>();

        //公共资源(注意:需要先配置公共资源，否则可能会导致被认证拦截器直接拦截)
        authStrategy.put("/favicon.ico","anon");
        authStrategy.put("/**.html","anon");
        //TODO 通过配置文件方式

         //1.web登录方式
        authStrategy.put("/token/login/**","anon");
         //2.sso登录方式
        TokenFilter tokenFilter = new TokenFilter();
        Map<String, Filter> filterMap = new HashMap<>(1);
        filterMap.put("sso",tokenFilter);
        shiroFilterFactoryBean.setFilters(filterMap);
        authStrategy.put("/**", "sso");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(authStrategy);

        //未授权页面（自动跳转）
        shiroFilterFactoryBean.setUnauthorizedUrl("/403.html");
        return shiroFilterFactoryBean;

    }

    /**
     * 配置安全管理器
     * 2024/8/4 19:42
     * @author pengshuaifeng
     */
    @Bean
    public DefaultWebSecurityManager securityManager(List<Realm> authorizingRealms,RedisCacheManager redisCacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置认证&授权处理器
        for (Realm realm : authorizingRealms) {  //开启缓存
            AuthorizingRealm authorizingRealm=(AuthorizingRealm)realm;
            authorizingRealm.setAuthenticationCachingEnabled(true);
            authorizingRealm.setAuthorizationCachingEnabled(true);
        }
        securityManager.setRealms(authorizingRealms);
        //设置缓存管理器
        securityManager.setCacheManager(redisCacheManager);

        //设置SubjectDAO：关闭session存储
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);  //禁用session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);

        return securityManager;
    }

}
