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
 * shiro-web����
 * 2024/8/4 18:47
 * @author pengshuaifeng
 */
@Configuration
public class ShiroWebConfigure {


    /**
     * ����shiro-filter
     * 2024/8/4 18:49
     * @author pengshuaifeng
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //���ð�ȫ��������
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //������֤����Ȩ������ԣ�
        Map<String,String> authStrategy = new LinkedHashMap<>();

        //������Դ(ע��:��Ҫ�����ù�����Դ��������ܻᵼ�±���֤������ֱ������)
        authStrategy.put("/favicon.ico","anon");
        authStrategy.put("/**.html","anon");
        //TODO ͨ�������ļ���ʽ

         //1.web��¼��ʽ
        authStrategy.put("/token/login/**","anon");
         //2.sso��¼��ʽ
        TokenFilter tokenFilter = new TokenFilter();
        Map<String, Filter> filterMap = new HashMap<>(1);
        filterMap.put("sso",tokenFilter);
        shiroFilterFactoryBean.setFilters(filterMap);
        authStrategy.put("/**", "sso");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(authStrategy);

        //δ��Ȩҳ�棨�Զ���ת��
        shiroFilterFactoryBean.setUnauthorizedUrl("/403.html");
        return shiroFilterFactoryBean;

    }

    /**
     * ���ð�ȫ������
     * 2024/8/4 19:42
     * @author pengshuaifeng
     */
    @Bean
    public DefaultWebSecurityManager securityManager(List<Realm> authorizingRealms,RedisCacheManager redisCacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //������֤&��Ȩ������
        for (Realm realm : authorizingRealms) {  //��������
            AuthorizingRealm authorizingRealm=(AuthorizingRealm)realm;
            authorizingRealm.setAuthenticationCachingEnabled(true);
            authorizingRealm.setAuthorizationCachingEnabled(true);
        }
        securityManager.setRealms(authorizingRealms);
        //���û��������
        securityManager.setCacheManager(redisCacheManager);

        //����SubjectDAO���ر�session�洢
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);  //����session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);

        return securityManager;
    }

}
