package cn.cherish.xjgl.xjgl.web.req;

import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 学生成绩参数
 * Created by Cherish on 2017/2/17.
 */
@Data
public class ScoreReq implements java.io.Serializable {

    private static final long serialVersionUID = 5487881625239168954L;

    private Long id;

    private String sno;

    private Float num;

    private Integer subject;

    private Date createdTime;

    private Date modifiedTime;

}
