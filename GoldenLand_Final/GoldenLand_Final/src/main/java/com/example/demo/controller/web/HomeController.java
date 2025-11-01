package com.example.demo.controller.web;

import com.example.demo.converter.BuildingDTOConverter;
import com.example.demo.entity.Building;
import com.example.demo.enums.BuildingType;
import com.example.demo.model.request.BuildingSearchRequest;
import com.example.demo.model.response.BuildingSearchResponse;
import com.example.demo.security.utils.SecurityUtils;
import com.example.demo.service.BuildingService;
import com.example.demo.utils.DistrictCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(value = "homeControllerOfWeb")
public class HomeController {
	
	@Autowired
    private BuildingService buildingService;
	@Autowired
	private BuildingDTOConverter buildingDTOConverter;


	// 🏠 Trang chủ
    @RequestMapping(value = "/trang-chu", method = RequestMethod.GET)
    public ModelAndView homePage(BuildingSearchRequest buildingSearchRequest, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("web/home");
        mav.addObject("modelSearch", buildingSearchRequest);
        mav.addObject("districts", DistrictCode.type());

     // 👉 NẠP 3 TÒA NỔI BẬT CHO TRANG CHỦ
        var featured = buildingService.findTop3FeaturedOrLatest();
        mav.addObject("featuredBuildings", featured);
        // ⚙️ Nếu form "Tìm kiếm nhanh" có dữ liệu
        Map<String, Object> params = new HashMap<>();

        if (buildingSearchRequest.getDistrict() != null && !buildingSearchRequest.getDistrict().isBlank()) {
            params.put("district", buildingSearchRequest.getDistrict());
        }
        if (buildingSearchRequest.getTypeCode() != null && !buildingSearchRequest.getTypeCode().isEmpty()) {
            params.put("typeCode", buildingSearchRequest.getTypeCode());
        }
        if (buildingSearchRequest.getName() != null && !buildingSearchRequest.getName().isBlank()) {
            params.put("name", buildingSearchRequest.getName());
        }

        // ✅ Nếu có điều kiện tìm kiếm, gọi service để lấy danh sách
        if (!params.isEmpty()) {
            List<BuildingSearchResponse> buildings = buildingService.findAll(params, buildingSearchRequest.getTypeCode());
            mav.addObject("projects", buildings);
        }

        return mav;
    }


    // 📘 Giới thiệu
    @GetMapping("/gioi-thieu")
    public ModelAndView introducePage() {
        return new ModelAndView("web/introduce");
    }

