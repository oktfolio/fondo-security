package com.oktfolio.fondo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author oktfolio oktfolio@gmail.com
 * @date 2019/09/05
 */
public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger LOG = LoggerFactory.getLogger(JsonUsernamePasswordAuthenticationFilter.class);

    private boolean postOnly = true;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LOG.info("JsonUsernamePasswordAuthenticationFilter attemptAuthentication");
        if (postOnly && !HttpMethod.POST.name().equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            if (request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    || request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
                LOG.info("a json login request, continue");
                UsernamePasswordAuthenticationToken authRequest;
                try (InputStream inputStream = request.getInputStream()) {
                    UsernamePasswordRequestModel usernamePasswordRequestModel =
                            objectMapper.readValue(inputStream, UsernamePasswordRequestModel.class);
                    authRequest = new UsernamePasswordAuthenticationToken(
                            usernamePasswordRequestModel.getUsername(), usernamePasswordRequestModel.getPassword());
                } catch (IOException e) {
                    e.printStackTrace();
                    LOG.error("failed to obtain json from request, {}", e.getMessage());
                    authRequest = new UsernamePasswordAuthenticationToken(
                            "", "");
                }
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            } else {
                // 如果不是 JSON 请求，则使用超类的 attemptAuthentication
                LOG.info("not a json request, use default UsernamePasswordAuthenticationFilter");
                return super.attemptAuthentication(request, response);
            }
        }
    }
}
