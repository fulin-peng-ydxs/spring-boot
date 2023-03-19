package shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import shiro.entity.Perms;
import shiro.entity.User;
import shiro.salt.MyByteSource;
import shiro.service.UserService;
import shiro.utils.ApplicationContextUtils;
import java.util.List;
//�Զ���realm
public class CustomerRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //��ȡ�����Ϣ
        String primaryPrincipal = (String) principals.getPrimaryPrincipal();
        System.out.println("������Ȩ��֤: "+primaryPrincipal);
        //�����������Ϣ��ȡ��ɫ �� Ȩ����Ϣ
        UserService userService = (UserService) ApplicationContextUtils
                .getBean("userService");
        User user = userService.findRolesByUserName(primaryPrincipal);
        //��Ȩ��ɫ��Ϣ
        if(!CollectionUtils.isEmpty(user.getRoles())){
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            user.getRoles().forEach(role->{
                simpleAuthorizationInfo.addRole(role.getName());
                //Ȩ����Ϣ
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
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("==========================");
        //���������Ϣ
        String principal = (String) token.getPrincipal();
        //�ڹ����л�ȡservice����
        UserService userService = (UserService) ApplicationContextUtils.getBean("userService");
        User user = userService.findByUserName(principal);
        if(!ObjectUtils.isEmpty(user)){
            return new SimpleAuthenticationInfo(user.getUsername(),user.getPassword(),
                    new MyByteSource(user.getSalt()),
                    this.getName());
        }
        return null;
    }
}
