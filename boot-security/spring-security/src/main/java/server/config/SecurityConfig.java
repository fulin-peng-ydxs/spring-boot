package server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author PengFuLin
 * @description security������
 * @date 2022/8/8 22:03
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin() //��������form����֤
                .loginPage("/login.html") //��¼ҳ��
                .loginProcessingUrl("/login") //���ύ��֤����url
                /*ע��˷�����ʹ��forward�ڲ�ת������ע�����������ʽ���µ�����*/
                .successForwardUrl("/success") //��¼�ɹ���url
                .failureForwardUrl("/failure") //��¼ʧ�ܺ�url
                .usernameParameter("user_name") //��¼�û�������
                .passwordParameter("user_pwd"); //��¼�û��������

        /*
         * ��Ҫע�⣬��ʹ���Զ��������࣬��Ĭ��Ȩ�޿��ƻ�ʧЧ����Ҫ�ֶ�����
         */
        //��������������Ҫ��֤
        http.authorizeRequests().antMatchers("/login.html","/error").permitAll()
                .antMatchers("/hasAuthority").hasAuthority("admin")  //��Ҫ����adminȨ��
                .antMatchers("/hasAnyAuthority").hasAnyAuthority("pfl","bmgl")  //��Ҫ����adminȨ�޻�bmglȨ��
                .antMatchers("/hasRole").hasRole("role") //��Ҫ��ָ����ɫ
                .anyRequest().authenticated();  //�����������Ҫ��֤����������þ�Ĭ�ϲ���Ҫ��

        /*����Ȩ���쳣����*/
        http.exceptionHandling().accessDeniedPage("/denied.html") //���ô�������Ȩ����ʱ��ҳ��url
                .accessDeniedHandler(null); //���ô�������Ȩ����ʱ�Ĵ�����

        /*���õǳ�����*/
        http.logout().logoutUrl("/logout") //���õǳ���ַ
                .logoutSuccessUrl("/logout.html").permitAll(); //���õǳ��ɹ���ת��ַ

        /*�ر�Csrf����*/
//        http.csrf().disable();
    }
}
