package com.example.ddd.infrastructure.iam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ddd.infrastructure.iam.dataobject.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

    UserDO selectByUsername(@Param("username") String username);
}
