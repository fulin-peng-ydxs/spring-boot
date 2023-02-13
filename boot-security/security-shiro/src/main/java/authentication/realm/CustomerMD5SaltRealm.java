package authentication.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

public class CustomerMD5SaltRealm extends AuthorizingRealm {

    //授权方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    //认证方法
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        String principal = (String) token.getPrincipal();
        if ("xiaochen".equals(principal)) {
            String password = "3c88b338102c1a343bcb88cd3878758e";  //此为加密密文
            //随机盐，进行MD5加密时，会默认将其盐插入到明文的后面，以提高密码在数据库中的主动安全性
            String salt = "Q4F%";
            return new SimpleAuthenticationInfo(principal, password,
                    ByteSource.Util.bytes(salt), this.getName());
        }
        return null;
    }
}