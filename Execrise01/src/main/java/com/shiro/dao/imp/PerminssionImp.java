package com.shiro.dao.imp;

import com.shiro.JdbcTemplateUtils;
import com.shiro.dao.PermissionDao;
import com.shiro.entity.Permission;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//创建权限
public class PerminssionImp implements PermissionDao {

    JdbcTemplate jdbcTemplate = JdbcTemplateUtils.getJdbcTemplate();
    @Override
    public Permission createPermission(Permission permission) {
        if (permission==null)return null;
        String sql = "insert into sys_permissions(permission, description, available) values(?,?,?)";
        //设置主键
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        //更新并拿到主键值设置到permission中
        jdbcTemplate.update(sql, new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(sql,new String[]{"id"});
                preparedStatement.setString(1,permission.getPremission());
                preparedStatement.setString(2,permission.getDescription());
                preparedStatement.setBoolean(3,permission.getAvailable());
                return  preparedStatement;
            }
        },keyHolder);
        permission.setId(keyHolder.getKey().longValue());
        return permission;
    }
//删除权限
    @Override
    public void deletePermission(Long permissionId) {
        if (permissionId==null) return;
        //删除相关表数据
        String sql = "delete from sys_roles_premissions where permission_id=?";
        jdbcTemplate.update(sql,permissionId);

        sql = "delete from where sys_premissions where premission_id = ?";
        jdbcTemplate.update(sql,permissionId);
    }
}
