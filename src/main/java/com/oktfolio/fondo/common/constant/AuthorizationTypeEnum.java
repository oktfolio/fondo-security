package com.oktfolio.fondo.common.constant;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/14
 */
public enum AuthorizationTypeEnum {

    Bearer("Bearer", "Bearer ");

    private final String name;
    private final String value;

    AuthorizationTypeEnum(String name, String value) {
        this.name = name;
        this.value = value;

    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

}
