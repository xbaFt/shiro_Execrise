package com.shiro.dao;

import com.shiro.entity.Permission;

public interface PermissionDao {
    public Permission createPermission(Permission permission);
    public void deletePermission(Long id);
}
