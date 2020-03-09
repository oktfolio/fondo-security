package com.oktfolio.fondo.security;

import com.oktfolio.fondo.common.constant.Const;
import com.oktfolio.fondo.model.ResourcesLiteBo;
import com.oktfolio.fondo.model.SysRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author oktfolio oktfolio@gmail.com
 * @date 2019/09/05
 */
public class JWTUserDetails implements UserDetails {

    private Integer id;
    private String username;
    private String password;
    private Integer status;
    private List<SysRole> roles;
    private List<ResourcesLiteBo> resources;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    public List<ResourcesLiteBo> getResources() {
        return resources;
    }

    public void setResources(List<ResourcesLiteBo> resources) {
        this.resources = resources;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        List<ResourcesLiteBo> resources = this.getResources();
        // 添加请求权限
        if (resources != null && resources.size() > 0) {
            for (ResourcesLiteBo resource : resources) {
                if (StringUtils.isNotBlank(resource.getUri())
                        && StringUtils.isNotBlank(resource.getMethod())) {

                    authorityList.add(new SimpleGrantedAuthority(resource.getMethod() + ":" + resource.getUri()));
                }
            }
        }
        return authorityList;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
//        return !this.status.equals(Const.UserStatusEnum.EXPIRED.getValue());
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
//        return !this.status.equals(Const.UserStatusEnum.LOCKED.getValue())
//                || !this.status.equals(Const.UserStatusEnum.DISABLED.getValue());
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
//        return !this.status.equals(Const.UserStatusEnum.CREDENTIAL_EXPIRED.getValue());
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status.equals(Const.UserStatusEnum.ENABLED.getValue());
    }
}
