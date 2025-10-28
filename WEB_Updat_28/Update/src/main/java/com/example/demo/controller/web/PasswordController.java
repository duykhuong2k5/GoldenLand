package com.example.demo.controller.web;

import com.example.demo.entity.Customer;
import com.example.demo.service.CustomerService;
import com.example.demo.service.EmailService;
import com.example.demo.service.OtpService;
import com.example.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@Controller
public class PasswordController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/forgot-password")
    public ModelAndView forgotPasswordPage() {
        return new ModelAndView("forgot-password");
    }

    @PostMapping("/forgot-password")
    public RedirectView handleForgotPassword(@RequestParam("email") String email) {
        // Check email exists
        if (!customerService.existsByEmail(email)) {
            return new RedirectView("/forgot-password?error=notfound");
        }

        // generate OTP and send email with a link
        String otp = otpService.generateOtp(email);
        // build a link that user can click (will include otp in query for convenience)
        String link = "/reset-password?key=" + email + "&otp=" + otp;
        // send a mail (reuse sendOtp or send a custom message)
        emailService.sendOtp(email, otp + "\nChange password here: " + link);

        return new RedirectView("/reset-password?key=" + email + "&otp=" + otp);
    }

    @GetMapping("/reset-password")
    public ModelAndView resetPasswordPage(@RequestParam(value = "key", required = false) String key,
                                          @RequestParam(value = "otp", required = false) String otp) {
        ModelAndView mav = new ModelAndView("reset-password");
        mav.addObject("key", key);
        mav.addObject("otp", otp);
        return mav;
    }

    @PostMapping("/reset-password")
    public RedirectView handleResetPassword(@RequestParam("key") String key,
                                            @RequestParam("otp") String otp,
                                            @RequestParam("password") String password,
                                            @RequestParam("confirm") String confirm) {
        // validate
        if (password == null || !password.equals(confirm) || password.length() < 6) {
            return new RedirectView("/reset-password?key=" + key + "&error=invalid");
        }

        if (!otpService.validateOtp(key, otp)) {
            return new RedirectView("/reset-password?key=" + key + "&error=invalidotp");
        }

        // find customer by email and set new password
        Optional<Customer> opt = customerRepository.findByEmail(key);
        if (opt.isEmpty()) {
            return new RedirectView("/reset-password?key=" + key + "&error=notfound");
        }

        Customer c = opt.get();
        c.setPassword(passwordEncoder.encode(password));
        customerService.saveDirect(c);

        return new RedirectView("/login?reset=success");
    }
}
