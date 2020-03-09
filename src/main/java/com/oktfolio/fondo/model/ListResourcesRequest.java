package com.oktfolio.fondo.model;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/11/04
 */
public class ListResourcesRequest {

    private Integer id;
    private String name;
    private String code;
    private Integer type;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
