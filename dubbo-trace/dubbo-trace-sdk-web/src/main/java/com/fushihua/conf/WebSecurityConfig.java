package com.fushihua.conf;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fushihua.domain.SysConst;
import com.fushihua.security.MyAuthenticationSuccessHandler;
import com.fushihua.security.MyDaoAuthenticationProvider;
import com.fushihua.security.MyLogoutHandler;
import com.fushihua.util.XxteaUtils;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private SessionRegistry sessionRegistry;
    
    @Autowired
    private MyAuthenticationSuccessHandler authenticationSuccessHandler;
    
    @Autowired
    private MyLogoutHandler logoutHandler;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/login").permitAll()
            .antMatchers("/logout").permitAll()
            .antMatchers("/ops/**").permitAll()
            .antMatchers("/**/*.jpg").permitAll()
            .antMatchers("/**/*.png").permitAll()
            .antMatchers("/**/*.js").permitAll()
            .antMatchers("/**/*.css").permitAll()
            .antMatchers("/favicon.ico").permitAll()
            .antMatchers("/error").permitAll()
            .antMatchers("/").permitAll()
            .anyRequest().authenticated()
            .and()
            .httpBasic()
            .and()
            .headers().frameOptions().disable()
            .and().formLogin().permitAll().successHandler(authenticationSuccessHandler)
            .and().logout().permitAll().addLogoutHandler(logoutHandler)
            .and().sessionManagement().sessionFixation().changeSessionId()  
            	.maximumSessions(1).sessionRegistry(sessionRegistry)
            ; 
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public SessionRegistry getSessionRegistry(){
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }
    
    @Bean
    public MyDaoAuthenticationProvider authenticationProvider() {
    	MyDaoAuthenticationProvider provider = new MyDaoAuthenticationProvider();
        provider.setHideUserNotFoundExceptions(false);
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return XxteaUtils.encrypt((String) rawPassword, SysConst.USER_PASSWORD_ENCRYPT_KEY);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encodedPassword.equals(XxteaUtils.encrypt((String) rawPassword, SysConst.USER_PASSWORD_ENCRYPT_KEY));
            }
        });
        return provider;
    }
}