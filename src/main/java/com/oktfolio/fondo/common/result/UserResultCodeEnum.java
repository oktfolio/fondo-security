package com.oktfolio.fondo.common.result;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/18
 */
public enum UserResultCodeEnum implements ResultCode {

    // failed to delete user roles
    FAILED_DELETE_USER_ROLES(400030,"failed to delete user roles"),
    // failed to add user roles
    FAILED_ADD_USER_ROLES(400031,"failed to add user roles"),
    // failed create user
    FAILED_CREATE_USER(400020, "failed to create user"),
    // failed delete user
    FAILED_DELETE_USER(400021, "failed to delete user"),
    // failed update user
    FAILED_UPDATE_USER(400022, "failed to update user"),
    // username exists
    USERNAME_EXISTS(400010, "username already registered"),
    // email exists
    EMAIL_EXISTS(400011, "email has bean used by other user"),
    // bad username or password
    MOBILE_EXISTS(400012, "mobile has bean used by other user"),
    // bad username or password
    BAD_USERNAME_PASSWORD(401011, "bad username or password"),
    // user not exist
    USER_NOT_EXIST(401012, "user not exist"),
    // invalid login status
    INVALID_LOGIN_STATUS(401113, "invalid login status, please relogin"),
    // invalid token
    INVALID_TOKEN(401014, "invalid token"),
    // user expired
    USER_EXPIRED(401015, "user expired"),
    // user locked or disabled
    USER_LOCKED(401016, "user locked or disabled"),
    // credentials expired
    CREDENTIALS_EXPIRED(401017, "credentials expired"),
    // user disabled
    USER_DISABLED(401018, "user disabled"),
    // failed login
    FAILED_LOGIN(401019, "failed login");

    private final int value;
    private final String message;

    UserResultCodeEnum(int value, String message) {
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
