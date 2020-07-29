package com.shiro.entity;

import java.io.Serializable;
import java.util.Objects;

public class Role implements Serializable {

    private Long id;
    private String role;//用户标识
    private String description;//角色描述
    private Boolean available = Boolean.FALSE;//是否可用，如果不可用则不会添加给用户

    public Role(){}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Role(String role, String description, Boolean available) {
        this.role = role;
        this.description = description;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role1 = (Role) o;
        if (id!=null?!role1.id.equals(id):role1.id!=null)return false;
        return true;
    }

    @Override
    public int hashCode() {
        return id!=null?id.hashCode():0;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", getRole='" + description + '\'' +
                ", available=" + available +
                '}';
    }
}
