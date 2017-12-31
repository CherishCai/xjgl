package cn.cherish.xjgl.xjgl.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author caihongwen@u51.com
 * @date 2017/12/31 21:32
 */
@Getter
@AllArgsConstructor
public enum SubjectEnum {

    GTDS(1, "高等代数"),
    SXFX(2, "数学分析"),
    JXJH(3, "解析几何"),
    JSJ(4, "计算机");

    private Integer seq;

    private String desc;

    public static String getDesc(Integer status) {
        switch (status) {
            case 1:
                return GTDS.desc;
            case 2:
                return SXFX.desc;
            case 3:
                return JXJH.desc;
            case 4:
                return JSJ.desc;
            default:
                return null;
        }
    }
}
