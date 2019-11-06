package com.oktfolio.fondo.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * 授权信息管理器
 *
 * 用于收集系统中所有的 AuthorizeConfigProvider 并加载其配置
 *
 * @author oktfolio
 * @date 2019/09/13
 */
public interface AuthorizeConfigManager {

    /**
     *
     * @param config
     */
    void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config);
}
