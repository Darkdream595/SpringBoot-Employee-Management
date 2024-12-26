package cn.example.springboot.springbootemployeemanagement.service.impl;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import cn.example.springboot.springbootemployeemanagement.entity.Role;
import cn.example.springboot.springbootemployeemanagement.repository.RoleRepository;
import cn.example.springboot.springbootemployeemanagement.service.RoleService;
import jakarta.annotation.PostConstruct;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> getRoles(@NonNull Long id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.map(Collections::singletonList).orElse(Collections.emptyList());
    }

    @PostConstruct
    public void initRoles() {
        // 检查并创建默认角色
        createRoleIfNotExists("ROLE_NORMAL_EMPLOYEE");
        createRoleIfNotExists("ROLE_HR");
        createRoleIfNotExists("ROLE_SYSTEM_ADMIN");
    }

    private void createRoleIfNotExists(String roleName) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = new Role();
            role.setName(roleName);
            Instant now = Instant.now();
            role.setGmtCreate(now);
            role.setGmtModified(now);
            roleRepository.save(role);
        }
    }
}
