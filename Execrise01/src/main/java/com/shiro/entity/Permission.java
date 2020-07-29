package com.shiro.entity;

import java.io.Serializable;

public class Permission implements Serializable {

    private Long id;
    //权限标识符
    private String premission;
    //权限描述
    private String description;
    //是否有效
    private Boolean available=Boolean.FALSE;

    public Permission(){

    }

    public Permission(String premission, String description, Boolean available) {
        this.premission = premission;
        this.description = description;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPremission() {
        return premission;
    }

    public void setPremission(String premission) {
        this.premission = premission;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        Permission that = (Permission) o;
        if (id!=null?!id.equals(that.id):that.id!=null)return false;
        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode():0;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", premission='" + premission + '\'' +
                ", description='" + description + '\'' +
                ", available=" + available +
                '}';
    }
}
