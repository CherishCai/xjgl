package cn.cherish.xjgl.xjgl.web.req;

import lombok.Data;

import java.util.Date;

/**
 * 会员请求参数
 * Created by Cherish on 2017/2/17.
 */
@Data
public class CustomerReq implements java.io.Serializable {

    private static final long serialVersionUID = 5487881625239168954L;

    private Long id;

    private String nickname;

    private String headimaurl;

    private String telephone;

    private String password;

    private Date createdTime;

    private Date modifiedTime;

    private Integer active;

    private Long storeId;

}
