package com.oktfolio.fondo.model;

/**
 * @author oktfolio oktfolio@gmail.com
 * @date 2019/11/06
 */
public class CreateUserRequest {

    private String username;

    private String password;

    private String realName;

    private String mobile;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
