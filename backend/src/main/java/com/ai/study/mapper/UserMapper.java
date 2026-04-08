package com.ai.study.mapper;

import com.ai.study.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    User findByUsername(@Param("username") String username);

    User findByNickname(@Param("nickname") String nickname);

    int insert(User user);
}
