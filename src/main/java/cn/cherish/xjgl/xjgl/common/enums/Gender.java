package cn.cherish.xjgl.xjgl.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别枚举类
 */
@Getter
@AllArgsConstructor
public enum Gender {
    M("M", "男"),
    F("F", "女"),
    N("N", "未知");

    private String code;

    private String desc;
}
