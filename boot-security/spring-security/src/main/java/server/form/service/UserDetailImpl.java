package server.form.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author PengFuLin
 * @description ��֤��Ϣ��ѯ����
 * @date 2022/8/8 22:08
 */
@Service
public class UserDetailImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws
            UsernameNotFoundException {
        // �ж��û����Ƿ����
        if (!"admin".equals(username)) {
            throw new UsernameNotFoundException("�û��������ڣ�");
        }
        // �����ݿ��л�ȡ������ atguigu ������
        String pwd =
                "$2a$10$2R/M6iU3mCZt3ByG7kwYTeeW0w7/UqdeXrb27zkBIizBvAven0/na";
        return new User(username, pwd,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_role,ROLE_admin"));
    }
}
