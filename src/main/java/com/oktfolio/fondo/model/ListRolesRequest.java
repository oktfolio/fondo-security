package com.oktfolio.fondo.model;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/11/04
 */
public class ListRolesRequest {
    private Integer id;
    private String name;
    private String code;

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
}
