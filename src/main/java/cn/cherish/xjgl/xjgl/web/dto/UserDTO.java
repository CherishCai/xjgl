package cn.cherish.xjgl.xjgl.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class UserDTO implements java.io.Serializable {

    private static final long serialVersionUID = 6265204827137964278L;

    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String telephone;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifiedTime;

    private String description;

}
