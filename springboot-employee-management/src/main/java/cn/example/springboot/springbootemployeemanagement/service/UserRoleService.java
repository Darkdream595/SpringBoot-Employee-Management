package cn.example.springboot.springbootemployeemanagement.service;

import cn.example.springboot.springbootemployeemanagement.entity.Role;
import cn.example.springboot.springbootemployeemanagement.entity.UserRole;
import org.springframework.lang.NonNull;

import java.util.List;

public interface UserRoleService {
    /**
     * 获取用户的所有角色关联
     * @param userId 用户ID
     * @return 用户角色关联列表
     */
    List<UserRole> getUserRoles(@NonNull Long userId);

    /**
     * 为用户添加角色
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 是否添加成功
     */
    boolean addUserRole(@NonNull Long userId, @NonNull Long roleId);

    /**
     * 获取用户的所有角色
     * @param userId 用户ID
     * @return 用户角色列表
     */
    List<Role> getRolesByUserId(@NonNull Long userId);
}