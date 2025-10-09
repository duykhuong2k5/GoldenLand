package com.javaweb.repository.custom;

import com.javaweb.entity.UserEntity;
import java.util.List;

public interface UserRepositoryImpl {
    List<UserEntity> searchCustom(String keyword);
}
