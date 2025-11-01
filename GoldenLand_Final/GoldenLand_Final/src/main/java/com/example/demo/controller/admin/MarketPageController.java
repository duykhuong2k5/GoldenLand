package com.example.demo.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/market")
public class MarketPageController {

    @GetMapping("/price/{buildingId}")
    public String view(@PathVariable Long buildingId, Model model) {
        model.addAttribute("buildingId", buildingId);
        return "admin/market/price";
    }
}
