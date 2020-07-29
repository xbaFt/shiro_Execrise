package com.shiro.dao;

import com.shiro.entity.User;

import java.util.Set;

/**
 * 创建用户
 * 删除用户
 * 关联用户-角色
 * 解除用户权限关联
 */
public interface UserDao {

    public User createUser(User user);
    public void updateUser(User user);
    public void deleteUser(Long userId);

    public void correlationRoles(Long userId,Long ... roleIds);
    public void uncorrelationRoles(Long userId,Long...roleIds);

    public User findOne(Long userId);
    public User findByUsername(String userName);
    public Set<String> findRoles(String username);
    public Set<String> findPermission(String username);
}
