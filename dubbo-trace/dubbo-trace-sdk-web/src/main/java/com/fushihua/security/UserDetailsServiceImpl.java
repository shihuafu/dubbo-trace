package com.fushihua.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fushihua.domain.SysConst;
import com.fushihua.util.XxteaUtils;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private String PASSWORD = XxteaUtils.encrypt("admin", SysConst.USER_PASSWORD_ENCRYPT_KEY);
	
    @Override
    public UserDetails loadUserByUsername(String userId) { //重写loadUserByUsername 方法获得 userdetails 类型用户

    	if (!"admin".equals(userId)) {
    		throw new UsernameNotFoundException("用户不存在");
		}
    	
    	boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		Collection<GrantedAuthority> grantedAuths = obtionGrantedAuthorities(userId);
    	SecurityUser securityUser = new SecurityUser(userId, PASSWORD, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuths);
    	securityUser.setUserId(userId);
    	return securityUser;
    }
    
	//取得用户的权限(即角色)
	private Set<GrantedAuthority> obtionGrantedAuthorities(String userId) {
		Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();
		return authSet;
	}
}