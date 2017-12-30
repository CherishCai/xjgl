package cn.cherish.xjgl.xjgl.web.req;

import lombok.Data;

@Data
public class StudentSearchReq implements java.io.Serializable {

    private static final long serialVersionUID = -6252802038931388533L;

    private String sno;
    private String nickname;

}
