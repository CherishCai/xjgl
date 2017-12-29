package cn.cherish.xjgl.xjgl.web.req;

import lombok.Data;

@Data
public class UserSearchReq implements java.io.Serializable {

    private static final long serialVersionUID = 5644178229675645079L;

    private String username;

    private String nickname;

    private String telephone;


}
