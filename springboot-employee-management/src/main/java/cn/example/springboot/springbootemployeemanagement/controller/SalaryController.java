package cn.example.springboot.springbootemployeemanagement.controller;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.example.springboot.springbootemployeemanagement.entity.Salary;
import cn.example.springboot.springbootemployeemanagement.entity.User;
import cn.example.springboot.springbootemployeemanagement.service.SalaryService;
import cn.example.springboot.springbootemployeemanagement.service.UserService;
import cn.example.springboot.springbootemployeemanagement.vo.JsonResult;
import cn.example.springboot.springbootemployeemanagement.vo.SalaryVO;

@RestController
@RequestMapping("/salary")
public class SalaryController {

    @Autowired
    private SalaryService salaryService;
    
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('HR', 'SYSTEM_ADMIN')")
    public JsonResult list() {
        List<Salary> salaries = salaryService.findAll();
        List<SalaryVO> salaryVOs = salaries.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        return JsonResult.success(salaryVOs);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('NORMAL_EMPLOYEE', 'HR', 'SYSTEM_ADMIN')")
    public JsonResult getMySalary() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.getByUsername(username).orElse(null);
        if (user == null) {
            return JsonResult.fail("用户不存在");
        }
        List<Salary> salaries = salaryService.findByUserId(user.getId());
        List<SalaryVO> salaryVOs = salaries.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        return JsonResult.success(salaryVOs);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('HR', 'SYSTEM_ADMIN')")
    public JsonResult getUserSalary(@PathVariable Long userId) {
        List<Salary> salaries = salaryService.findByUserId(userId);
        List<SalaryVO> salaryVOs = salaries.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        return JsonResult.success(salaryVOs);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('HR') or hasRole('SYSTEM_ADMIN')")
    public JsonResult add(@RequestBody SalaryVO salaryVO) {
        try {
            Salary salary = new Salary();
            salary.setUserId(salaryVO.getUserId());
            
            // 转换月份
            salary.setSalaryMonth(salaryVO.getSalaryMonth());
            
            salary.setBaseSalary(salaryVO.getBaseSalary());
            salary.setBonus(salaryVO.getBonus());
            salary.setTotalSalary(salaryVO.getBaseSalary().add(salaryVO.getBonus()));
            salary.setGmtCreate(Instant.now());
            salary.setGmtModified(Instant.now());
            
            salaryService.save(salary);
            return JsonResult.success("添加成功");
        } catch (Exception e) {
            return JsonResult.fail("添加失败：" + e.getMessage());
        }
    }

    private SalaryVO convertToVO(Salary salary) {
        SalaryVO vo = new SalaryVO();
        vo.setId(salary.getId());
        vo.setUserId(salary.getUserId());
        vo.setSalaryMonth(salary.getSalaryMonth());
        vo.setBaseSalary(salary.getBaseSalary());
        vo.setBonus(salary.getBonus());
        vo.setTotalSalary(salary.getTotalSalary());
        return vo;
    }
} 