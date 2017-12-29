package cn.cherish.xjgl.xjgl.web.controller;

import cn.cherish.xjgl.xjgl.dal.entity.User;
import cn.cherish.xjgl.xjgl.service.UserService;
import cn.cherish.xjgl.xjgl.web.MResponse;
import cn.cherish.xjgl.xjgl.web.dto.UserDTO;
import cn.cherish.xjgl.xjgl.web.req.BasicSearchReq;
import cn.cherish.xjgl.xjgl.web.req.UserReq;
import cn.cherish.xjgl.xjgl.web.req.UserSearchReq;
import com.google.common.base.Throwables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 * Created by Cherish on 2017/1/6.
 */
@Controller
@RequestMapping("user")
public class UserController extends ABaseController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("admin/user/list");
        return mv;
    }

    /**
     * 返回新增用户的页面
     */
    @GetMapping("/add")
    public ModelAndView addForm(){
        ModelAndView mv = new ModelAndView("admin/user/add");
        return mv;
    }

    /**
     * 返回修改用户信息的页面
     */
    @GetMapping("/{userId}/update")
    public ModelAndView updateForm(@PathVariable("userId") Long userId){
        ModelAndView mv = new ModelAndView("admin/user/edit");
        User user = userService.findById(userId);
        mv.addObject("user", user);
        return mv;
    }

    /**
     * 分页查询用户
     * @param basicSearchReq 基本搜索条件
     * @return JSON
     * @date 2016年8月30日 下午5:30:18
     */
    @GetMapping("/page")
    @ResponseBody
    public MResponse toPage(BasicSearchReq basicSearchReq, UserSearchReq userSearchReq){
        Page<UserDTO> page = userService.findAll(userSearchReq, basicSearchReq);
        return buildResponse(Boolean.TRUE, basicSearchReq.getDraw(), page);
    }

    /**
     * 删除用户
     * @param userId 用户ID
     * @return JSON
     */
    @DeleteMapping("/{userId}/delete")
    @ResponseBody
    public MResponse delete(@PathVariable("userId") Long userId){
        userService.delete(userId);
        return buildResponse(Boolean.TRUE, "删除成功", null);
    }

    /**
     * 更改用户信息
     * @param userReq 更新信息
     * @return ModelAndView
     */
    @PostMapping("/update")
    public ModelAndView update(UserReq userReq){
        log.info("【更改用户信息】 {}", userReq);
        ModelAndView mv = new ModelAndView("admin/user/edit");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if(userReq == null || userReq.getId() == null){
            errorMap.put("msg", "数据错误");
            return mv;
        }
        try {
            userService.updateByReq(userReq);
            mv.addObject("user", userService.findById(userReq.getId()));
            errorMap.put("msg", "修改成功");
        } catch (Exception e) {
            errorMap.put("msg", "系统繁忙");
            log.error("更改用户信息】 {}", Throwables.getStackTraceAsString(e));
        }
        return mv;
    }

    /**
     * 保存新用户
     * @param userReq 保存的信息
     * @return ModelAndView
     */
    @PostMapping("/save")
    public ModelAndView save(UserReq userReq){
        log.info("【保存新用户】 {}", userReq);
        ModelAndView mv = new ModelAndView("admin/user/add");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);
        try {
            if (userService.exist(userReq.getUsername())){
                errorMap.put("msg", "该用户名已存在，请更换再试");
                mv.addObject("user", userReq);
            }else {
                userService.saveByReq(userReq);
                errorMap.put("msg", "添加成功");
            }
        } catch (Exception e) {
            errorMap.put("msg", "系统繁忙");
            log.error("【保存新用户】 {}", Throwables.getStackTraceAsString(e));
        }
        return mv;
    }

}
