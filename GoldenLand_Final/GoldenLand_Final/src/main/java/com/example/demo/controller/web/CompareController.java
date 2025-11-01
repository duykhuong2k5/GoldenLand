package com.example.demo.controller.web;

import com.example.demo.model.dto.BuildingDTO;
import com.example.demo.service.BuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CompareController {

 private final BuildingService buildingService;

 // Render trang (Thymeleaf): /templates/web/compare.html
 @GetMapping("/so-sanh")
 public String comparePage() {
     return "web/compare";
 }

 /**
  * API trả danh sách tòa theo ids.
  * Hỗ trợ cả 2 cách truyền:
  *  - /api/buildings?ids=1&ids=5&ids=9
  *  - /api/buildings?ids=1,5,9
  */
 @GetMapping("/api/buildings")
 @ResponseBody
 public ResponseEntity<List<BuildingDTO>> getByIds(
         @RequestParam(value = "ids", required = false) List<String> idsRaw) {

     if (idsRaw == null || idsRaw.isEmpty()) {
         return ResponseEntity.ok(List.of());
     }

     List<Long> ids = idsRaw.stream()
             .flatMap(s -> Arrays.stream(s.split(",")))
             .map(String::trim)
             .filter(str -> !str.isEmpty() && str.matches("\\d+"))
             .distinct()
             .limit(4)                         // 👈 giới hạn 4 tòa
             .map(Long::valueOf)
             .toList();

     return ResponseEntity.ok(buildingService.findByIds(ids));
 }

}
