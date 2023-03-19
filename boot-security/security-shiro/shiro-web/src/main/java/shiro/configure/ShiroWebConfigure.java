package shiro.configure;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shiro.cache.RedisCacheManager;
import shiro.realm.CustomerRealm;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author PengFuLin
 * @description shiro配置器
 * @date 2022/8/13 12:39
 */
@Configuration
public class ShiroWebConfigure {

    //配置shiroFilter：通过filter实现认证授权
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager){
        //创建shiro的filter
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //注入安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //配置认证&授权
        Map<String,String> map = new LinkedHashMap<>();
        //公共资源 anon 放行对应资源 (注意，需要先配置公共资源，否则可能会导致被认证拦截器直接拦截)
        map.put("/user/login","anon");
        map.put("/user/register","anon");
        map.put("/register.html","anon");
        map.put("/user/getImage","anon");
        map.put("/favicon.ico","anon");
        //authc 认证处理
        map.put("/**","authc");
        //设置登录路径
        shiroFilterFactoryBean.setLoginUrl("/login.html");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        return shiroFilterFactoryBean; //spring会调用getObject获取真正的filter

    }

    //配置DefaultWebSecurityManager（继承了SessionsSecurityManager）(注意不能
    // 返回WebSecurityManager，会导致应shiro自动配置出现缺少bean而启动失败)：通过realm进行委托授认证授权
    @Bean
    public DefaultWebSecurityManager getWebSecurityManager(Realm realm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(realm);
        return defaultWebSecurityManager;
    }

    //配置realm：最终处理证和授权
    @Bean
    public Realm getRealm(){
        CustomerRealm customerRealm = new CustomerRealm();
        //修改凭证校验匹配器
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //设置加密算法为md5
        credentialsMatcher.setHashAlgorithmName("MD5");
        //设置散列次数
        credentialsMatcher.setHashIterations(1024);
        customerRealm.setCredentialsMatcher(credentialsMatcher);
        //开启缓存管理
        customerRealm.setCacheManager(new RedisCacheManager());  //设置缓存管理
        customerRealm.setCachingEnabled(true);//开启全局缓存
        //认证缓存配置
        customerRealm.setAuthenticationCachingEnabled(true);
        customerRealm.setAuthenticationCacheName("authenticationCache");
        //授权缓存配置
        customerRealm.setAuthorizationCachingEnabled(true);
        customerRealm.setAuthorizationCacheName("authorizationCache");

        return customerRealm;
    }
}
