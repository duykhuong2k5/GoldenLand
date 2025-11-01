package com.example.demo.api;

import com.example.demo.entity.Customer;
import com.example.demo.model.dto.AssignmentDTO;
import com.example.demo.model.dto.CustomerDTO;
import com.example.demo.model.request.ChangePasswordRequest;
import com.example.demo.model.request.CustomerCreateRequest;
import com.example.demo.model.request.TransactionCreateRequest;
import com.example.demo.model.response.ResponseDTO;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.security.utils.SecurityUtils;
import com.example.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/api/customer")
public class CustomerAPI {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;
    @PreAuthorize("hasAnyAuthority('STAFF','MANAGER','ADMIN')")
    @PostMapping
    public ResponseDTO addOrUpdateCustomer(@RequestBody CustomerCreateRequest customerCreateRequest){
        return customerService.save(customerCreateRequest);
    }
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN')")
    @PostMapping("/disable")
    public ResponseDTO disableActivity(@PathVariable List<Long> ids){
        return customerService.disableActivity(ids);
    }
    @PreAuthorize("hasAnyAuthority('MANAGER','STAFF','ADMIN')")
    @GetMapping("/{id}/staffs")
    public ResponseDTO loadStaffs(@PathVariable Long id){
        ResponseDTO responseDTO = customerService.findStaffsByCustomerId(id);
        return responseDTO;
    }
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN')")
    @PostMapping("/assignment")
    public ResponseDTO updateAssignmentCustomer(@RequestBody AssignmentDTO assignmentDTO){
        return customerService.updateAssignmentTable(assignmentDTO);
    }
    
    // ==================== XÓA KHÁCH HÀNG ====================
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/delete")
    public ResponseDTO deleteCustomers(@RequestBody List<Long> ids) {
        return customerService.delete(ids);
    }
    // ================== CUSTOMER SELF-SERVICE (profile/password) ==================
    

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/profile/{username}")
    public CustomerDTO updateProfile(@PathVariable String username, @RequestBody CustomerDTO dto) {
        return customerService.updateProfile(username, dto);
    }

    /**
     * Đổi mật khẩu khách hàng theo ID
     * Dành cho người dùng đã đăng nhập (isAuthenticated)
     * Gọi từ: templates/web/customer/password.html
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/change-password/{id}")
    public String changePassword(@PathVariable Long id, @RequestBody ChangePasswordRequest req) {
        boolean ok = customerService.changePassword(id, req.getOldPassword(), req.getNewPassword());
        return ok ? "update_success" : "change_password_fail";
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @GetMapping("/new")
    public List<CustomerDTO> getNewCustomers(
            @RequestParam(value = "days", required = false) Integer days) {
        int d = (days == null || days <= 0) ? 7 : days;
        return customerService.findNewCustomers(d);
    }
    /**
     * Cập nhật trạng thái KH bằng mã không dấu:
     * CHUA_XU_LY / DANG_XU_LY / DA_XU_LY
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','STAFF')")
    @PostMapping("/{id}/update-status")
    public ResponseDTO updateStatus(@PathVariable Long id,
                                    @RequestParam("status") String statusCode) {

        Customer c = customerRepository.findById(id)       // 👈 DÙNG ENTITY
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        String newStatus = switch (statusCode) {
            case "DANG_XU_LY" -> "Đang xử lý";
            case "DA_XU_LY"   -> "Đã xử lý";
            case "CHUA_XU_LY" -> "Chưa xử lý";
            default -> "Chưa xử lý";
        };

        c.setStatus(newStatus);
        c.setModifiedBy(SecurityUtils.getPrincipal() != null
                ? SecurityUtils.getPrincipal().getUsername() : "system");
        c.setModifiedDate(new java.util.Date());
        customerRepository.save(c);

        // 👇 ĐỪNG dùng builder() nếu class bạn không có @Builder
        ResponseDTO resp = new ResponseDTO();
        resp.setMessage("Cập nhật trạng thái thành công");
        resp.setData(newStatus);
        return resp;
    }
    /**
     * Lấy danh sách KH "Chưa xử lý" (để làm nút "Khách chờ liên hệ").
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','STAFF')")
    @GetMapping("/pending")
    public List<CustomerDTO> pendingLeads() {
        return customerService.findByStatus("Chưa xử lý"); // triển khai trong service
    }

}
