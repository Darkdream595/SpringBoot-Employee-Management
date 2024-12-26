package cn.example.springboot.springbootemployeemanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;

import cn.example.springboot.springbootemployeemanagement.entity.User;
import cn.example.springboot.springbootemployeemanagement.vo.UserVO;

public interface UserService {
    /**
     * 获取所有用户列表
     * @return 用户VO列表
     */
    List<UserVO> findAll();

    /**
     * 通过ID获取用户
     * @param id 用户ID
     * @return 用户实体
     */
    User getById(@NonNull Long id);

    /**
     * 通过用户名获取用户
     * @param username 用户名
     * @return 用户实体
     */
    Optional<User> getByUsername(@NonNull String username);

    /**
     * 创建用户
     * @param userVO 用户VO
     * @return 是否创建成功
     */
    boolean create(@NonNull UserVO userVO);

    /**
     * 保存用户
     * @param user 用户实体
     * @param roles 角色列表
     * @return 保存后的用户实体
     */
    User save(User user, List<String> roles);

    /**
     * 更新用户
     * @param user 用户实体
     * @param roles 角色列表
     * @return 更新后的用户实体
     */
    User update(User user, List<String> roles);

    /**
     * 删除用户
     * @param id 用户ID
     */
    void delete(Long id);

    /**
     * 获取用户的角色列表
     * @param userId 用户ID
     * @return 角色名称列表
     */
    List<String> getRolesByUserId(Long userId);

    /**
     * 编辑用户
     * @param userVO 用户信息
     * @return 是否编辑成功
     */
    boolean edit(@NonNull UserVO userVO);

    /**
     * 删除用户
     * @param id 用户ID
     * @return 是否删除成功
     */
    boolean deleteUser(@NonNull Long id);

    User findById(Long id);

    List<String> getRoles(Long userId);
}
