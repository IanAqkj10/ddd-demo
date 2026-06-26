package com.example.ddd.infrastructure.iam.assembler;

import com.example.ddd.domain.iam.model.HashedPassword;
import com.example.ddd.domain.iam.model.IamUserId;
import com.example.ddd.domain.iam.model.RoleId;
import com.example.ddd.domain.iam.model.User;
import com.example.ddd.domain.iam.model.UserStatus;
import com.example.ddd.domain.iam.model.Username;
import com.example.ddd.infrastructure.iam.dataobject.UserDO;
import java.util.LinkedHashSet;
import java.util.Set;

public class UserDataAssembler {

    public static UserDO toDataObject(User user) {
        if (user == null) {
            return null;
        }
        UserDO dataObject = new UserDO();
        dataObject.setUserId(user.getId().getValue());
        dataObject.setUsername(user.getUsername().getValue());
        dataObject.setPasswordHash(user.getPassword().getHash());
        dataObject.setPasswordSalt(user.getPassword().getSalt());
        dataObject.setStatus(user.getStatus().name());
        dataObject.setRoleIds(joinRoleIds(user.getRoleIds()));
        dataObject.setCreatedAt(user.getCreatedAt());
        dataObject.setLastUpdatedAt(user.getLastUpdatedAt());
        return dataObject;
    }

    public static User toDomainModel(UserDO dataObject) {
        if (dataObject == null) {
            return null;
        }
        return User.restore(
            IamUserId.of(dataObject.getUserId()),
            Username.of(dataObject.getUsername()),
            HashedPassword.of(dataObject.getPasswordHash(), dataObject.getPasswordSalt()),
            UserStatus.valueOf(dataObject.getStatus()),
            parseRoleIds(dataObject.getRoleIds()),
            dataObject.getCreatedAt(),
            dataObject.getLastUpdatedAt()
        );
    }

    private static String joinRoleIds(Set<RoleId> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (RoleId roleId : roleIds) {
            if (builder.length() > 0) {
                builder.append(',');
            }
            builder.append(roleId.getValue());
        }
        return builder.toString();
    }

    private static Set<RoleId> parseRoleIds(String raw) {
        Set<RoleId> result = new LinkedHashSet<>();
        if (raw == null || raw.trim().isEmpty()) {
            return result;
        }
        for (String part : raw.split(",")) {
            String trimmed = part.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            result.add(RoleId.of(Long.parseLong(trimmed)));
        }
        return result;
    }
}
