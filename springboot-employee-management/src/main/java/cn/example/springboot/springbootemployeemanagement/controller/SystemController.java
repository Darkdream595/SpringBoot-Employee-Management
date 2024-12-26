package cn.example.springboot.springbootemployeemanagement.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.example.springboot.springbootemployeemanagement.entity.Permission;
import cn.example.springboot.springbootemployeemanagement.entity.Role;
import cn.example.springboot.springbootemployeemanagement.service.RolePermissionService;
import cn.example.springboot.springbootemployeemanagement.vo.JsonResult;
import cn.example.springboot.springbootemployeemanagement.vo.PermissionVO;
import cn.example.springboot.springbootemployeemanagement.vo.RoleVO;

@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private RolePermissionService rolePermissionService;

    @GetMapping("/roles")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public JsonResult getRoles() {
        try {
            List<Role> roles = rolePermissionService.getAllRoles();
            List<RoleVO> roleVOs = roles.stream().map(role -> {
                RoleVO vo = new RoleVO();
                vo.setId(role.getId());
                vo.setName(role.getName());
                vo.setGmtCreate(role.getGmtCreate());
                vo.setGmtModified(role.getGmtModified());
                // 获取角色的权限
                List<Permission> permissions = rolePermissionService.getPermissionsByRoleId(role.getId());
                vo.setPermissions(permissions.stream().map(Permission::getName).collect(Collectors.toList()));
                return vo;
            }).collect(Collectors.toList());
            return JsonResult.success(roleVOs);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("获取角色列表失败");
        }
    }

    @GetMapping("/permissions")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public JsonResult getPermissions() {
        try {
            List<Permission> permissions = rolePermissionService.getAllPermissions();
            List<PermissionVO> permissionVOs = permissions.stream().map(permission -> {
                PermissionVO vo = new PermissionVO();
                vo.setId(permission.getId());
                vo.setName(permission.getName());
                vo.setGmtCreate(permission.getGmtCreate());
                vo.setGmtModified(permission.getGmtModified());
                return vo;
            }).collect(Collectors.toList());
            return JsonResult.success(permissionVOs);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("获取权限列表失败");
        }
    }
} 