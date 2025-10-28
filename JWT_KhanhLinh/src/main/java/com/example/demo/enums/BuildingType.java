package com.example.demo.enums;


import java.util.*;

public enum BuildingType {
    TANG_TRET ("Tầng Trệt "),
    NGUYEN_CAN ("Nguyên Căn "),
    NOI_THAT ("Nội Thất ");

    private final String name;

    BuildingType(String name) {
        this.name = name;
    }

    public String getCode() {
        return name;
    }

    public static Map<String,String> type(){
        Map<String,String> listType = new TreeMap<>();
        for(BuildingType item : BuildingType.values()){
            listType.put(item.toString() , item.name);
        }
        return listType;
    }
    
    // ✅ Thêm phương thức map loại chi tiết sang enum
    public static BuildingType mapToGroup(String typeCode) {
        if (typeCode == null) return null;
        switch (typeCode) {
            case "Cho thuê căn hộ chung cư":
            case "Cho thuê chung cư mini, căn hộ dịch vụ (Mới)":
            case "Cho thuê văn phòng":
            case "Cho thuê nhà riêng":
                return NOI_THAT; // hoặc NOI_THAT tuỳ cách bạn muốn nhóm
            case "Cho thuê nhà biệt thự, liền kề":
            case "Cho thuê nhà trọ, phòng trọ":
                return NGUYEN_CAN;
            case "Cho thuê nhà mặt phố":
            case "Cho thuê shophouse, nhà phố thương mại":
                return TANG_TRET;
            default:
                return null;
        }
    }

}
