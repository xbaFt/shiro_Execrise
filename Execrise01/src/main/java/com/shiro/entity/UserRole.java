package com.shiro.entity;

import java.util.Objects;

public class UserRole {

    private Long userId;
    private Long roleId;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        if (userId!=null?!this.userId.equals(userRole.userId):userRole.userId!=null) return false;
        if (roleId!=null?!this.roleId.equals(userRole.roleId):userRole.roleId!=null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null?userId.hashCode():0;
        result = 31*result+(roleId!=null?roleId.hashCode():0);
        return result;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "userId=" + userId +
                ", roleId=" + roleId +
                '}';
    }
}
