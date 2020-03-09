package com.oktfolio.fondo.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/15
 */
@Table(name = "roles")
public class SysRole  extends BaseModel{

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private String name;

    private String code;

    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
