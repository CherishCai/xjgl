package cn.cherish.xjgl.xjgl.web.req;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 会员请求参数
 * Created by Cherish on 2017/2/17.
 */
@Data
public class CustomerReq implements java.io.Serializable {

    private static final long serialVersionUID = 5487881625239168954L;

    private Long id;

    @Length(min = 1 ,max = 16 ,message = "{user.nickname}")
    private String nickname;

    private String headimaurl;

    @Pattern(regexp = "^[1][34578][0-9]{9}$", message = "请输入正确的手机号码")
    private String telephone;

    @Length(min = 6, message="密码至少6个字符")
    private String password;

    private Date createdTime;

    private Date modifiedTime;

    private Integer active;

    @NotNull
    private Long storeId;

}
