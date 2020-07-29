package com.shiro.dao.imp;

import com.shiro.JdbcTemplateUtils;
import com.shiro.dao.RoleDao;
import com.shiro.entity.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RolesImp implements RoleDao {

    private JdbcTemplate jdbcTemplateUtils = JdbcTemplateUtils.getJdbcTemplate();
    @Override
    public Role createRole(Role role) {

        String sql="insert into sys_Roles(role, description, available) values(?,?,?)";
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplateUtils.update( new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(sql,new String[]{"id"});
                preparedStatement.setString(1,role.getRole());
                preparedStatement.setString(2,role.getDescription());
                preparedStatement.setBoolean(3,role.getAvailable());
                return preparedStatement;
            }
        },generatedKeyHolder);
        role.setId(generatedKeyHolder.getKey().longValue());
        return role;
    }

    @Override
    public void deleteRole(Long id) {

        //先删除role关联的表数据
        String sql = "delete from sys_users_roles where role_id=?";
        jdbcTemplateUtils.update(sql,id);

        sql = "delete from sys_roles where id =?";
        jdbcTemplateUtils.update(sql,id);
    }

    //用户关联角色
    @Override
    public void correlationPerissions(Long roleId, Long... permissionIds) {
        if (roleId==null||permissionIds==null)return;
        String sql = "insert into sys_users_perimssion(role_id,permission_id) values(?,?)";
        for (Long permissionId:permissionIds){
            if (!exists(roleId, permissionId)) {
                jdbcTemplateUtils.update(sql,roleId,permissionId);
            }
        }
    }

    //解除用户所关联的角色
    @Override
    public void uncorrelationPerissions(Long roleId, Long... permissionIds) {
        if (roleId==null||permissionIds==null)return;
        String sql = "delete from sys_users_permission where role_id = ? and permission=?";
        for (Long perminssionId:permissionIds){
            if (exists(roleId, perminssionId)) {
                jdbcTemplateUtils.update(sql,roleId,perminssionId);
            }
        }
    }

    public Boolean exists(Long roleId,Long permissionId){
        String sql = "select count(1) from sys_roles_permissions where role_id = ? and permission_id=?";
        return jdbcTemplateUtils.queryForObject(sql,Integer.class,roleId,permissionId)!=0;
    }
}
