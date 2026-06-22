package com.example.ddd.infrastructure.membership.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ddd.domain.membership.model.Membership;
import com.example.ddd.domain.membership.model.MembershipId;
import com.example.ddd.domain.membership.repository.MembershipRepository;
import com.example.ddd.domain.order.model.UserId;
import com.example.ddd.infrastructure.membership.assembler.MembershipDataAssembler;
import com.example.ddd.infrastructure.membership.dataobject.MembershipDO;
import com.example.ddd.infrastructure.membership.mapper.MembershipMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MybatisPlusMembershipRepository implements MembershipRepository {

    private final MembershipMapper membershipMapper;

    public MybatisPlusMembershipRepository(MembershipMapper membershipMapper) {
        this.membershipMapper = membershipMapper;
    }

    @Override
    public void save(Membership membership) {
        MembershipDO dataObject = MembershipDataAssembler.toDataObject(membership);
        MembershipDO existing = membershipMapper.selectById(membership.getId().getValue());

        if (existing == null) {
            membershipMapper.insert(dataObject);
        } else {
            membershipMapper.updateById(dataObject);
        }
    }

    @Override
    public Membership findById(MembershipId id) {
        MembershipDO dataObject = membershipMapper.selectById(id.getValue());
        return MembershipDataAssembler.toDomainModel(dataObject);
    }

    @Override
    public Membership findByUserId(UserId userId) {
        MembershipDO dataObject = membershipMapper.selectByUserId(userId.getValue());
        return MembershipDataAssembler.toDomainModel(dataObject);
    }

    @Override
    public boolean existsByUserId(UserId userId) {
        LambdaQueryWrapper<MembershipDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MembershipDO::getUserId, userId.getValue());
        return membershipMapper.selectCount(queryWrapper) > 0;
    }
}
