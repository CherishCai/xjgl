package cn.cherish.xjgl.xjgl.web.controller;

import cn.cherish.xjgl.xjgl.common.shiro.CryptographyUtil;
import cn.cherish.xjgl.xjgl.common.shiro.ShiroUserUtil;
import cn.cherish.xjgl.xjgl.service.UserService;
import cn.cherish.xjgl.xjgl.util.SessionUtil;
import cn.cherish.xjgl.xjgl.util.ValidateCode;
import cn.cherish.xjgl.xjgl.web.req.UserReq;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础
 * Created by Cherish on 2017/1/6.
 */
@Controller
public class BasicController extends ABaseController {

	private final UserService userService;

	@Autowired
	public BasicController(UserService userService) {
		this.userService = userService;
	}

    /**
     * 管理页面
     */
    @GetMapping(value = "admin")
    public String admin(){
        return "admin/datapanel";
    }

	/**
	 * 登陆页面
	 */
	@GetMapping(value = "/login")
	public String login(){
		return "admin/login";
	}
	
	/**
	 * 执行登陆
	 */
	@PostMapping(value = "/login")
	public ModelAndView login(@Validated UserReq loginReq, BindingResult bindingResult, HttpServletRequest request){
		log.info("【执行登陆】 {}", loginReq);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/login");
        Map<String, Object> errorMap = new HashMap<>();
        modelAndView.addObject("errorMap", errorMap);

		String code = (String) request.getSession().getAttribute("validateCode");
		String submitCode = WebUtils.getCleanParam(request, "validateCode");
		//判断验证码
		if (StringUtils.isBlank(submitCode) || !StringUtils.equalsIgnoreCase(code,submitCode.toLowerCase())) {
			log.debug("验证码不正确");
            errorMap.put("validateCodeError", "验证码不正确");
            //添加上表单输入数据返回给页面
			modelAndView.addObject("loginReq", loginReq);
			return modelAndView;
		}
		//表单验证是否通过
		if (bindingResult.hasErrors()) {
			errorMap.putAll(getErrors(bindingResult));
			//添加上表单输入数据返回给页面
			modelAndView.addObject("loginReq", loginReq);
			return modelAndView;
		}
		//实现登陆
		UsernamePasswordToken token = new UsernamePasswordToken(
				loginReq.getUsername(), CryptographyUtil.cherishSha1(loginReq.getPassword()));
		//token.setRememberMe(true);
		Subject subject = SecurityUtils.getSubject();

		try {
			//subject.login(token);就会调用 ShiroRealm的 doGetAuthenticationInfo方法
			subject.login(token);
		} catch (UnknownAccountException uae) {
			log.warn("账户不存在!");
			errorMap.put("username","账户或密码错误，请重新输入");
		} catch (IncorrectCredentialsException ice) {
			errorMap.put("username","账户或密码错误，请重新输入");
			log.warn("密码不正确!");
		} catch (LockedAccountException lae) {
			log.warn("账户被冻结!");
			errorMap.put("username","该账户被冻结");
		}catch(ExcessiveAttemptsException eae){
			log.warn("错误次数过多");
			errorMap.put("username","密码错误次数过多，请稍后再试");
		} catch (AuthenticationException ae) {
			token.clear();
			errorMap.put("username","系统认证错误");
			log.warn("认证错误!");
		}

		if (subject.isAuthenticated()){
			log.debug("登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
			Session session = subject.getSession();
			session.setAttribute("msg", "登陆成功");
			session.setAttribute("username", ShiroUserUtil.getUsername());
			session.setAttribute("nickname", ShiroUserUtil.getNickname());
			SessionUtil.addUser(userService.findByUsername(ShiroUserUtil.getUsername()));
			modelAndView.setViewName("redirect:/admin");
		}
		return modelAndView;
	}

	@GetMapping("/403")
	public String unauthorizedRole(){
		log.warn("无权限");
		return "error/403";
	}
	
	/**
	 * 生成验证码
	 * @param request 请求
	 * @param response 响应
	 * @throws IOException 图片生成错误
	 */
	@GetMapping(value = "/validateCode")
	public void validateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setHeader("Cache-Control", "no-cache");
		String verifyCode = ValidateCode.generateTextCode(ValidateCode.TYPE_NUM_ONLY, 4, null);
		request.getSession().setAttribute("validateCode", verifyCode);
		response.setContentType("image/jpeg");
		BufferedImage bim = ValidateCode.generateImageCode(verifyCode, 90, 30, 3, true, Color.WHITE, Color.BLACK, null);
		ImageIO.write(bim, "JPEG", response.getOutputStream());
	}

}
