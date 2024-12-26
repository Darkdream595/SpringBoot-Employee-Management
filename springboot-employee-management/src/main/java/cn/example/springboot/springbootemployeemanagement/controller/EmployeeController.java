package cn.example.springboot.springbootemployeemanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.example.springboot.springbootemployeemanagement.entity.User;
import cn.example.springboot.springbootemployeemanagement.service.UserService;
import cn.example.springboot.springbootemployeemanagement.vo.JsonResult;
import cn.example.springboot.springbootemployeemanagement.vo.UserVO;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('HR', 'SYSTEM_ADMIN')")
    public JsonResult list() {
        try {
            return JsonResult.success(userService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("获取员工列表失败");
        }
    }

    @GetMapping("/view/{id}")
    @PreAuthorize("hasAnyRole('HR', 'SYSTEM_ADMIN')")
    public JsonResult view(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user != null) {
            UserVO userVO = new UserVO();
            userVO.setId(user.getId());
            userVO.setUsername(user.getUsername());
            return JsonResult.success(userVO);
        }
        return JsonResult.fail("用户不存在");
    }
} 