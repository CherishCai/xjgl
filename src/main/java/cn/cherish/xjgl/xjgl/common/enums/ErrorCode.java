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

/**
 * 格式错误代码 400
 * 逻辑(ServiceException)错误代码 410*
 * 服务器内部错误代码 500*
 */
@AllArgsConstructor
@Getter
public enum ErrorCode {
    ERROR_CODE_400("EC400","请求参数错误"),

    ERROR_CODE_500_001("EC500001","服务器忙,请稍后再试"),
    ERROR_CODE_500_002("EC500002","资源忙，请稍后再试");
    private String code;

    private String desc;
}
