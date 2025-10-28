package com.example.demo.api;

import com.example.demo.entity.ChatRoom;
import com.example.demo.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatRestAPI {

    private final ChatService chatService;

    public ChatRestAPI(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/room/wait")
    public ResponseEntity<?> waitRoom(@RequestParam("customerId") Long customerId) {
        ChatRoom room = chatService.createOrGetWaitingRoom(customerId);
        return ResponseEntity.ok(Map.of(
                "roomId", room.getId(),            // = customerId
                "staffId", room.getStaffId(),      // có thể null
                "customerId", room.getCustomerId(),
                "active", room.getActive(),
                "createdAt", room.getCreatedAt()
        ));
    }

    @GetMapping("/room/active-by-staff")
    public ResponseEntity<?> activeByStaff(@RequestParam("staffId") Long staffId) {
        return chatService.getActiveRoomForStaff(staffId)
                .<ResponseEntity<?>>map(r -> ResponseEntity.ok(Map.of(
                        "roomId", r.getId(),
                        "customerId", r.getCustomerId(),
                        "roomCode", r.getRoomCode()
                )))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/staff/next")
    public ResponseEntity<?> claimNext(@RequestParam("staffId") Long staffId) {
        return chatService.assignNextWaitingRoomToStaff(staffId)
                .<ResponseEntity<?>>map(r -> ResponseEntity.ok(Map.of(
                        "roomId", r.getId(),
                        "customerId", r.getCustomerId(),
                        "roomCode", r.getRoomCode()
                )))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/room/close")
    public ResponseEntity<?> closeRoom(@RequestParam("roomId") Long roomId) {
        chatService.closeRoom(roomId);
        return ResponseEntity.ok(Map.of("closed", true));
    }

    /** Staff bấm Thoát: đóng + claim tiếp theo (nếu có) */
    @PostMapping("/staff/close-and-next")
    public ResponseEntity<?> closeAndNext(@RequestParam Long staffId,
                                          @RequestParam(required = false) Long roomId) {
        return ResponseEntity.ok(chatService.closeCurrentAndAssignNext(staffId, roomId));
    }

    @PostMapping("/staff/reset")
    public ResponseEntity<?> resetStaff(@RequestParam Long staffId) {
        int closed = chatService.resetStaffSession(staffId);
        return ResponseEntity.ok(Map.of("closedRooms", closed));
    }
}
