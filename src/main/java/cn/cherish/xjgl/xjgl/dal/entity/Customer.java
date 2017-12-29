package cn.cherish.xjgl.xjgl.dal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_customer")
public class Customer implements java.io.Serializable {

	private static final long serialVersionUID = 2285174464789310329L;

	@Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    /**
     * 昵称
     */
    @Column(name = "nickname", nullable = false, length = 32)
    private String nickname;
    /**
     * 手机
     */
    @Column(name = "telephone", nullable = false, length = 11)
    private String telephone;
    /**
     * 密码
     */
    @JsonIgnore
    @Column(name = "password", nullable = false, length = 40)
    private String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", nullable = false, length = 19)
    private Date createdTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_time", length = 19)
    private Date modifiedTime;

}
