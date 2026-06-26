package com.example.ddd.infrastructure.iam.gateway;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.example.ddd.application.iam.port.IdGenerator;

public class SnowflakeIdGenerator implements IdGenerator {

    @Override
    public Long nextId() {
        return IdWorker.getId();
    }
}
