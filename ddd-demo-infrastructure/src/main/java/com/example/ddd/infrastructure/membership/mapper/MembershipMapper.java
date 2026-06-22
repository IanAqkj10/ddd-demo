package com.example.ddd.infrastructure.membership.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ddd.infrastructure.membership.dataobject.MembershipDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MembershipMapper extends BaseMapper<MembershipDO> {

    MembershipDO selectByUserId(@Param("userId") Long userId);
}
