package com.market.account.Enum;

import lombok.Getter;

@Getter
public enum ContactTypeEnum {

    NORMAL(0),
    PHONE(1),
    EMAIL(2);

    final Integer type;

    ContactTypeEnum(Integer type) {
        this.type = type;
    }

    public static boolean contains(int contactType) {
        for (ContactTypeEnum value : ContactTypeEnum.values()) {
            if (value.type.equals(contactType)) {
                return true;
            }
        }
        return false;
    }
}
