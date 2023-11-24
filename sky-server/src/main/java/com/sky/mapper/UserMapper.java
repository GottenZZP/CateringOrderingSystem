package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from user where openid = #{openId}")
    User getByOpenId(String openId);

    /**
     * 插入新用户
     *
     * @param user 用户
     */
    void insert(User user);
}