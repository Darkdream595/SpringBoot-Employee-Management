package cn.example.springboot.springbootemployeemanagement.service.impl;

import cn.example.springboot.springbootemployeemanagement.entity.Permission;
import cn.example.springboot.springbootemployeemanagement.repository.PermissionRepository;
import cn.example.springboot.springbootemployeemanagement.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public List<Permission> getPermissions(Long roleId) {
        return permissionRepository.getPermissionsById(roleId);
    }
}
