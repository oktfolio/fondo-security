package com.oktfolio.fondo.common.result;

/**
 * @author oktfolio oktfolio@gmail.com
 * @date 2019/11/06
 */
public enum RoleResultCodeEnum implements ResultCode {

    // failed to create role
    FAILED_CREATE_ROLE(400210, "failed to create roles"),
    // role name exists
    ROLE_NAME_EXISTS(400211, "role name exists"),
    // role code exists
    ROLE_CODE_EXISTS(400212, "role code exists"),
    // deleted or not exist
    ROLE_DELETE_OR_NOT_EXISTS(400213, "role has been deleted or not exists"),
    // failed to disable role
    FAILED_DISABLE_ROLE(400214, "failed to disable role"),
    // failed to enable role
    FAILED_ENABLE_ROLE(400215, "failed to disable role"),
    // failed to update role
    FAILED_UPDATE_ROLE(400210, "failed to update user roles");

    private final int value;
    private final String message;

    RoleResultCodeEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    @Override
    public int value() {
        return this.value;
    }

    @Override
    public String message() {
        return this.message;
    }
}
