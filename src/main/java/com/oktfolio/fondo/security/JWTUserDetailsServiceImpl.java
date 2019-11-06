package com.oktfolio.fondo.security;

import com.oktfolio.fondo.common.result.UserResultCodeEnum;
import com.oktfolio.fondo.model.ResourcesLiteBo;
import com.oktfolio.fondo.model.SysUser;
import com.oktfolio.fondo.service.ResourceService;
import com.oktfolio.fondo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author oktfolio oktfolio@gmail.com
 * @date 2019/09/05
 */
@Component
public class JWTUserDetailsServiceImpl implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(JWTUserDetailsServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ResourceService resourceService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        LOG.info("loadUserByUsername");

        SysUser user = userService.findByUsername(s);

        if (user == null) {
            throw new UsernameNotFoundException(UserResultCodeEnum.USER_NOT_EXIST.message());
        }

        JWTUserDetails jwtUserDetails = new JWTUserDetails();
        jwtUserDetails.setUsername(user.getUsername());
        jwtUserDetails.setPassword(user.getPassword());
        jwtUserDetails.setStatus(user.getStatus());

        List<ResourcesLiteBo> resourceList = resourceService.findEnabledResourcesLiteBoListByUsername(s);

        jwtUserDetails.setResources(resourceList);
        return jwtUserDetails;
    }
}
