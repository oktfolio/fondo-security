package com.oktfolio.fondo.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author oktfolio oktfolio@gmail.com
 * @date 2019/11/02
 */
@Table(name = "role_resources")
public class SysRoleResource{

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    private Integer roleId;
    private Integer resourceId;
    private LocalDateTime createAt;
    private Integer createBy;

    public SysRoleResource() {
    }

    public SysRoleResource(Integer roleId, Integer resourceId) {
        this.roleId = roleId;
        this.resourceId = resourceId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }
}
