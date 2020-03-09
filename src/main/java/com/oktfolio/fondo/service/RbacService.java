package com.oktfolio.fondo.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author oktfolio oktfolio@gmail.com
 * @date 2019/09/05
 */
public interface RbacService {
    boolean hasPermission(HttpServletRequest request, Authentication authentication) throws UsernameNotFoundException;
}
