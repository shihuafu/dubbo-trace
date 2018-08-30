package com.fushihua.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.fushihua.util.EmptyUtils;

/**
 * @ClassName: SecurityUser 
 * @Description: 登录缓存的信息，尽量少放自定义信息，避免占用资源 
 * @author fushihua
 * @date 2017年3月1日 下午4:11:04
 */
public class SecurityUser extends User {	

	private static final long serialVersionUID = -6423349660894263533L;
	/** user的id */
	private String userId;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public SecurityUser(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
	}

	public static SecurityUser sessionUserDetails() {
		if (SecurityContextHolder.getContext() == null
				|| SecurityContextHolder.getContext().getAuthentication() == null)
			return null;
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (obj == null || !(obj instanceof SecurityUser)) {
			return null;
		}
		SecurityUser securityUser = (SecurityUser) obj;
		return securityUser;
	}
	
	/**
	 * @Description: 获取当前登录人员工号，未登录则为null
	 * @return
	 * @author fushihua
	 * @Time 2017年3月1日 下午2:55:22
	 */
	public static String getLoginUserId() {
		SecurityUser securityUser = SecurityUser.sessionUserDetails();
		if (EmptyUtils.isEmpty(securityUser)) {
			return null;
		}
		return securityUser.getUserId();
	}
}
