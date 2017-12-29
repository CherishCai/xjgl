package cn.cherish.xjgl.xjgl.web.controller;

import cn.cherish.xjgl.xjgl.dal.entity.Customer;
import cn.cherish.xjgl.xjgl.service.CustomerService;
import cn.cherish.xjgl.xjgl.web.MResponse;
import cn.cherish.xjgl.xjgl.web.dto.CustomerDTO;
import cn.cherish.xjgl.xjgl.web.req.BasicSearchReq;
import cn.cherish.xjgl.xjgl.web.req.CustomerReq;
import cn.cherish.xjgl.xjgl.web.req.CustomerSearchReq;
import com.google.common.base.Throwables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 会员
 * Created by Cherish on 2017/1/6.
 */
@Controller
@RequestMapping("customer")
public class CustomerController extends ABaseController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("admin/customer/list");
        return mv;
    }

    /**
     * 返回新增页面
     */
    @GetMapping("/add")
    public ModelAndView addForm(){
        ModelAndView mv = new ModelAndView("admin/customer/add");
        return mv;
    }

    /**
     * 返回修改信息页面
     */
    @GetMapping("/{customerId}/update")
    public ModelAndView updateForm(@PathVariable("customerId") Long customerId){
        ModelAndView mv = new ModelAndView("admin/customer/edit");
        Customer customer = customerService.findById(customerId);
        mv.addObject(customer);
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
    public MResponse toPage(BasicSearchReq basicSearchReq, CustomerSearchReq customerSearchReq){
        Page<CustomerDTO> page = customerService.findAll(basicSearchReq, customerSearchReq);
        return buildResponse(Boolean.TRUE, basicSearchReq.getDraw(), page);
    }

    /**
     * 删除
     * @param customerId ID
     * @return JSON
     */
    @DeleteMapping("/{customerId}/delete")
    @ResponseBody
    public MResponse delete(@PathVariable("customerId") Long customerId){
        customerService.delete(customerId);
        return buildResponse(Boolean.TRUE, "删除成功", null);
    }

    /**
     * 更改信息
     * @param customerReq 更新信息
     * @return ModelAndView
     */
    @PostMapping("/update")
    public ModelAndView update(@Validated CustomerReq customerReq, BindingResult bindingResult){
        log.info("【更改信息】 {}", customerReq);
        ModelAndView mv = new ModelAndView("admin/customer/edit");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if(customerReq == null || customerReq.getId() == null){
            errorMap.put("msg", "数据错误");
            return mv;
        }
        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("customer", customerReq);
            return mv;
        }
        try {
            customerService.update(customerReq);
            mv.addObject("customer", customerService.findById(customerReq.getId()));
            errorMap.put("msg", "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            errorMap.put("msg", "系统繁忙");
            log.error("【更改信息】 {}", Throwables.getStackTraceAsString(e));
        }
        return mv;
    }

    /**
     * 保存
     * @param customerReq 保存的信息
     * @return ModelAndView
     */
    @PostMapping("/save")
    public ModelAndView save(@Validated CustomerReq customerReq, BindingResult bindingResult){
        log.info("【保存信息】 {}", customerReq);
        ModelAndView mv = new ModelAndView("admin/customer/add");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("customer", customerReq);
            return mv;
        }
        try {
            customerService.save(customerReq);
            errorMap.put("msg", "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            errorMap.put("msg", "系统繁忙");
            log.error("保存信息】 {}", Throwables.getStackTraceAsString(e));
        }
        return mv;
    }

}
