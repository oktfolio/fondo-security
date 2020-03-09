package com.oktfolio.fondo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.oktfolio.fondo.common.constant.AuthorizationTypeEnum;
import com.oktfolio.fondo.common.constant.Header;
import com.oktfolio.fondo.common.result.ResultEntity;
import com.oktfolio.fondo.common.result.UserResultCodeEnum;
import com.oktfolio.fondo.exception.InvalidLoginStatusException;
import com.oktfolio.fondo.exception.InvalidTokenException;
import com.oktfolio.fondo.model.ResourcesLiteBo;
import com.oktfolio.fondo.model.TokenUser;
import com.oktfolio.fondo.properties.FondoSecurityProperties;
import com.oktfolio.fondo.service.ResourceService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author oktfolio oktfolio@gmail.com
 * @date 2019/09/05
 */
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    private final static Logger LOG = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    private ResourceService resourceService;

    private FondoSecurityProperties fondoSecurityProperties;

    private StringRedisTemplate stringRedisTemplate;

    private ObjectMapper objectMapper;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                   ResourceService resourceService,
                                   FondoSecurityProperties fondoSecurityProperties,
                                   StringRedisTemplate stringRedisTemplate,
                                   ObjectMapper objectMapper) {
        super(authenticationManager);
        this.resourceService = resourceService;
        this.fondoSecurityProperties = fondoSecurityProperties;
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException, InvalidTokenException {
        LOG.info("doFilterInternal Authorization");
        String authorizationHeader = request.getHeader(Header.AUTHORIZATION);
        LOG.info("Authorization header:{}", authorizationHeader);

        // 若在请求头中没拿到 authorization，则到 URL 中取
        if (StringUtils.isBlank(authorizationHeader)) {
            authorizationHeader = request.getParameter(Header.AUTHORIZATION);
        }

        LOG.info("useRedis:{}", fondoSecurityProperties.useRedis);


        boolean invalid = !StringUtils.isNotBlank(authorizationHeader) || (!fondoSecurityProperties.useRedis
                && !authorizationHeader.startsWith(AuthorizationTypeEnum.Bearer.getValue()));

        if (!invalid) {
            try {
                String jwtToken = authorizationHeader.replace(AuthorizationTypeEnum.Bearer.getValue(), "");
                UsernamePasswordAuthenticationToken authentication = getAuthentication(jwtToken, response);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (InvalidLoginStatusException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.getWriter().write(
                        objectMapper.writeValueAsString(
                                ResultEntity.unauthorized(UserResultCodeEnum.INVALID_LOGIN_STATUS)));
                return;
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.getWriter().write(
                        objectMapper.writeValueAsString(
                                ResultEntity.unauthorized(UserResultCodeEnum.INVALID_TOKEN)));
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token,
                                                                  HttpServletResponse response)
            throws IOException, InvalidLoginStatusException {
        // 用户名
        String username = "";
        // 权限
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (fondoSecurityProperties.useRedis) {
            LOG.info("header: {}", token);
            String jwtToken = stringRedisTemplate.opsForValue().get(FondoSecurityProperties.TOKEN_PRE + token);

            LOG.info("authorities: {}", jwtToken);

            if (jwtToken == null || StringUtils.isBlank(jwtToken)) {
                throw new InvalidLoginStatusException();
            }
            TokenUser user = new Gson().fromJson(jwtToken, TokenUser.class);
            username = user.getUsername();
            if (fondoSecurityProperties.storePermissions) {
                // 缓存了权限
                for (String ga : user.getPermissions()) {
                    authorities.add(new SimpleGrantedAuthority(ga));
                }
            } else {
                // 未缓存 读取权限数据
                authorities = getCurrentUserPermissions(username);
            }
            // if (!user.getSaveLogin()) {
            // 若未保存登录状态重新设置失效时间
            stringRedisTemplate.opsForValue().set(FondoSecurityProperties.USER_STR + username, jwtToken, 30,
                    TimeUnit.MINUTES);
            stringRedisTemplate.opsForValue().set(FondoSecurityProperties.TOKEN_PRE + token, jwtToken, 30,
                    TimeUnit.MINUTES);
            // }
        } else {
            // JWT
            try {
                // 解析token
                Claims claims = Jwts.parser()
                        .setSigningKey(fondoSecurityProperties.signKey)
                        .parseClaimsJws(token)
                        .getBody();

                // 获取用户名
                username = claims.getSubject();
                // 获取权限

//                if (fondoSecurityProperties.storePermissions) {
//                    // 缓存了权限
//                    String authority = claims.get("authorities").toString();
//                    if (StringUtils.isNotBlank(authority)) {
//                        List<String> list = new Gson().fromJson(authority, new TypeToken<List<String>>() {
//                        }.getType());
//                        for (String ga : list) {
//                            authorities.add(new SimpleGrantedAuthority(ga));
//                        }
//                    }
//                } else {
                // 未缓存 读取权限数据
                authorities = getCurrentUserPermissions(username);
//                }
            } catch (ExpiredJwtException e) {
                throw e;
            } catch (Exception e) {
                throw new InvalidTokenException();
            }
        }

        if (StringUtils.isNotBlank(username)) {
            User principal = new User(username, "", authorities);
            return new UsernamePasswordAuthenticationToken(principal, null, authorities);
        }

        return null;
    }

    /**
     * 通过用户名获取用户拥有权限
     *
     * @param username 用户名
     */
    public List<GrantedAuthority> getCurrentUserPermissions(String username) {

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (ResourcesLiteBo resource : resourceService.findEnabledResourcesLiteBoListByUsername(username)) {
            authorities.add(new SimpleGrantedAuthority(resource.getMethod() + ":" + resource.getUri()));
        }
        return authorities;
    }
}