 // 🏗️ Sản phẩm (danh sách dự án / tòa nhà)
    @GetMapping("/san-pham")
    public ModelAndView buildingList(BuildingSearchRequest req,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "9") int size) {
        ModelAndView mav = new ModelAndView("web/list");

        Map<String, Object> params = new HashMap<>();
        if (req.getName() != null && !req.getName().isEmpty()) params.put("name", req.getName());
        if (req.getDistrict() != null && !req.getDistrict().isEmpty()) params.put("district", req.getDistrict());
        if (req.getWard() != null && !req.getWard().isEmpty()) params.put("ward", req.getWard());
        if (req.getStreet() != null && !req.getStreet().isEmpty()) params.put("street", req.getStreet());
        if (req.getDirection() != null && !req.getDirection().isEmpty()) params.put("direction", req.getDirection());
        if (req.getNumberOfBasement() != null) params.put("numberOfBasement", req.getNumberOfBasement());
        if (req.getFloorArea() != null) params.put("floorArea", req.getFloorArea());
        if (req.getRentPriceFrom() != null) params.put("rentPriceFrom", req.getRentPriceFrom());
        if (req.getRentPriceTo() != null) params.put("rentPriceTo", req.getRentPriceTo());

        // 1) Lấy toàn bộ qua service cũ
        List<BuildingSearchResponse> all = buildingService.findAll(params, req.getTypeCode());

        // 2) Cắt trang thủ công
        int from = Math.max(0, page * size);
        int to   = Math.min(all.size(), from + size);
        List<BuildingSearchResponse> slice = (from > to) ? List.of() : all.subList(from, to);

        Page<BuildingSearchResponse> buildingPage =
                new PageImpl<>(slice, PageRequest.of(page, size), all.size());

        // 3) Trả ra view
        mav.addObject("projects", slice);          // UI đang dùng để render card
        mav.addObject("buildingPage", buildingPage); // dùng vẽ pagination
        mav.addObject("search", req);
        mav.addObject("districts", DistrictCode.type());
        mav.addObject("typeCodes", BuildingType.type());
        mav.addObject("selectedType", req.getTypeCode());
        // 🧩 THÊM DÒNG NÀY: lấy id khách hàng hiện tại (nếu có)
        Long currentCustomerId = null;
        try {
            var principal = SecurityUtils.getPrincipal();
            if (principal != null && principal.getId() != null) {
                currentCustomerId = principal.getId();
            }
        } catch (Exception ignored) {}

        mav.addObject("currentCustomerId", currentCustomerId);
        return mav;
    }

    
 // 🏢 Trang chi tiết tòa nhà
    @GetMapping("/chi-tiet-toa-nha/{id}")
    public ModelAndView buildingDetail(@PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("web/detail");

        // 🔹 1. Tăng lượt xem
        buildingService.increaseViewCount(id);

        // 🔹 2. Lấy thông tin tòa nhà chi tiết
        var building = buildingService.findBuildingById(id);

        // 🔹 3. Lấy danh sách tòa nhà tương tự
        var related = buildingService.getRelatedBuildings(building.getDistrict(), id);

        // 🔹 4. Truyền sang view
        mav.addObject("building", building);
        mav.addObject("related", related);
        return mav;
    }



    // 📰 Tin tức
    @GetMapping("/tin-tuc")
    public ModelAndView newsPage() {
        return new ModelAndView("web/news");
    }

    // 📞 Liên hệ
    @GetMapping("/lien-he")
    public ModelAndView contactPage() {
        return new ModelAndView("web/contact");
    }

    // 🔐 Đăng nhập
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    // 🚫 Truy cập bị từ chối
    @RequestMapping(value = "/access-denied", method = RequestMethod.GET)
    public ModelAndView accessDenied() {
        return new ModelAndView("redirect:/login?accessDenied");
    }

    // 🚪 Đăng xuất
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new ModelAndView("redirect:/trang-chu");
    }
    @GetMapping("/homes/type/{typeCode}")
    public ModelAndView showBuildingsByType(@PathVariable String typeCode,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "9") int size) {
        ModelAndView mav = new ModelAndView("web/list");
        String decoded = java.net.URLDecoder.decode(typeCode, java.nio.charset.StandardCharsets.UTF_8);
        BuildingType group = BuildingType.mapToGroup(decoded);

        List<BuildingSearchResponse> all = new ArrayList<>();
        if (group != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("typeCode", group.name());
            all = buildingService.findAll(params, List.of(group.name()));
        }

        int from = Math.max(0, page * size);
        int to   = Math.min(all.size(), from + size);
        List<BuildingSearchResponse> slice = (from > to) ? List.of() : all.subList(from, to);

        Page<BuildingSearchResponse> buildingPage =
                new PageImpl<>(slice, PageRequest.of(page, size), all.size());

        mav.addObject("projects", slice);
        mav.addObject("buildingPage", buildingPage);
        mav.addObject("selectedType", decoded);
        mav.addObject("districts", DistrictCode.type());
        mav.addObject("typeCodes", BuildingType.type());
        mav.addObject("search", new BuildingSearchRequest());
        return mav;
    }

    
 // 🏙️ Danh sách tòa nhà theo địa điểm (TP.HCM, Hà Nội, Đà Nẵng)
    @GetMapping("/dia-diem/{city}")
    public ModelAndView showBuildingsByCity(@PathVariable String city,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "9") int size) {
        ModelAndView mav = new ModelAndView("web/list");

        Map<String, Object> params = new HashMap<>();
        params.put("city", city.trim());

        List<BuildingSearchResponse> all = buildingService.findAll(params, List.of());

        int from = Math.max(0, page * size);
        int to   = Math.min(all.size(), from + size);
        List<BuildingSearchResponse> slice = (from > to) ? List.of() : all.subList(from, to);

        Page<BuildingSearchResponse> buildingPage =
                new PageImpl<>(slice, PageRequest.of(page, size), all.size());

        Map<String, String> cityNames = Map.of(
                "HCM","TP. Hồ Chí Minh", "HaNoi","Hà Nội", "DaNang","Đà Nẵng"
        );

        mav.addObject("projects", slice);
        mav.addObject("buildingPage", buildingPage);
        mav.addObject("selectedCity", cityNames.getOrDefault(city, city));
        mav.addObject("districts", DistrictCode.type());
        mav.addObject("typeCodes", BuildingType.type());
        mav.addObject("search", new BuildingSearchRequest());
        return mav;
    }

  //Add
    @GetMapping("/chat")
    public String chatPage() {
        return "web/chat";
    }




}
