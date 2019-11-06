package com.oktfolio.fondo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.oktfolio.fondo.common.constant.AuthorizationTypeEnum;
import com.oktfolio.fondo.model.LoginResult;
import com.oktfolio.fondo.model.TokenUser;
import com.oktfolio.fondo.properties.FondoSecurityProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author oktfolio oktfolio@gmail.com
 * @date 2019/09/05
 */
@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationSuccessHandler.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FondoSecurityProperties fondoSecurityProperties;

    public Integer saveLoginDays = 7;

    // @Autowired
    // private IpInfoUtil ipInfoUtil;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, IOException {
        LOG.info("onAuthenticationSuccess");
        //用户选择保存登录状态几天
        String saveLogin = request.getParameter("saveLogin");
        boolean saved = false;
        if (StringUtils.isNotBlank(saveLogin) && Boolean.parseBoolean(saveLogin)) {
            saved = true;
            if (!fondoSecurityProperties.useRedis) {
                fondoSecurityProperties.tokenExpireTime = saveLoginDays * 60 * 24;
            }
        }
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) ((UserDetails) authentication.getPrincipal()).getAuthorities();
        List<String> list = new ArrayList<>();
        for (GrantedAuthority g : authorities) {
            list.add(g.getAuthority());
        }
        // ipInfoUtil.getUrl(request);
        // 登陆成功生成token
        String token;
        if (fondoSecurityProperties.useRedis) {
            // redis
            token = UUID.randomUUID().toString();
            TokenUser user = new TokenUser(username, list);
            // 不缓存权限
            if (!fondoSecurityProperties.storePermissions) {
                user.setPermissions(null);
            }
            // 单设备登录 之前的token失效
            if (fondoSecurityProperties.singleDeviceLogin) {
                String oldToken = stringRedisTemplate.opsForValue().get(FondoSecurityProperties.USER_STR + username);
                if (StringUtils.isNotBlank(oldToken)) {
                    stringRedisTemplate.delete(FondoSecurityProperties.TOKEN_PRE + oldToken);
                }
            }
            if (saved) {
                stringRedisTemplate.opsForValue().set(FondoSecurityProperties.USER_STR + username, token, saveLoginDays,
                        TimeUnit.DAYS);
                stringRedisTemplate.opsForValue().set(FondoSecurityProperties.TOKEN_PRE + token,
                        new Gson().toJson(user),
                        // stringRedisTemplate.opsForValue().set("TOKEN_PRE:" + token, new Gson().toJson(list),
                        saveLoginDays, TimeUnit.DAYS);
            } else {
                stringRedisTemplate.opsForValue().set(FondoSecurityProperties.USER_STR + username, token,
                        fondoSecurityProperties.tokenExpireTime,
                        TimeUnit.MINUTES);
                stringRedisTemplate.opsForValue().set(FondoSecurityProperties.TOKEN_PRE + token,
                        new Gson().toJson(user),
                        // stringRedisTemplate.opsForValue().set("TOKEN_PRE:" + token, new Gson().toJson(list),
                        fondoSecurityProperties.tokenExpireTime, TimeUnit.MINUTES);
            }
        } else {
            // 不缓存权限
            if (!fondoSecurityProperties.storePermissions) {
                list = null;
            }
            // jwt
            token = Jwts.builder()
                    //主题 放入用户名
                    .setSubject(username)
                    //自定义属性 放入用户拥有请求权限
                    .claim("authorities", new Gson().toJson(list))
                    //失效时间
                    .setExpiration(new Date(System.currentTimeMillis() + fondoSecurityProperties.tokenExpireTime * 60 * 1000))
                    //签名算法和密钥
                    .signWith(SignatureAlgorithm.HS512, fondoSecurityProperties.signKey)
                    .compact();
        }

        LoginResult loginResult = new LoginResult();
        loginResult.setAccessToken(token);
        loginResult.setExpiresIn(fondoSecurityProperties.tokenExpireTime * 60 * 1000);
        loginResult.setTokenType(AuthorizationTypeEnum.Bearer.getName());

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(loginResult));

    }
}
