package cn.example.springboot.springbootemployeemanagement.repository;

import cn.example.springboot.springbootemployeemanagement.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    List<Permission> getPermissionsById(Long id);
}

