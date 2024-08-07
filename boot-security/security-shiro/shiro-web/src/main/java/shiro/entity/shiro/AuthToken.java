package shiro.entity.shiro;


import lombok.AllArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * AuthenticationToken实现
 *
 * @author pengshuaifeng
 * 2024/8/4
 */
@AllArgsConstructor
public class AuthToken implements AuthenticationToken {

    private Object principal; //身份标识

    private Object credentials; //身份凭证

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }
}
