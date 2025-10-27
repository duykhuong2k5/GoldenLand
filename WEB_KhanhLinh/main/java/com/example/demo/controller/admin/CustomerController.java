package com.example.demo.controller.admin;

import com.example.demo.constant.SystemConstant;
import com.example.demo.converter.CustomerConverter;
import com.example.demo.enums.Status;
import com.example.demo.enums.TransactionType;
import com.example.demo.model.dto.CustomerDTO;
import com.example.demo.model.dto.TransactionDTO;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.model.request.CustomerCreateRequest;
import com.example.demo.model.request.CustomerSearchRequest;
import com.example.demo.security.utils.SecurityUtils;
import com.example.demo.utils.MessageUtils;
import org.apache.commons.lang.StringUtils; 
import com.example.demo.service.CustomerService;
import com.example.demo.service.UserService;
import com.example.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.HashMap; 

@Transactional
@RestController(value = "customersControllerOfAdmin")
public class CustomerController {
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerConverter customerConverter;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private MessageUtils messageUtil;

    @RequestMapping(value = "/admin/customer-list", method = RequestMethod.GET)
    public ModelAndView getNews(@ModelAttribute CustomerSearchRequest customerSearchRequest,
                                @RequestParam Map<String, Object> conditions,
                                @ModelAttribute(SystemConstant.MODEL) UserDTO model, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("admin/customer/list");
        
        if(SecurityUtils.getAuthorities().contains("ROLE_STAFF")){
            Long staffId = SecurityUtils.getPrincipal().getId();
            conditions.put("staffId", staffId);
        }
        List<CustomerDTO> customerDTOS = customerService.findAll(conditions);
        model.setMaxPageItems(5);
        model.setTotalItem(customerDTOS.size());
        mav.addObject("customerList", customerDTOS);
        mav.addObject("modelSearch", customerSearchRequest);
        mav.addObject("staffs", userService.getStaffs());
        return mav;
    }

    @GetMapping(value = "/admin/customer-edit")
    public ModelAndView customerAddForm(@ModelAttribute CustomerCreateRequest customerCreateRequest, HttpServletRequest request){
        ModelAndView mav = new ModelAndView("admin/customer/edit");
        mav.addObject("statuses", Status.type());
        mav.addObject("TransactionList", TransactionType.transactionType());
        mav.addObject("customerCreateRequest", customerCreateRequest);
        return mav;
    }
    @GetMapping(value = "/admin/customer-edit-{id}")
    public ModelAndView customerEditForm(@PathVariable("id") Long id, HttpServletRequest request){
        ModelAndView mav = new ModelAndView("admin/customer/edit");
        CustomerCreateRequest customerCreateRequest = customerService.findOneById(id);
        mav.addObject("statuses", Status.type());
        mav.addObject("TransactionList", TransactionType.transactionType());
        mav.addObject("customerCreateRequest", customerCreateRequest);
        List<TransactionDTO> CSKH = transactionService.findAllByCodeAndCustomer("CSKH", id);
        List<TransactionDTO> DDX = transactionService.findAllByCodeAndCustomer("DDX", id);
        mav.addObject("CSKH", CSKH);
        mav.addObject("DDX", DDX);
        return mav;
    }
 // /customer/profile-{username}
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
