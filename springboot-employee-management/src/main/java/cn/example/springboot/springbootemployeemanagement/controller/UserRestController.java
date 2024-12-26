package cn.example.springboot.springbootemployeemanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.example.springboot.springbootemployeemanagement.entity.User;
import cn.example.springboot.springbootemployeemanagement.service.UserService;
import cn.example.springboot.springbootemployeemanagement.vo.JsonResult;
import cn.example.springboot.springbootemployeemanagement.vo.UserVO;

@RestController
@RequestMapping("/user")
public class UserRestController {
    @Autowired
    private UserService userService;

    @GetMapping("/view/{id}")
    public JsonResult getUserInfo(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        if (user != null) {
            UserVO userVO = new UserVO();
            userVO.setId(user.getId());
            userVO.setUsername(user.getUsername());
            userVO.setGmtCreate(user.getGmtCreate());
            userVO.setGmtModified(user.getGmtModified());
            return JsonResult.success(userVO);
        }
        return JsonResult.fail("用户不存在");
    }

    @GetMapping("/list")
    public JsonResult getUserList() {
        try {
            List<UserVO> userList = userService.findAll();
            return JsonResult.success(userList);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("获取数据失败！");
        }
    }

    @PutMapping("/edit")
    @PreAuthorize("hasAnyRole('HR', 'SYSTEM_ADMIN')")
    public JsonResult edit(@RequestBody UserVO userVO) {
        if (userService.edit(userVO)) {
            return JsonResult.success("修改成功");
        }
        return JsonResult.fail("修改失败");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('HR', 'SYSTEM_ADMIN')")
    public JsonResult delete(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return JsonResult.success("删除成功");
        }
        return JsonResult.fail("删除失败");
    }
}