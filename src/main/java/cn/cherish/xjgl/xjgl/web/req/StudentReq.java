package cn.cherish.xjgl.xjgl.web.req;

import lombok.Data;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

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

    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date registerDate;

    private Date createdTime;

    private Date modifiedTime;

}
