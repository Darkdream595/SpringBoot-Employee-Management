package cn.example.springboot.springbootemployeemanagement.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.example.springboot.springbootemployeemanagement.entity.Role;
import cn.example.springboot.springbootemployeemanagement.entity.User;
import cn.example.springboot.springbootemployeemanagement.entity.UserRole;
import cn.example.springboot.springbootemployeemanagement.repository.RoleRepository;
import cn.example.springboot.springbootemployeemanagement.repository.UserRepository;
import cn.example.springboot.springbootemployeemanagement.repository.UserRoleRepository;
import cn.example.springboot.springbootemployeemanagement.service.UserService;
import cn.example.springboot.springbootemployeemanagement.vo.UserVO;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserVO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            UserVO vo = new UserVO();
            vo.setId(user.getId());
            vo.setUsername(user.getUsername());
            vo.setGmtCreate(user.getGmtCreate());
            vo.setGmtModified(user.getGmtModified());
            vo.setRoles(getRolesByUserId(user.getId()));
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public User getById(@NonNull Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Optional<User> getByUsername(@NonNull String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public boolean create(@NonNull UserVO userVO) {
        try {
            User user = new User();
            user.setUsername(userVO.getUsername());
            user.setPassword(passwordEncoder.encode(userVO.getPassword()));
            user.setGmtCreate(Instant.now());
            user.setGmtModified(Instant.now());
            User savedUser = userRepository.save(user);

            if (userVO.getRoles() != null && !userVO.getRoles().isEmpty()) {
                List<Role> roles = roleRepository.findByNameIn(userVO.getRoles());
                for (Role role : roles) {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(savedUser.getId());
                    userRole.setRoleId(role.getId());
                    userRole.setGmtCreate(Instant.now());
                    userRole.setGmtModified(Instant.now());
                    userRoleRepository.save(userRole);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public User save(User user, List<String> roles) {
        try {
            // 设置创建时间和修改时间
            Instant now = Instant.now();
            user.setGmtCreate(now);
            user.setGmtModified(now);
            
            // 加密密码
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            
            // 保存用户
            User savedUser = userRepository.save(user);
            
            // 保存用户角色关系
            if (roles != null && !roles.isEmpty()) {
                List<Role> roleEntities = roleRepository.findByNameIn(roles);
                if (roleEntities.isEmpty()) {
                    throw new RuntimeException("未找到指定的角色");
                }
                
                for (Role role : roleEntities) {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(savedUser.getId());
                    userRole.setRoleId(role.getId());
                    userRole.setGmtCreate(now);
                    userRole.setGmtModified(now);
                    userRoleRepository.save(userRole);
                }
            }
            
            return savedUser;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    @Transactional
    public User update(User user, List<String> roles) {
        User existingUser = userRepository.findById(user.getId()).orElse(null);
        if (existingUser == null) {
            return null;
        }

        // 更新基本信息
        existingUser.setUsername(user.getUsername());
        existingUser.setGmtModified(Instant.now());
        User updatedUser = userRepository.save(existingUser);

        // 更新角色
        if (roles != null) {
            // 删除原有角色
            userRoleRepository.deleteByUserId(user.getId());
            
            // 添加新角色
            List<Role> roleEntities = roleRepository.findByNameIn(roles);
            for (Role role : roleEntities) {
                UserRole userRole = new UserRole();
                userRole.setUserId(updatedUser.getId());
                userRole.setRoleId(role.getId());
                userRole.setGmtCreate(Instant.now());
                userRole.setGmtModified(Instant.now());
                userRoleRepository.save(userRole);
            }
        }

        return updatedUser;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // 删除用户角色关系
        userRoleRepository.deleteByUserId(id);
        // 删除用户
        userRepository.deleteById(id);
    }

    @Override
    public List<String> getRolesByUserId(Long userId) {
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
        List<Long> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
        List<Role> roles = roleRepository.findAllById(roleIds);
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean edit(@NonNull UserVO userVO) {
        try {
            User existingUser = userRepository.findById(userVO.getId()).orElse(null);
            if (existingUser == null) {
                return false;
            }

            existingUser.setUsername(userVO.getUsername());
            if (userVO.getPassword() != null && !userVO.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(userVO.getPassword()));
            }
            existingUser.setGmtModified(Instant.now());
            userRepository.save(existingUser);

            // 更新用户角色
            if (userVO.getRoles() != null && !userVO.getRoles().isEmpty()) {
                userRoleRepository.deleteByUserId(userVO.getId());
                List<Role> roles = roleRepository.findByNameIn(userVO.getRoles());
                for (Role role : roles) {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(existingUser.getId());
                    userRole.setRoleId(role.getId());
                    userRole.setGmtCreate(Instant.now());
                    userRole.setGmtModified(Instant.now());
                    userRoleRepository.save(userRole);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteUser(@NonNull Long id) {
        try {
            // 先删除用户角色关联
            userRoleRepository.deleteByUserId(id);
            // 再删除用户
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<String> getRoles(Long userId) {
        return getRolesByUserId(userId);
    }
}