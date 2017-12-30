package cn.cherish.xjgl.xjgl.web.req;

import lombok.Data;

import java.util.Date;

/**
 * 学生请求参数
 * Created by Cherish on 2017/2/17.
 */
@Data
public class StudentReq implements java.io.Serializable {

    private static final long serialVersionUID = 5487881625239168954L;

    private Long id;

    private String sno;

    private String nickname;

    private Date createdTime;

    private Date modifiedTime;

}
