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
 * @description shiro������
 * @date 2022/8/13 12:39
 */
@Configuration
public class ShiroWebConfigure {

    //����shiroFilter��ͨ��filterʵ����֤��Ȩ
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager){
        //����shiro��filter
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //ע�밲ȫ������
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //������֤&��Ȩ
        Map<String,String> map = new LinkedHashMap<>();
        //������Դ anon ���ж�Ӧ��Դ (ע�⣬��Ҫ�����ù�����Դ��������ܻᵼ�±���֤������ֱ������)
        map.put("/user/login","anon");
        map.put("/user/register","anon");
        map.put("/register.html","anon");
        map.put("/user/getImage","anon");
        map.put("/favicon.ico","anon");
        //authc ��֤����
        map.put("/**","authc");
        //���õ�¼·��
        shiroFilterFactoryBean.setLoginUrl("/login.html");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        return shiroFilterFactoryBean; //spring�����getObject��ȡ������filter

    }

    //����DefaultWebSecurityManager���̳���SessionsSecurityManager��(ע�ⲻ��
    // ����WebSecurityManager���ᵼ��Ӧshiro�Զ����ó���ȱ��bean������ʧ��)��ͨ��realm����ί������֤��Ȩ
    @Bean
    public DefaultWebSecurityManager getWebSecurityManager(Realm realm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(realm);
        return defaultWebSecurityManager;
    }

    //����realm�����մ���֤����Ȩ
    @Bean
    public Realm getRealm(){
        CustomerRealm customerRealm = new CustomerRealm();
        //�޸�ƾ֤У��ƥ����
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //���ü����㷨Ϊmd5
        credentialsMatcher.setHashAlgorithmName("MD5");
        //����ɢ�д���
        credentialsMatcher.setHashIterations(1024);
        customerRealm.setCredentialsMatcher(credentialsMatcher);
        //�����������
        customerRealm.setCacheManager(new RedisCacheManager());  //���û������
        customerRealm.setCachingEnabled(true);//����ȫ�ֻ���
        //��֤��������
        customerRealm.setAuthenticationCachingEnabled(true);
        customerRealm.setAuthenticationCacheName("authenticationCache");
        //��Ȩ��������
        customerRealm.setAuthorizationCachingEnabled(true);
        customerRealm.setAuthorizationCacheName("authorizationCache");

        return customerRealm;
    }
}
