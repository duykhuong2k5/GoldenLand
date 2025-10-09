package com.javaweb.repository.custom;

import com.javaweb.entity.BuildingEntity;
import java.util.List;

public interface BuildingRepositoryImpl {
    List<BuildingEntity> searchCustom(String keyword);
}
