package com.example.demo.api;

import com.example.demo.constant.SystemConstant;
import com.example.demo.exception.MyException;
import com.example.demo.model.dto.PasswordDTO;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.security.utils.SecurityUtils;
import com.example.demo.service.UserService;
//import org.springframework.http.MediaType;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserAPI {

    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN')")
    public ResponseEntity<UserDTO> createUsers(@RequestBody UserDTO newUser) {
        // Managers may only create STAFF accounts
        if (SecurityUtils.getAuthorities().contains("MANAGER") && !"STAFF".equalsIgnoreCase(newUser.getRoleCode())) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(userService.insert(newUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN')")
    public ResponseEntity<UserDTO> updateUsers(@PathVariable("id") long id, @RequestBody UserDTO userDTO) {
        // Managers may only update STAFF accounts and cannot escalate roles
        if (SecurityUtils.getAuthorities().contains("MANAGER") && userDTO.getRoleCode() != null 
                && !"STAFF".equalsIgnoreCase(userDTO.getRoleCode())) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(userService.update(id, userDTO));
    }

    @PutMapping("/change-password/{id}")
    public ResponseEntity<String> changePasswordUser(@PathVariable("id") long id, @RequestBody PasswordDTO passwordDTO) {
        try {
            userService.updatePassword(id, passwordDTO);
            return ResponseEntity.ok(SystemConstant.UPDATE_SUCCESS);
        } catch (MyException e) {
            //LOGGER.error(e.getMessage());
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @PutMapping("/password/{id}/reset")
    public ResponseEntity<UserDTO> resetPassword(@PathVariable("id") long id) {
        return ResponseEntity.ok(userService.resetPassword(id));
    }

    @PutMapping("/profile/{username}")
    public ResponseEntity<UserDTO> updateProfileOfUser(@PathVariable("username") String username, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateProfileOfUser(username, userDTO));
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Void> deleteUsers(@RequestBody long[] idList) {
        if (idList.length > 0) {
            userService.delete(idList);
        }
        return ResponseEntity.noContent().build();
    }
//    /**
//     * Trả về danh sách người dùng mới tạo trong N ngày gần nhất (mặc định 7)
//     */
//    @GetMapping(value = "/new", produces = MediaType.APPLICATION_JSON_VALUE)
//    public List<UserDTO> getNewUsers(@RequestParam(value = "days", required = false) Integer days) {
//        int d = (days == null || days <= 0) ? 7 : days;
//        return userService.findNewUsers(d);   // trả trực tiếp List -> Jackson serialize JSON
//    }
}
