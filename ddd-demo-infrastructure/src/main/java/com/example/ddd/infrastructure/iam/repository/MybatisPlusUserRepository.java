package com.example.ddd.infrastructure.iam.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ddd.domain.iam.model.IamUserId;
import com.example.ddd.domain.iam.model.User;
import com.example.ddd.domain.iam.model.Username;
import com.example.ddd.domain.iam.repository.UserRepository;
import com.example.ddd.infrastructure.iam.assembler.UserDataAssembler;
import com.example.ddd.infrastructure.iam.dataobject.UserDO;
import com.example.ddd.infrastructure.iam.mapper.UserMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MybatisPlusUserRepository implements UserRepository {

    private final UserMapper userMapper;

    public MybatisPlusUserRepository(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void save(User user) {
        UserDO dataObject = UserDataAssembler.toDataObject(user);
        UserDO existing = userMapper.selectById(user.getId().getValue());
        if (existing == null) {
            userMapper.insert(dataObject);
        } else {
            userMapper.updateById(dataObject);
        }
    }

    @Override
    public User findById(IamUserId id) {
        UserDO dataObject = userMapper.selectById(id.getValue());
        return UserDataAssembler.toDomainModel(dataObject);
    }

    @Override
    public User findByUsername(Username username) {
        UserDO dataObject = userMapper.selectByUsername(username.getValue());
        return UserDataAssembler.toDomainModel(dataObject);
    }

    @Override
    public boolean existsByUsername(Username username) {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDO::getUsername, username.getValue());
        return userMapper.selectCount(queryWrapper) > 0;
    }
}
