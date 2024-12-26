package cn.example.springboot.springbootemployeemanagement.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.example.springboot.springbootemployeemanagement.entity.User;
import cn.example.springboot.springbootemployeemanagement.repository.RoleRepository;
import cn.example.springboot.springbootemployeemanagement.repository.UserRoleRepository;
import cn.example.springboot.springbootemployeemanagement.service.UserService;
import cn.example.springboot.springbootemployeemanagement.vo.JsonResult;
import cn.example.springboot.springbootemployeemanagement.vo.UserVO;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRoleRepository userRoleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/add")
    public JsonResult<UserVO> add(@RequestBody UserVO userVO) {
        try {
            // 检查角色数据
            if (userVO.getRoles() == null || userVO.getRoles().isEmpty()) {
                return JsonResult.fail("角色不能为空");
            }

            // 转换角色名称格式
            List<String> roles = userVO.getRoles().stream()
                    .map(role -> {
                        if (role.equals("NORMAL_EMPLOYEE")) return "ROLE_NORMAL_EMPLOYEE";
                        if (role.equals("HR")) return "ROLE_HR";
                        if (role.equals("SYSTEM_ADMIN")) return "ROLE_SYSTEM_ADMIN";
                        return role;
                    })
                    .collect(Collectors.toList());
            userVO.setRoles(roles);

            User user = new User();
            user.setUsername(userVO.getUsername());
            user.setPassword(userVO.getPassword());
            
            // 保存用户和角色
            User savedUser = userService.save(user, userVO.getRoles());
            if (savedUser == null) {
                return JsonResult.fail("保存用户失败");
            }
            
            // 构建返回数据
            UserVO result = new UserVO();
            result.setId(savedUser.getId());
            result.setUsername(savedUser.getUsername());
            result.setGmtCreate(savedUser.getGmtCreate());
            result.setGmtModified(savedUser.getGmtModified());
            result.setRoles(userService.getRolesByUserId(savedUser.getId()));
            
            return JsonResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("添加用户失败：" + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public JsonResult<UserVO> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return JsonResult.error("用户不存在");
        }
        
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setGmtCreate(user.getGmtCreate());
        userVO.setGmtModified(user.getGmtModified());
        
        // 获取用户角色
        List<String> roles = userService.getRolesByUserId(user.getId());
        userVO.setRoles(roles);
        
        return JsonResult.success(userVO);
    }

    @PostMapping("/edit")
    public JsonResult<UserVO> edit(@RequestBody UserVO userVO) {
        try {
            // 检查用户是否存在
            User existingUser = userService.findById(userVO.getId());
            if (existingUser == null) {
                return JsonResult.fail("用户不存在");
            }

            // 转换角色名称格式
            if (userVO.getRoles() != null && !userVO.getRoles().isEmpty()) {
                List<String> roles = userVO.getRoles().stream()
                        .map(role -> {
                            if (role.equals("NORMAL_EMPLOYEE")) return "ROLE_NORMAL_EMPLOYEE";
                            if (role.equals("HR")) return "ROLE_HR";
                            if (role.equals("SYSTEM_ADMIN")) return "ROLE_SYSTEM_ADMIN";
                            return role;
                        })
                        .collect(Collectors.toList());
                userVO.setRoles(roles);
            }

            // 更新用户信息
            User user = new User();
            user.setId(userVO.getId());
            user.setUsername(userVO.getUsername());
            if (userVO.getPassword() != null && !userVO.getPassword().isEmpty()) {
                user.setPassword(userVO.getPassword());
            }

            // 保存用户和角色
            User updatedUser = userService.update(user, userVO.getRoles());
            if (updatedUser == null) {
                return JsonResult.fail("更新用户失败");
            }

            // 构建返回数据
            UserVO result = new UserVO();
            result.setId(updatedUser.getId());
            result.setUsername(updatedUser.getUsername());
            result.setGmtCreate(updatedUser.getGmtCreate());
            result.setGmtModified(updatedUser.getGmtModified());
            result.setRoles(userService.getRolesByUserId(updatedUser.getId()));

            return JsonResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("修改用户失败：" + e.getMessage());
        }
    }
} 