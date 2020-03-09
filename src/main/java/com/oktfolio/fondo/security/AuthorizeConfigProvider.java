package com.oktfolio.fondo.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * @author oktfolio
 * @date 2019/09/13
 */
public interface AuthorizeConfigProvider {
    /**
     *
     * @param config
     * @return boolean 返回值表示配置中是否有针对 anyRequest 的配置。在整个授权配置中，应该有且仅有一个针对 anyRequest 的配置。
     * 如果所有的实现都没有针对 anyRequest 的配置，系统会自动增加一个 anyRequest().authenticated() 配置。
     * 如果有多个针对 anyRequest 的配置，则会抛出异常。
     */
    boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config);
}
