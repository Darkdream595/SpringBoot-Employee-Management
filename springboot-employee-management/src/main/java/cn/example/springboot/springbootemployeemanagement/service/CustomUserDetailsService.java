package cn.example.springboot.springbootemployeemanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cn.example.springboot.springbootemployeemanagement.entity.Permission;
import cn.example.springboot.springbootemployeemanagement.entity.Role;
import cn.example.springboot.springbootemployeemanagement.entity.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RolePermissionService rolePermissionService;

    /**
     * 加载用户信息并构建 UserDetails 对象
     * 这个方法在用户登录时被调用
     * 主要完成以下工作：
     * 1. 根据用户名查找用户
     * 2. 获取用户的角色和权限
     * 3. 构建 UserDetails 对象返回
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Loading user details for username: " + username);

        Optional<User> userOptional = userService.getByUsername(username);
        if (userOptional.isEmpty()) {
            System.out.println("User not found with username: " + username);
            throw new UsernameNotFoundException("User not found: " + username);
        }

        User user = userOptional.get();
        System.out.println("Found user: id=" + user.getId() + ", username=" + user.getUsername());

        // 直接获取用户的角色列表
        List<Role> roles = userRoleService.getRolesByUserId(user.getId());
        System.out.println("Found " + roles.size() + " roles for user " + user.getUsername());
        roles.forEach(role -> System.out.println("Role: id=" + role.getId() + ", name=" + role.getName()));

        // 批量获取所有角色对应的权限
        List<Permission> permissions = rolePermissionService.getPermissionsByRoleIds(
                roles.stream().map(Role::getId).toList());
        System.out.println("Found " + permissions.size() + " unique permissions for user " + user.getUsername());
        permissions.forEach(permission ->
                System.out.println("Permission: id=" + permission.getId() + ", name=" + permission.getName()));

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles.size() + permissions.size());

        // 转换角色为权限
        List<SimpleGrantedAuthority> roleAuthorities = roles.stream()
                .map(role -> {
                    String roleName = role.getName().startsWith("ROLE_") ?
                            role.getName() : "ROLE_" + role.getName();
                    System.out.println("Converting role: " + role.getName() + " -> " + roleName);
                    return roleName;
                })
                .map(SimpleGrantedAuthority::new)
                .toList();

        // 转换权限
        List<SimpleGrantedAuthority> permissionAuthorities = permissions.stream()
                .map(permission -> {
                    System.out.println("Converting permission: " + permission.getName());
                    return permission.getName();
                })
                .map(SimpleGrantedAuthority::new)
                .toList();

        grantedAuthorities.addAll(roleAuthorities);
        grantedAuthorities.addAll(permissionAuthorities);
        System.out.println("Total authorities granted to user " + user.getUsername() + ": " + grantedAuthorities.size());

        // 构建并返回 UserDetails 对象
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(grantedAuthorities)
                .build();
    }
}
