package cn.example.springboot.springbootemployeemanagement.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.example.springboot.springbootemployeemanagement.entity.RolePermission;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    List<RolePermission> findByRoleId(Long roleId);
    List<RolePermission> findByRoleIdIn(Collection<Long> roleIds);
}
