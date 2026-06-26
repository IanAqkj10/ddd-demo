package com.example.ddd.infrastructure.iam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ddd.infrastructure.iam.dataobject.RoleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RoleMapper extends BaseMapper<RoleDO> {

    RoleDO selectByCode(@Param("code") String code);
}
