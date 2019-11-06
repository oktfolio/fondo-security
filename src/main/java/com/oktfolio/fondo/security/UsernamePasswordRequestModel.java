package com.oktfolio.fondo.security;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/14
 */
public class UsernamePasswordRequestModel {
    private String username;
    private String password;

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

    @Override
    public String toString() {
        return "UsernamePasswordRequestModel{" +
                "username='" + username + '\'' +
                ", password='" + null + '\'' +
                '}';
    }
}
