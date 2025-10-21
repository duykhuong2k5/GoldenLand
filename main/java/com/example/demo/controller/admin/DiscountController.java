package com.example.demo.controller.admin;

import com.example.demo.entity.AppDiscount;
import com.example.demo.service.impl.AppDiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/app-discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final AppDiscountService service;

    @GetMapping
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("admin/discount/list");
        mv.addObject("items", service.list());
        mv.addObject("active", service.active().orElse(null));
        return mv;
    }

    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView mv = new ModelAndView("admin/discount/edit");
        mv.addObject("model", new AppDiscount());
        return mv;
    }

    @GetMapping("/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("admin/discount/edit");
        mv.addObject("model", service.find(id).orElseThrow());
        return mv;
    }

    @PostMapping
    public String save(@ModelAttribute AppDiscount model, RedirectAttributes ra) {
        service.save(model);
        ra.addFlashAttribute("message", "Lưu chiết khấu thành công");
        return "redirect:/admin/app-discounts";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("message", "Đã xoá");
        return "redirect:/admin/app-discounts";
    }
}
