package server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author PengFuLin
 * @description security配置类
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
        http.formLogin() //用于配置form表单认证
                .loginPage("/login.html") //登录页面
                .loginProcessingUrl("/login") //表单提交认证请求url
                /*注意此方法会使用forward内部转发处理，注意的是其请求方式导致的问题*/
                .successForwardUrl("/success") //登录成功后url
                .failureForwardUrl("/failure") //登录失败后url
                .usernameParameter("user_name") //登录用户名参数
                .passwordParameter("user_pwd"); //登录用户密码参数

        /*
         * 需要注意，当使用自定义配置类，则默认权限控制会失效，需要手动设置
         */
        //配置所有请求都需要认证
        http.authorizeRequests().antMatchers("/login.html","/error").permitAll()
                .antMatchers("/hasAuthority").hasAuthority("admin")  //需要具有admin权限
                .antMatchers("/hasAnyAuthority").hasAnyAuthority("pfl","bmgl")  //需要具有admin权限或bmgl权限
                .antMatchers("/hasRole").hasRole("role") //需要有指定角色
                .anyRequest().authenticated();  //其他请求均需要认证（如果不设置就默认不需要）

        /*设置权限异常处理*/
        http.exceptionHandling().accessDeniedPage("/denied.html") //设置处理无授权访问时的页面url
                .accessDeniedHandler(null); //设置处理无授权访问时的处理器

        /*设置登出操作*/
        http.logout().logoutUrl("/logout") //设置登出地址
                .logoutSuccessUrl("/logout.html").permitAll(); //设置登出成功跳转地址

        /*关闭Csrf功能*/
//        http.csrf().disable();
    }
}
