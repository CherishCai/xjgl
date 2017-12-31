package cn.cherish.xjgl.xjgl.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

/**
 * Created by Cherish on 2017/2/17.
 */
@Data
public class ScoreDTO implements java.io.Serializable {

    private static final long serialVersionUID = -3859558785433931029L;

    private Long id;

    private String sno;

    private Float num;

    private Integer subject;

    private String subjectStr;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifiedTime;

}
