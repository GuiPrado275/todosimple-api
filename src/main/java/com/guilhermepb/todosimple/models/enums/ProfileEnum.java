package com.guilhermepb.todosimple.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;


@AllArgsConstructor
@Getter
public enum ProfileEnum {

    ADMIN(1, "ROLE_ADMIN"),  //two types of users
    USER(2, "ROLE_USER");

    private Integer code;
    private String description;

    public static ProfileEnum toEnum(Integer code) { //to find out if you are an administrator or a normal user

        if(Objects.isNull(code)){
            return null;
        }
        for (ProfileEnum x : ProfileEnum.values()) {
            if (code.equals(x.getCode())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Invalid Code " + code);

    }
}
