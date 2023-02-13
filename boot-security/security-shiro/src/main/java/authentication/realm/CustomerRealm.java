package authentication.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class CustomerRealm extends AuthorizingRealm {


    //授权方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //principals:与相应的Subject关联的所有主体的集合。主体只是标识属性的一个安全术语，
        // 例如用户名或用户id或社会安全号码或任何其他可以被认为是Subject的“标识”属性的东西。
        String primaryPrincipal = (String) principals.getPrimaryPrincipal();
        System.out.println("primaryPrincipal = " + primaryPrincipal);
        if(primaryPrincipal.equals("xiaochen1")){
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            //添加admin角色
            simpleAuthorizationInfo.addRole("admin");
            //添加相关权限
            simpleAuthorizationInfo.addStringPermission("user:update:*");
            simpleAuthorizationInfo.addStringPermission("product:*:*");
            return simpleAuthorizationInfo;
        }
        return null;
    }
    
    //认证方法：
    // 返回null（或者凭证错误）/抛出异常即会被认证器判断身份认证失败
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        String principal = (String) token.getPrincipal();
        if("xiaochen".equals(principal)){
            return new SimpleAuthenticationInfo(principal,"123",this.getName());
        }
        return null;
    }
}