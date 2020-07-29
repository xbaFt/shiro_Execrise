package com.shiro.dao.imp;

import com.shiro.JdbcTemplateUtils;
import com.shiro.dao.UserDao;
import com.shiro.entity.User;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Set;

public class UserImp implements UserDao {

    private JdbcTemplate jdbcTemplate = JdbcTemplateUtils.getJdbcTemplate();

    public User createUser(final com.shiro.entity.User user) {

        final String sql = "insert into sys_users(username,password,salt,locked) values(?,?,?,?)";
        //插入并返回主键值
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((PreparedStatementCreator) (connection)->{
            PreparedStatement preparedStatement = connection.prepareStatement(sql,new String[]{"id"});
            preparedStatement.setString(1,user.getUsername());
            preparedStatement.setString(2,user.getPassword());
            preparedStatement.setString(3,user.getSalt());
            preparedStatement.setBoolean(4,user.getLocked());
            return preparedStatement;
        },generatedKeyHolder);
        user.setId(generatedKeyHolder.getKey().longValue());
        return user;
    }

    public void updateUser(User user) {
        String sql = "update sys_users set username=?,password=?,salt=?,locked=? where id = ?";
        jdbcTemplate.update(sql,user.getUsername(),user.getPassword(),user.getSalt(),user.getLocked(),user.getId());
    }

    public void deleteUser(Long userId) {
        String sql="delete sys_users where id = ?";
        jdbcTemplate.update(sql,userId);
    }

    public void correlationRoles(Long userId, Long... roleIds) {
        if (userId==null||roleIds==null) return;
        String sql = "insert into sys_users_roles(user_id,role_id) values(?,?)";
        for (Long roleId:roleIds){
            //判断数据库中是否存在用户
            if (!exeists(userId,roleId)) {
                jdbcTemplate.update(sql, userId, roleId);
            }
        }
    }

    public void uncorrelationRoles(Long userId, Long... roleIds) {
        if (userId==null||roleIds==null) return;
        String sql = "delete sys_users_roles where userId = ? and roleId = ?";
        for(Long roleId:roleIds){
            if (exeists(userId, roleId)) {
                jdbcTemplate.update(sql,userId,roleId);
            }
        }
    }

    //查询用户第一个角色
    public User findOne(Long userId) {
        String sql = "select * from sys_users where userid=?";
        List<User> user = jdbcTemplate.query(sql,new BeanPropertyRowMapper(User.class),userId);
        if(user.size()==0)return null;
        return user.get(0);
    }

    //根据用户名查用户第一条信息
    public User findByUsername(String userName) {
       String sql="select * from sys_users where username=?";
       List<User> users = jdbcTemplate.query(sql,new BeanPropertyRowMapper(User.class),userName);
       if(users.size()==0) return null;
       return users.get(0);
    }

    public Set<String> findRoles(String username) {
        return null;
    }

    public Set<String> findPermission(String username) {
        return null;
    }
    //判断数据库中是否存在用户
    public Boolean exeists(Long userId,Long roleId){
        final String sql =  "select count(1) from sys_users_roles where userid=? and roleid=?";
        return jdbcTemplate.queryForObject(sql,Integer.class ,userId,roleId)!=null;
    }
}
