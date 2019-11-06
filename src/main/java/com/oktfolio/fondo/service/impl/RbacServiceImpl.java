package com.oktfolio.fondo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oktfolio.fondo.model.TokenUser;
import com.oktfolio.fondo.service.RbacService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author oktfolio oktfolio@gmail.com
 * @date 2019/09/05
 */
@Service("rbacService")
public class RbacServiceImpl implements RbacService {

    private static final Logger LOG = LoggerFactory.getLogger(RbacServiceImpl.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    private static final String TOKEN_PRE = "token:";

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication)
            throws UsernameNotFoundException {
        LOG.info("验证 RBAC 权限");
        Object principal = authentication.getPrincipal();

        boolean hasPermission = false;

        if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            return true;
        }

        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();

        List<String> resources = new ArrayList<>();

        LOG.info("request UIR：{}, request Method:{}", requestURI, requestMethod);

        if (principal instanceof String) {

            if (!"anonymousUser".equals(principal)) {

                String v = stringRedisTemplate.opsForValue().get(TOKEN_PRE + principal);

                TokenUser user;

                try {
                    user = objectMapper.readValue(v, TokenUser.class);
                    resources = user.getPermissions();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (!resources.isEmpty()) {
                for (String resource : resources) {
                    String[] split = resource.split(":");

                    boolean match = match(split, requestMethod, requestURI);

                    if (match) {
                        hasPermission = true;
                        break;
                    }
                }
            }
        } else if (principal instanceof User) {
            User user = (User) principal;
            user.getAuthorities();

            for (GrantedAuthority ga : user.getAuthorities()) {

                String[] split = ga.getAuthority().split(":");

                boolean match = match(split, requestMethod, requestURI);

                if (match) {
                    hasPermission = true;
                    break;
                }
            }
        }

        LOG.info("hasPermission: {}", hasPermission);

        return hasPermission;
    }

    private boolean match(String[] split, String requestMethod, String requestURI) {

        LOG.info("split: {}", split[0]);
        LOG.info("split: {}", split[1]);

        if (split.length <= 1) {
            return false;
        }

        String method = split[0];
        String uri = split[1];

        boolean doesMethodMatch;
        boolean doesAntPathMatch;

        doesMethodMatch = ("*".equals(method) || method.equals(requestMethod));
        doesAntPathMatch = (antPathMatcher.match(uri, requestURI));
        return doesAntPathMatch && doesMethodMatch;
    }

}
