package com.example.demo.api;

import com.example.demo.entity.Customer;
import com.example.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")   // công khai
public class WebContactAPI {

    @Autowired
    private CustomerService customerService;

    // payload tối giản cho form liên hệ
    public static record ContactReq(String fullname, String email, String phone, String demand) {}

    @PostMapping("/contact")
    public String submit(@RequestBody ContactReq req) {
        Customer c = new Customer();
        c.setFullname(req.fullname());
        c.setEmail(req.email());
        c.setPhone(req.phone());
        c.setDemand(req.demand());
        c.setCompanyname("Chưa cập nhật");
        c.setStatus("Chưa xử lý");
        c.setIsActive(1);

        // Nếu cột username/password đang NOT NULL thì set tạm cho hợp lệ
        c.setUsername((req.email()!=null && !req.email().isBlank())
                ? req.email() : ("lead_"+System.currentTimeMillis()));
        c.setPassword("N/A"); // không dùng để đăng nhập

        customerService.saveDirect(c);  // <- đã có trong CustomerService
        return "ok";
    }
}
