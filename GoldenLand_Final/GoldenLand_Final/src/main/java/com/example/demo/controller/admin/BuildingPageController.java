package com.example.demo.controller.admin;

import com.example.demo.constant.SystemConstant;
import com.example.demo.enums.BuildingType;
import com.example.demo.enums.DistrictCode;
import com.example.demo.model.dto.BuildingDTO;
import com.example.demo.model.request.BuildingSearchRequest;
import com.example.demo.model.response.BuildingSearchResponse;
import com.example.demo.security.utils.SecurityUtils;
import com.example.demo.service.BuildingService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class BuildingPageController {

    @Autowired private UserService userService;
    @Autowired private BuildingService buildingService;

    @GetMapping("/building-list")
    public ModelAndView buildingList(
            @ModelAttribute BuildingSearchRequest buildingSearchRequest,
            @RequestParam Map<String, Object> conditions,
            @RequestParam(name = "typeCode", required = false) List<String> typeCode,
            @ModelAttribute(SystemConstant.MODEL) BuildingDTO model,
            HttpServletRequest request,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Long currentUserId = SecurityUtils.getPrincipal().getId();
        List<String> roles = SecurityUtils.getAuthorities();
        if (roles.contains(SystemConstant.STAFF_ROLE)) {
            conditions.put("staffId", currentUserId);
        }

        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1));

        // ✅ LẤY PAGE ĐÚNG CHUẨN
        Page<BuildingSearchResponse> buildingPage =
                buildingService.findAll(conditions, typeCode, pageable);

        ModelAndView mav = new ModelAndView("admin/building/list");
        mav.addObject("modelSearch", buildingSearchRequest);

        // Bảng dùng list, phân trang dùng Page
        mav.addObject("buildingList", buildingPage.getContent());
        mav.addObject("buildingPage", buildingPage);

        if (roles.contains(SystemConstant.MANAGER_ROLE) || roles.contains(SystemConstant.ADMIN_ROLE)) {
            mav.addObject("staffs", userService.getStaffs());
        }
        mav.addObject("districts", DistrictCode.type());
        mav.addObject("typeCodes", BuildingType.type());

        // (tuỳ bạn còn dùng 2 field này không)
        model.setMaxPageItems(size);
        model.setTotalItem((int) buildingPage.getTotalElements());

        return mav;
    }

    @GetMapping("/building-edit")
    public ModelAndView buildingEdit(@ModelAttribute BuildingDTO buildingDTO, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("admin/building/edit");
        mav.addObject("buildingEdit", buildingDTO);
        mav.addObject("districts", DistrictCode.type());
        mav.addObject("typeCodes", BuildingType.type());
        return mav;
    }

    @GetMapping("/building-edit-{id}")
    public ModelAndView buildingEdit(@PathVariable("id") Long id, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("admin/building/edit");
        mav.addObject("buildingEdit", buildingService.findBuildingById(id));
        mav.addObject("districts", DistrictCode.type());
        mav.addObject("typeCodes", BuildingType.type());
        return mav;
    }
}
