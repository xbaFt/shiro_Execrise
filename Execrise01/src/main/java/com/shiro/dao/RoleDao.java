package com.shiro.dao;

import com.shiro.entity.Role;

/**
 * 创建角色
 * 删除角色
 * 关联角色权限
 * 解除角色权限
 */
public interface RoleDao {

    public Role createRole(Role role);
    public void deleteRole(Long id);

    public void correlationPerissions(Long roleId, Long... permissionIds);
    public void uncorrelationPerissions(Long roleId, Long... permissionIds);
}
