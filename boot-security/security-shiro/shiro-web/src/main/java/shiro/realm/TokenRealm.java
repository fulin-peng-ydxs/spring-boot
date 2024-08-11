package shiro.realm;


import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import shiro.entity.Perms;
import shiro.entity.User;
import shiro.entity.shiro.AuthToken;
import shiro.service.UserService;
import shiro.utils.ApplicationContextUtils;

import java.util.List;

/**
 * token认证授权器
 * 2024/8/4 19:34
 * @author pengshuaifeng
 */
@Component
@Slf4j
public class TokenRealm extends AuthorizingRealm {


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof AuthToken;
    }

    public TokenRealm(){
        super.setAuthorizationCacheName("auth-ri");
        super.setAuthenticationCacheName("auth-ti");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取身份信息：主凭证
        String primaryPrincipal = (String) principalCollection.getPrimaryPrincipal();
        log.debug("调用授权验证: "+primaryPrincipal);
        //根据主身份信息获取角色 和 权限信息
        UserService userService = (UserService) ApplicationContextUtils
                .getBean("userService");
        User user = userService.findRolesByUserName(primaryPrincipal);
        //授权角色信息
        if(!CollectionUtils.isEmpty(user.getRoles())){
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            user.getRoles().forEach(role->{
                simpleAuthorizationInfo.addRole(role.getName());
                //权限信息
                List<Perms> perms = userService.findPermsByRoleId(role.getId());
                if(!CollectionUtils.isEmpty(perms)){
                    perms.forEach(perm->{
                        simpleAuthorizationInfo.addStringPermission(perm.getName());
                    });
                }
            });
            return simpleAuthorizationInfo;
        }
        return null;
    }



    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.debug("调用认证验证: "+authenticationToken);
        //根据身份信息
        String principal = (String) authenticationToken.getPrincipal();
        //在工厂中获取service对象
        UserService userService = (UserService) ApplicationContextUtils.getBean("userService");
        User user = userService.findByUserName(principal);
        if(!ObjectUtils.isEmpty(user)){
            //user.getUsername() 作为主凭证
            return new SimpleAuthenticationInfo(user.getUsername(),user.getUsername(), this.getName());
        }
        return null;
    }

}
