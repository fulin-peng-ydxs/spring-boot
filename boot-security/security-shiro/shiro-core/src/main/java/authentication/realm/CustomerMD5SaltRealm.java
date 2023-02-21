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

    //��Ȩ����
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    //��֤����
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        String principal = (String) token.getPrincipal();
        if ("xiaochen".equals(principal)) {
            String password = "3c88b338102c1a343bcb88cd3878758e";  //��Ϊ��������
            //����Σ�����MD5����ʱ����Ĭ�Ͻ����β��뵽���ĵĺ��棬��������������ݿ��е�������ȫ��
            String salt = "Q4F%";
            return new SimpleAuthenticationInfo(principal, password,
                    ByteSource.Util.bytes(salt), this.getName());
        }
        return null;
    }
}