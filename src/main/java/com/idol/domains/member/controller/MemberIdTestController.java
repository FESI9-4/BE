package com.idol.domains.member.controller;

import com.idol.domains.auth.util.annotation.MemberId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/test")
public class MemberIdTestController {

    @GetMapping("/me")
    public ResponseEntity<String> getMyMemberId(@MemberId UUID memberId) {
        return ResponseEntity.ok(memberId.toString());
    }
}
