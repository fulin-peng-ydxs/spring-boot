package test;

import authentication.realm.CustomerMD5SaltRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.Subject;

public class AuthenticatorMD5SaltRealm {

    public static void main(String[] args) {
        //����securityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        //����Ϊ�Զ���realm��ȡ��֤����
        AuthorizingRealm customerRealm = new CustomerMD5SaltRealm();
          //Ϊrealm����ΪAuthenticationToken�ṩ��ƾ֤��֤ǰ�ṩɢ�е�֧��
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        //�����ύ��AuthenticationToken��ƾ������洢��ϵͳ�е�ƾ�ݽ��бȽ�֮ǰ��ɢ�еĴ�����
        //���Ǳ���д������Ĭ��ֵΪ1������ζ�Ž����������ĵ���ɢ��ִ�С�
        //����ò���С��1(��0����)����Ӧ��Ĭ��ֵ1����������������һ�ι�ϣ����(����û�й�ϣ)��
        credentialsMatcher.setHashIterations(1024);
        //��Ӧ�����ǴӴ洢���ʻ���Ϣ�л�ȡ�������Ǹ����û�/ subject��������ݽ��н��͡�
        // �û���������ݸ����ױ������߹���������δ�������ն��û���Ψһ�˻�(�Ͱ�ȫ������ɵ�)�μ��������ܱ��ƽ⡣
        // �˷�������Shiro 2.0��ɾ����
        /*credentialsMatcher.setHashSalted();*/
        customerRealm.setCredentialsMatcher(credentialsMatcher);
        defaultSecurityManager.setRealm(customerRealm);
        //����װ������������Ĭ�ϰ�ȫ������
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        //��ȡ�������
        Subject subject = SecurityUtils.getSubject();
        //����token����:������֤��token����
        UsernamePasswordToken token = new UsernamePasswordToken("xiaochen", "123");
        try {
            subject.login(token);//�û���¼
            System.out.println("��¼�ɹ�~~");
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("�û�������!!");
        }catch (IncorrectCredentialsException e){
            e.printStackTrace();
            System.out.println("�������!!!");
        }
    }
}