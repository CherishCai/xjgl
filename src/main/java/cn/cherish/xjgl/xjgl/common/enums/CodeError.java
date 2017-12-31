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
