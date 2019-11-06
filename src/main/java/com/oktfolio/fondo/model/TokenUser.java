package com.oktfolio.fondo.model;

import java.util.List;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/18
 */
public class TokenUser {

    private String username;
    private List<String> permissions;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public TokenUser() {
    }

    public TokenUser(String username, List<String> permissions) {
        this.username = username;
        this.permissions = permissions;
    }

}
