package com.zzy.webtest.mapper;

import com.zzy.webtest.enity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {

    @Select("SELECT * FROM user WHERE username = #{username} and password = #{password}")
    public User getUser(@Param("username") String username, @Param("password") String password);


}
