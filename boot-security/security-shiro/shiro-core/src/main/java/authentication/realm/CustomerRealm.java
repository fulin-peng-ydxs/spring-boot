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


    //��Ȩ����
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //principals:����Ӧ��Subject��������������ļ��ϡ�����ֻ�Ǳ�ʶ���Ե�һ����ȫ���
        // �����û������û�id����ᰲȫ������κ��������Ա���Ϊ��Subject�ġ���ʶ�����ԵĶ�����
        String primaryPrincipal = (String) principals.getPrimaryPrincipal();
        System.out.println("primaryPrincipal = " + primaryPrincipal);
        if(primaryPrincipal.equals("xiaochen1")){
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            //���admin��ɫ
            simpleAuthorizationInfo.addRole("admin");
            //������Ȩ��
            simpleAuthorizationInfo.addStringPermission("user:update:*");
            simpleAuthorizationInfo.addStringPermission("product:*:*");
            return simpleAuthorizationInfo;
        }
        return null;
    }
    
    //��֤������
    // ����null������ƾ֤����/�׳��쳣���ᱻ��֤���ж������֤ʧ��
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