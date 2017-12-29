/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package cn.cherish.xjgl.xjgl.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 手机验证码
 * Created by Cherish on 2017/3/19.
 */
@Getter
@AllArgsConstructor
@ToString
public enum CodeError {
    SUCCESS("验证码获取成功，请输入验证"),
    FAIL("系统繁忙，验证码获取失败"),
    TOO_MUCH("获取次数过多，请30分钟后再试"),
    ;

    private String desc;
}
