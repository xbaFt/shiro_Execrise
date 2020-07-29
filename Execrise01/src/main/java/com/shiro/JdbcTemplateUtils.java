package com.shiro;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTemplateUtils {

    private static JdbcTemplate jdbcTemplate;

    public static JdbcTemplate getJdbcTemplate(){
        return jdbcTemplate==null? jdbcTemplate=createJdbcTemplate():jdbcTemplate;
    }

    public static JdbcTemplate createJdbcTemplate(){

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql//localhost:3306/shiro");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");

        return new JdbcTemplate(dataSource);
    }
}
