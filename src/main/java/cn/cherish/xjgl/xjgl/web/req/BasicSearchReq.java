package cn.cherish.xjgl.xjgl.web.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 基本搜索条件
 * @author Cherish
 */
@Data
public class BasicSearchReq implements Serializable {

	private static final long serialVersionUID = -1707407604592617342L;

	//orderColumn=name&orderDir=desc&
	private String orderColumn;
	private String orderDir;
	//startIndex=0&pageSize=10&draw=2
	private Integer startIndex = 0;
	private Integer pageSize = 20;
	private String draw;

	/**
	 * 判断本对象是否下列属性均为空
	 */
	public boolean isBlank() {
		return null == this.getStartIndex() && null == this.getPageSize();
	}

}
