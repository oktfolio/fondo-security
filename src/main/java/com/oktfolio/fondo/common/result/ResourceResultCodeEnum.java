package com.oktfolio.fondo.common.result;

/**
 * @author oktfolio oktfolio@gmail.com
 * @date 2019/11/06
 */
public enum ResourceResultCodeEnum implements ResultCode {

    // failed to create resource
    FAILED_CREATE_RESOURCE(400310, "failed to create resource"),
    // resource name exists
    RESOURCE_NAME_EXISTS(400311, "resource name exists"),
    // resource code exists
    RESOURCE_CODE_EXISTS(400312, "resource code exists"),
    // deleted or not exist
    RESOURCE_DELETE_OR_NOT_EXISTS(400313, "resource has been deleted or not exists"),
    // failed to disable resource
    FAILED_DISABLE_RESOURCE(400314, "failed to disable resource"),
    // failed to enable resource
    FAILED_ENABLE_RESOURCE(400315, "failed to disable resource"),
    // failed to update resource
    FAILED_UPDATE_RESOURCE(400310, "failed to update user resource");

    private final int value;
    private final String message;

    ResourceResultCodeEnum(int value, String message) {
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
