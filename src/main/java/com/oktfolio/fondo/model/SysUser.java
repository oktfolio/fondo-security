package com.oktfolio.fondo.model;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/14
 */
@Table(name = "users")
public class SysUser extends BaseModel {

    public interface SimpleView {
    }

    public interface WithoutPasswordView extends SimpleView {
    }

    public interface WithPasswordView extends WithoutPasswordView {
    }

    @Id
    @GeneratedValue(generator = "JDBC")
    @JsonView(WithoutPasswordView.class)
    private Integer id;

    @JsonView(SimpleView.class)
    private String username;

    @JsonView(WithPasswordView.class)
    private String password;

    @JsonView(WithoutPasswordView.class)
    private String realName;

    @JsonView(WithoutPasswordView.class)
    private String mobile;

    @JsonView(WithoutPasswordView.class)
    private String gender;

    @JsonView(WithoutPasswordView.class)
    private String avatar;

    @JsonView(WithoutPasswordView.class)
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
