package com.example.demo.service.impl;

import com.example.demo.builder.BuildingSearchBuilder;
import com.example.demo.converter.BuildingDTOConverter;
import com.example.demo.converter.BuildingSearchBuilderConverter;
import com.example.demo.entity.Building;
import com.example.demo.entity.Customer;
import com.example.demo.entity.User;
import com.example.demo.enums.BuildingType;
import com.example.demo.model.dto.AssignmentDTO;
import com.example.demo.model.dto.BuildingDTO;
import com.example.demo.model.response.BuildingSearchResponse;
import com.example.demo.model.response.ResponseDTO;
import com.example.demo.model.response.StaffResponseDTO;
import com.example.demo.repository.BuildingRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.RentAreaRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class BuildingServiceImpl implements BuildingService {
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private BuildingDTOConverter buildingDTOConverter;
    @Autowired
    private BuildingSearchBuilderConverter buildingSearchBuilderConverter;
    @Autowired
    private RentAreaRepository rentAreaRepository;
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CustomerRepository customerRepository; 

    @Override
    public List <BuildingSearchResponse> findAll(Map<String, Object> params, List<String> typeCode){
        BuildingSearchBuilder buildingSearchBuilder = buildingSearchBuilderConverter.toBuildingSearchBuilder(params, typeCode);
        List < Building> buildingEntities;
     // ✅ Nếu có staffId → chỉ load tòa nhà của nhân viên đó
        if (params.containsKey("staffId")) {
            Long staffId = Long.parseLong(params.get("staffId").toString());
            buildingEntities = buildingRepository.findByStaffId(staffId);
        } else {
            buildingEntities = buildingRepository.findAll(buildingSearchBuilder);
        }
        List<BuildingSearchResponse> result = new ArrayList<>();
        for(Building b : buildingEntities){
            result.add(buildingDTOConverter.toBuildingRespone(b));
        }
        return result;
    }
    @Override
    public Page<BuildingSearchResponse> findAll(Map<String, Object> params, List<String> typeCode, Pageable pageable) {
        BuildingSearchBuilder builder = buildingSearchBuilderConverter.toBuildingSearchBuilder(params, typeCode);

        List<Building> entities;
        long total;

        if (params.containsKey("staffId")) { // STAFF → chỉ tòa nhà được giao
            Long staffId = Long.parseLong(params.get("staffId").toString());
            entities = buildingRepository.findByStaffId(staffId);
            total = entities.size(); // danh sách theo staffId đang không phân trang ở repo → đếm trực tiếp
            // cắt trang tay để đồng bộ UI (không đụng vào query cũ)
            int from = (int) pageable.getOffset();
            int to = Math.min(from + pageable.getPageSize(), entities.size());
            List<Building> paged = from > to ? new ArrayList<>() : entities.subList(from, to);
            List<BuildingSearchResponse> content = paged.stream()
                    .map(buildingDTOConverter::toBuildingRespone)
                    .toList();
            return new PageImpl<>(content, pageable, total);
        } else {
            // sử dụng repo custom mới có phân trang + count
            entities = buildingRepository.findAll(builder, pageable);              // [ADDED]
            total    = buildingRepository.countAll(builder);                       // [ADDED]
            List<BuildingSearchResponse> content = entities.stream()
                    .map(buildingDTOConverter::toBuildingRespone)
                    .toList();
            return new PageImpl<>(content, pageable, total);
        }
    }
    @Override
    public List<BuildingSearchResponse> findTop3FeaturedOrLatest() {
        var entities = buildingRepository.findTop3ByOrderByCreatedDateDesc();
        return entities.stream()
                .map(buildingDTOConverter::toBuildingRespone) // 👈 dùng đúng hàm sẵn có
                .toList();
    }

    
    @Override
    public ResponseDTO save(BuildingDTO buildingDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        Building buildingEntity;

        // 🔹 Nếu là cập nhật (đã có ID)
        if (buildingDTO.getId() != null) {
            // 1️⃣ Lấy entity cũ trong DB
            Building existingBuilding = buildingRepository.findById(buildingDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy tòa nhà có id = " + buildingDTO.getId()));

            // 2️⃣ Convert DTO sang entity mới
            buildingEntity = buildingDTOConverter.toBuildingEntity(buildingDTO);

            // 3️⃣ Giữ nguyên status cũ nếu DTO không có
            if (buildingDTO.getStatus() == null || buildingDTO.getStatus().isEmpty()) {
                buildingEntity.setStatus(existingBuilding.getStatus());
            }

            // 4️⃣ Giữ lại các quan hệ
            buildingEntity.setUserEntities(existingBuilding.getUserEntities());
            if (existingBuilding.getCustomer() != null) {
                buildingEntity.setCustomer(existingBuilding.getCustomer());
            }

            responseDTO.setMessage("Cập nhật tòa nhà thành công");

        } else {
            // 🆕 Nếu là thêm mới
            buildingEntity = buildingDTOConverter.toBuildingEntity(buildingDTO);
            responseDTO.setMessage("Thêm tòa nhà thành công");

            // ✅ Gán mặc định status nếu DTO không có
            if (buildingEntity.getStatus() == null || buildingEntity.getStatus().isEmpty()) {
                buildingEntity.setStatus("PENDING");
            }
        }

        // 🔹 Gán Customer nếu có customerId
        if (buildingDTO.getCustomerId() != null) {
            Customer customer = customerRepository.findById(buildingDTO.getCustomerId()).orElse(null);
            if (customer != null) {
                buildingEntity.setCustomer(customer);
            }
        }

        // 🔹 Nếu cập nhật: lấy lại danh sách user liên kết
        List<Building> buildingEntities = new ArrayList<>();
        buildingEntities.add(buildingEntity);
        List<User> userEntities = new ArrayList<>();
        if (buildingEntity.getId() != null) {
            userEntities = userRepository.findByBuildingEntityList(buildingEntities);
        }

        // 🔹 Lưu DB
        buildingEntity = buildingRepository.save(buildingEntity);
        buildingEntity.setUserEntities(userEntities);

        return responseDTO;
    }


    @Override
    public BuildingDTO findBuildingById(Long id) {
        Building buildingEntity = buildingRepository.findById(id).get();
        return buildingDTOConverter.toBuildingDTO(buildingEntity);
    }

    @Override
    public ResponseDTO deleteBuildings(List<Long> buildingIds) {
        rentAreaRepository.deleteBybuilding_IdIn(buildingIds);
        buildingRepository.deleteAllByIdIn(buildingIds);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("success");
        return responseDTO;
    }

    @Override
    public ResponseDTO findStaffsByBuildingId(Long buildingId) {
        Building buildingEntity = buildingRepository.findById(buildingId).get();
        List<User> staffList = userRepository.findByStatusAndRoles_Code(1, "STAFF");
        List<User> assignedStaffList = buildingEntity.getUserEntities();
        List<StaffResponseDTO> staffResponseDTOS = new ArrayList<>();
        for(User u : staffList){
            StaffResponseDTO staffResponseDTO = new StaffResponseDTO();
            staffResponseDTO.setStaffId(u.getId());
            staffResponseDTO.setFullName(u.getFullName());
            if(assignedStaffList.contains(u)){
                staffResponseDTO.setChecked("checked");
            }
            else staffResponseDTO.setChecked("");
            staffResponseDTOS.add(staffResponseDTO);
        }
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(staffResponseDTOS);
        responseDTO.setMessage("success");
        return responseDTO;
    }

    @Override
    public ResponseDTO updateAssignmentTable(AssignmentDTO assignmentBuildingDTO) {
        List<Long> staffIds = assignmentBuildingDTO.getStaffs();
        Building buildingEntity = buildingRepository.findById(assignmentBuildingDTO.getId()).get();
        List<User> userEntities = new ArrayList<>();
        for(Long id : staffIds){
            userEntities.add(userRepository.findById(id).get());
        }
        buildingEntity.setUserEntities(userEntities);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("success");
        return responseDTO;
    }
    
    @Override
    public void increaseViewCount(Long id) {
        buildingRepository.increaseViewCount(id);
    }

    @Override
    public List<Building> getRelatedBuildings(String district, Long currentId) {
        return buildingRepository.findTop4RelatedByDistrict(currentId, district);
    }
    @Override
    public List<Building> getBuildingsByGroup(BuildingType group) {
        List<String> typeCodes;
        switch (group) {
            case NOI_THAT -> typeCodes = List.of(
                "Cho thuê căn hộ chung cư",
                "Cho thuê chung cư mini, căn hộ dịch vụ (Mới)",
                "Cho thuê văn phòng",
                "Cho thuê nhà riêng"
            );
            case NGUYEN_CAN -> typeCodes = List.of(
                "Nguyên Căn",
                "Cho thuê nhà biệt thự, liền kề",
                "Cho thuê nhà trọ, phòng trọ"
            );
            case TANG_TRET -> typeCodes = List.of(
                "Tầng Trệt",
                "Cho thuê nhà mặt phố",
                "Cho thuê shophouse, nhà phố thương mại"
            );
            default -> typeCodes = Collections.emptyList();
        }
        return buildingRepository.findByTypeCodeIn(typeCodes);
    }





}
