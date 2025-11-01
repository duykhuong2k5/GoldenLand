package com.example.demo.controller.admin;

import com.example.demo.constant.SystemConstant;
import com.example.demo.enums.Status;
import com.example.demo.enums.TransactionType;
import com.example.demo.model.dto.CustomerDTO;
import com.example.demo.model.dto.TransactionDTO;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.model.request.CustomerCreateRequest;
import com.example.demo.model.request.CustomerSearchRequest;
import com.example.demo.security.utils.SecurityUtils;
import com.example.demo.service.CustomerService;
import com.example.demo.service.TransactionService;
import com.example.demo.service.UserService;
import com.example.demo.utils.MessageUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CustomerPageController {

    @Autowired private UserService userService;
    @Autowired private CustomerService customerService;
    @Autowired private TransactionService transactionService;
    @Autowired private MessageUtils messageUtil;

    // ========== LIST + PHÂN TRANG ==========
    @GetMapping("/admin/customer-list")
    public ModelAndView getCustomers(@ModelAttribute CustomerSearchRequest customerSearchRequest,
                                     @RequestParam(required = false) Map<String, Object> ignored,
                                     @ModelAttribute(SystemConstant.MODEL) UserDTO model,
                                     HttpServletRequest request,
                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                     @RequestParam(name = "size", defaultValue = "5") int size) {

        Map<String, Object> conditions = new HashMap<>();

        if (StringUtils.isNotBlank(customerSearchRequest.getFullname())) {
            conditions.put("fullname", customerSearchRequest.getFullname().trim());
        }
        if (StringUtils.isNotBlank(customerSearchRequest.getPhone())) {
            conditions.put("phone", customerSearchRequest.getPhone().trim());
        }
        if (StringUtils.isNotBlank(customerSearchRequest.getEmail())) {
            conditions.put("email", customerSearchRequest.getEmail().trim());
        }
        if (customerSearchRequest.getStaffId() != null) {
            conditions.put("staffId", customerSearchRequest.getStaffId());
        }

        // STAFF chỉ xem khách của mình
        if (SecurityUtils.getAuthorities().contains("STAFF")) {
            Long staffId = SecurityUtils.getPrincipal().getId();
            conditions.put("staffId", staffId);
        }

        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1));
        Page<CustomerDTO> customerPage = customerService.findAll(conditions, pageable);

        ModelAndView mav = new ModelAndView("admin/customer/list");
        mav.addObject("customerList", customerPage.getContent());
        mav.addObject("customerPage", customerPage);

        mav.addObject("modelSearch", customerSearchRequest);
        mav.addObject("staffs", userService.getStaffs());
        mav.addObject("totalItem", (int) customerPage.getTotalElements());
        return mav;
    }

    // ========== FORM TẠO/SỬA ==========
    @GetMapping("/admin/customer-edit")
    public ModelAndView customerAddForm(@ModelAttribute CustomerCreateRequest customerCreateRequest) {
        ModelAndView mav = new ModelAndView("admin/customer/edit");
        mav.addObject("statuses", Status.type());
        mav.addObject("TransactionList", TransactionType.transactionType());
        mav.addObject("customerCreateRequest", customerCreateRequest);
        return mav;
    }

    @GetMapping("/admin/customer-edit-{id}")
    public ModelAndView customerEditForm(@PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("admin/customer/edit");
        CustomerCreateRequest customerCreateRequest = customerService.findOneById(id);
        mav.addObject("statuses", Status.type());
        mav.addObject("TransactionList", TransactionType.transactionType());
        mav.addObject("customerCreateRequest", customerCreateRequest);

        List<TransactionDTO> CSKH = transactionService.findAllByCodeAndCustomer("CSKH", id);
        List<TransactionDTO> DDX = transactionService.findAllByCodeAndCustomer("DDX", id);
        List<TransactionDTO> DATCOC       = transactionService.findAllByCodeAndCustomer("DATCOC", id);
        List<TransactionDTO> RENT_PAYMENT = transactionService.findAllByCodeAndCustomer("RENT_PAYMENT", id);
        mav.addObject("CSKH", CSKH);
        mav.addObject("DDX", DDX);
        mav.addObject("DATCOC", DATCOC);
        mav.addObject("RENT_PAYMENT", RENT_PAYMENT);
        return mav;
    }

    // ========== CUSTOMER SELF-SERVICE PAGES (VIEW) ==========
    @GetMapping("/customer/profile-{username}")
    public ModelAndView viewProfile(@PathVariable("username") String username, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("admin/customer/profile");
        CustomerDTO profileModel = customerService.findOneByUsername(username);
        initMessageResponse(mav, request);
        mav.addObject(SystemConstant.MODEL, profileModel);
        return mav;
    }

    @GetMapping("/customer/profile-password")
    public ModelAndView changePasswordPage(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("admin/customer/password");
        String username = SecurityUtils.getPrincipal().getUsername();
        CustomerDTO profileModel = customerService.findOneByUsername(username);
        initMessageResponse(mav, request);
        mav.addObject(SystemConstant.MODEL, profileModel);
        return mav;
    }

    private void initMessageResponse(ModelAndView mav, HttpServletRequest request) {
        String message = request.getParameter("message");
        if (message != null && StringUtils.isNotEmpty(message)) {
            Map<String, String> messageMap = messageUtil.getMessage(message);
            mav.addObject(SystemConstant.ALERT, messageMap.get(SystemConstant.ALERT));
            mav.addObject(SystemConstant.MESSAGE_RESPONSE, messageMap.get(SystemConstant.MESSAGE_RESPONSE));
        }
    }
}
