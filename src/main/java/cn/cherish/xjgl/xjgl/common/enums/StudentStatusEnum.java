package cn.cherish.xjgl.xjgl.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author caihongwen@u51.com
 * @date 2017/12/31 21:32
 */
@Getter
@AllArgsConstructor
public enum StudentStatusEnum {

    school(1, "在校"),
    graduate(2, "毕业"),
    temporary(3, "休学"),
    leave(4, "退学");

    private Integer seq;

    private String desc;

    public static String getDesc(Integer status) {
        switch (status) {
            case 1:
                return school.desc;
            case 2:
                return graduate.desc;
            case 3:
                return temporary.desc;
            case 4:
                return leave.desc;
            default:
                return school.desc;
        }
    }
}
