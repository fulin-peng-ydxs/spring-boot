package test;

import authentication.realm.CustomerRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;

public class TestAuthenticator {
    public static void main(String[] args) {
        //����securityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
         //����Ĭ���û�����Դ
//        defaultSecurityManager.setRealm(new IniRealm("classpath:shiro.ini"));
        defaultSecurityManager.setRealm(new CustomerRealm());
        //����Ĭ�ϰ�ȫ������
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        //��ȡ�������
        Subject subject = SecurityUtils.getSubject();
        //����token����:������֤��token����
        UsernamePasswordToken token = new UsernamePasswordToken("xiaochen", "123");
        try {
            //��֤token����
            subject.login(token);
            System.out.println("��¼�ɹ�~~");
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("�û�������!!");
        }catch (IncorrectCredentialsException e){
            e.printStackTrace();
            System.out.println("�������!!!");
        }
        //��֤ͨ��
        if(subject.isAuthenticated()){
            //���ڽ�ɫȨ�޹���
            boolean admin = subject.hasRole("admin");
            System.out.println(admin);
            boolean permitted = subject.isPermitted("product:create:001");
            System.out.println(permitted);
        }
    }
}