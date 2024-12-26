package cn.example.springboot.springbootemployeemanagement.service;

import cn.example.springboot.springbootemployeemanagement.entity.Permission;
import org.springframework.lang.NonNull;

import java.util.List;

public interface PermissionService {
    List<Permission> getPermissions(@NonNull Long roleId);
}
