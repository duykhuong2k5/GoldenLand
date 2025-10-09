package com.javaweb.repository.custom;

import com.javaweb.entity.RoleEntity;
import java.util.List;

public interface RoleRepositoryImpl {
    List<RoleEntity> searchCustom(String keyword);
}
