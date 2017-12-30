package cn.cherish.xjgl.xjgl.web.controller;

import cn.cherish.xjgl.xjgl.dal.entity.Student;
import cn.cherish.xjgl.xjgl.service.StudentService;
import cn.cherish.xjgl.xjgl.web.MResponse;
import cn.cherish.xjgl.xjgl.web.dto.StudentDTO;
import cn.cherish.xjgl.xjgl.web.req.BasicSearchReq;
import cn.cherish.xjgl.xjgl.web.req.StudentReq;
import cn.cherish.xjgl.xjgl.web.req.StudentSearchReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 学生管理
 * Created by Cherish on 2017/1/6.
 */
@Controller
@RequestMapping("student")
public class StudentController extends ABaseController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("admin/student/list");
        return mv;
    }

    /**
     * 返回新增页面
     */
    @GetMapping("/add")
    public ModelAndView addForm(){
        ModelAndView mv = new ModelAndView("admin/student/add");
        return mv;
    }

    /**
     * 返回修改信息页面
     */
    @GetMapping("/{studentId}/update")
    public ModelAndView updateForm(@PathVariable("studentId") Long studentId){
        ModelAndView mv = new ModelAndView("admin/student/edit");
        Student student = studentService.findById(studentId);
        mv.addObject(student);
        return mv;
    }

    /**
     * 分页查询
     * @param basicSearchReq 基本搜索条件
     * @return JSON
     * @date 2016年8月30日 下午5:30:18
     */
    @GetMapping("/page")
    @ResponseBody
    public MResponse toPage(BasicSearchReq basicSearchReq, StudentSearchReq studentSearchReq){
        Page<StudentDTO> page = studentService.findAll(basicSearchReq, studentSearchReq);
        return buildResponse(Boolean.TRUE, basicSearchReq.getDraw(), page);
    }

    /**
     * 删除
     * @param studentId ID
     * @return JSON
     */
    @DeleteMapping("/{studentId}/delete")
    @ResponseBody
    public MResponse delete(@PathVariable("studentId") Long studentId){
        studentService.delete(studentId);
        return buildResponse(Boolean.TRUE, "删除成功", null);
    }

    /**
     * 更改信息
     * @param studentReq 更新信息
     * @return ModelAndView
     */
    @PostMapping("/update")
    public ModelAndView update(StudentReq studentReq){
        log.info("【更改信息】 {}", studentReq);
        ModelAndView mv = new ModelAndView("admin/student/edit");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if(studentReq == null || studentReq.getId() == null){
            errorMap.put("msg", "数据错误");
            return mv;
        }
        studentService.update(studentReq);
        mv.addObject("student", studentService.findById(studentReq.getId()));
        errorMap.put("msg", "修改成功");
        return mv;
    }

    /**
     * 保存
     * @param studentReq 保存的信息
     * @return ModelAndView
     */
    @PostMapping("/save")
    public ModelAndView save(StudentReq studentReq){
        log.info("【保存信息】 {}", studentReq);
        ModelAndView mv = new ModelAndView("admin/student/add");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);
        studentService.save(studentReq);
        errorMap.put("msg", "添加成功");
        return mv;
    }

}
