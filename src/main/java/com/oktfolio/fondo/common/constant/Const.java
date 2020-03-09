package com.oktfolio.fondo.common.constant;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/13
 */
public class Const {

    public enum UserStatusEnum {

        CREDENTIAL_EXPIRED(4, "密码过期"),
        LOCKED(3, "已锁定"),
        EXPIRED(2, "已过期"),
        ENABLED(1, "已启用"),
        DISABLED(0, "未启用"),
        DELETED(-1, "已删除");

        private final int value;
        private final String desc;

        UserStatusEnum(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public int getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum RoleStatusEnum {
        ENABLED(1, "已启用"),
        DISABLED(0, "未启用");

        private final int value;
        private final String desc;

        RoleStatusEnum(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public int getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum ResourceStatusEnum {
        ENABLED(1, "已启用"),
        DISABLED(0, "未启用");

        private final int value;
        private final String desc;

        ResourceStatusEnum(int code, String desc) {
            this.value = code;
            this.desc = desc;
        }

        public int getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }

    }

    public enum ResourceTypeEnum {
        OTHER(0, "其他"),
        API(1, "API"),
        MENU(2, "菜单");

        private final int value;
        private final String desc;

        ResourceTypeEnum(int code, String desc) {
            this.value = code;
            this.desc = desc;
        }

        public int getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }
}