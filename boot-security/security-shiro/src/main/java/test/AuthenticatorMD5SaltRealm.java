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
        //创建securityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        //设置为自定义realm获取认证数据
        AuthorizingRealm customerRealm = new CustomerMD5SaltRealm();
          //为realm设置为AuthenticationToken提供的凭证认证前提供散列的支持
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        //设置提交的AuthenticationToken的凭据在与存储在系统中的凭据进行比较之前被散列的次数。
        //除非被重写，否则默认值为1，这意味着将发生正常的单个散列执行。
        //如果该参数小于1(即0或负数)，则应用默认值1。必须总是至少有一次哈希迭代(否则将没有哈希)。
        credentialsMatcher.setHashIterations(1024);
        //盐应该总是从存储的帐户信息中获取，而不是根据用户/ subject输入的数据进行解释。
        // 用户输入的数据更容易被攻击者攻击，而从未传播给终端用户的唯一账户(和安全随机生成的)盐几乎不可能被破解。
        // 此方法将在Shiro 2.0中删除。
        /*credentialsMatcher.setHashSalted();*/
        customerRealm.setCredentialsMatcher(credentialsMatcher);
        defaultSecurityManager.setRealm(customerRealm);
        //将安装工具类中设置默认安全管理器
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        //获取主体对象
        Subject subject = SecurityUtils.getSubject();
        //创建token令牌:用于认证的token令牌
        UsernamePasswordToken token = new UsernamePasswordToken("xiaochen", "123");
        try {
            subject.login(token);//用户登录
            System.out.println("登录成功~~");
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("用户名错误!!");
        }catch (IncorrectCredentialsException e){
            e.printStackTrace();
            System.out.println("密码错误!!!");
        }
    }
}