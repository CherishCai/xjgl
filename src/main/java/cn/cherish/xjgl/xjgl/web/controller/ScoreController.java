package cn.cherish.xjgl.xjgl.web.controller;

import cn.cherish.xjgl.xjgl.dal.entity.Score;
import cn.cherish.xjgl.xjgl.dal.entity.Student;
import cn.cherish.xjgl.xjgl.service.ScoreService;
import cn.cherish.xjgl.xjgl.service.StudentService;
import cn.cherish.xjgl.xjgl.web.MResponse;
import cn.cherish.xjgl.xjgl.web.dto.ScoreDTO;
import cn.cherish.xjgl.xjgl.web.req.BasicSearchReq;
import cn.cherish.xjgl.xjgl.web.req.ScoreReq;
import cn.cherish.xjgl.xjgl.web.req.ScoreSearchReq;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 学生管理
 * Created by Cherish on 2017/1/6.
 */
@Controller
@RequestMapping("score")
public class ScoreController extends ABaseController {

    private final ScoreService scoreService;
    private final StudentService studentService;

    @Autowired
    public ScoreController(ScoreService scoreService, StudentService studentService) {
        this.scoreService = scoreService;
        this.studentService = studentService;
    }

    @GetMapping
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("admin/score/list");
        return mv;
    }

    /**
     * 返回新增页面
     */
    @GetMapping("/add")
    public ModelAndView addForm(){
        ModelAndView mv = new ModelAndView("admin/score/add");
        return mv;
    }

    /**
     * 返回修改信息页面
     */
    @GetMapping("/{scoreId}/update")
    public ModelAndView updateForm(@PathVariable("scoreId") Long scoreId){
        ModelAndView mv = new ModelAndView("admin/score/edit");
        Score score = scoreService.findById(scoreId);
        mv.addObject(score);
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
    public MResponse toPage(BasicSearchReq basicSearchReq, ScoreSearchReq scoreSearchReq){
        Page<ScoreDTO> page = scoreService.findAll(basicSearchReq, scoreSearchReq);
        return buildResponse(Boolean.TRUE, basicSearchReq.getDraw(), page);
    }

    /**
     * 删除
     * @param scoreId ID
     * @return JSON
     */
    @DeleteMapping("/{scoreId}/delete")
    @ResponseBody
    public MResponse delete(@PathVariable("scoreId") Long scoreId){
        scoreService.delete(scoreId);
        return buildResponse(Boolean.TRUE, "删除成功", null);
    }

    /**
     * 更改信息
     * @param scoreReq 更新信息
     * @return ModelAndView
     */
    @PostMapping("/update")
    public ModelAndView update(ScoreReq scoreReq){
        log.info("【更改信息】 {}", scoreReq);
        ModelAndView mv = new ModelAndView("admin/score/edit");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if(scoreReq == null || scoreReq.getId() == null){
            errorMap.put("msg", "数据错误");
            return mv;
        }

        Student bySno = studentService.findBySno(scoreReq.getSno());
        if (bySno == null) {
            mv.addObject("score", scoreReq);
            errorMap.put("msg", "不存在该学号的学生");
            return mv;
        }

        scoreService.update(scoreReq);
        mv.addObject("score", scoreService.findById(scoreReq.getId()));
        errorMap.put("msg", "修改成功");
        return mv;
    }

    /**
     * 保存
     * @param scoreReq 保存的信息
     * @return ModelAndView
     */
    @PostMapping("/save")
    public ModelAndView save(ScoreReq scoreReq){
        log.info("【保存信息】 {}", scoreReq);
        ModelAndView mv = new ModelAndView("admin/score/add");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        Student bySno = studentService.findBySno(scoreReq.getSno());
        if (bySno == null) {
            mv.addObject("score", scoreReq);
            errorMap.put("msg", "不存在该学号的学生");
            return mv;
        }

        scoreService.save(scoreReq);
        errorMap.put("msg", "添加成功");
        return mv;
    }

}
