package com.oktfolio.fondo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/18
 */
@Component
@ConfigurationProperties(prefix="fondo.security")
public class FondoSecurityProperties {

    public boolean useRedis = false;

    public int tokenExpireTime = 30;

    public String signKey = "oktfolio";

    public boolean singleDeviceLogin = false;

    public boolean storePermissions = false;

    public static final String USER_STR = "user:";

    public static final String TOKEN_PRE = "token:";

    public void setUseRedis(boolean useRedis) {
        this.useRedis = useRedis;
    }

    public void setTokenExpireTime(int tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }

    public void setSingleDeviceLogin(boolean singleDeviceLogin) {
        this.singleDeviceLogin = singleDeviceLogin;
    }

    public void setStorePermissions(boolean storePermissions) {
        this.storePermissions = storePermissions;
    }
}
