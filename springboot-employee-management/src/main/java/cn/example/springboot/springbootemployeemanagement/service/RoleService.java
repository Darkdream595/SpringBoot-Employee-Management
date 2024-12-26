package cn.example.springboot.springbootemployeemanagement.service;

import java.util.List;

import org.springframework.lang.NonNull;

import cn.example.springboot.springbootemployeemanagement.entity.Role;

public interface RoleService {
    /**
     * 通过ID获取角色
     * @param id 角色ID
     * @return 角色列表
     */
    List<Role> getRoles(@NonNull Long id);

    /**
     * 获取所有角色
     * @return 角色列表
     */
    List<Role> findAll();
}
