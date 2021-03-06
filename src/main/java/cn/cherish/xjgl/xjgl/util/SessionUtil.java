
package cn.cherish.xjgl.xjgl.util;


import cn.cherish.xjgl.xjgl.dal.entity.User;

/**
 * @author Cherish
 */
public class SessionUtil {
	
	private static String SESSION_USER = "sessionUser";

	public static User getUser() {
		return (User) get(SESSION_USER);
	}

	public static void addUser(User user) {
		add(SESSION_USER, user);
	}

	public static Object get(String attrName) {
		return RequestHolder.getSession().getAttribute(attrName);
	}

	public static void add(String attrName, Object val) {
		RequestHolder.getSession().setAttribute(attrName, val);
	}



}
