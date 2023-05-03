package com.project.crypto.controller;

import com.project.crypto.config.LoggingConfig;
import com.project.crypto.dto.AuthCheckDto;
import com.project.crypto.dto.LoginDTO;
import com.project.crypto.model.User;
import com.project.crypto.model.UserDetail;
import com.project.crypto.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;

@Controller
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    AuthService authService;
    
    @Autowired
    private LoggingConfig logCryp;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO){
    	logCryp.logCrypBe.info("User ini login: " +loginDTO.getUsername());
        String status = authService.login(loginDTO);
        logCryp.logCrypBe.info("login status: "+status);
        if (status != null) {
            return ResponseEntity.ok(status);
        }
        return new ResponseEntity<>("Username / password salah",HttpStatusCode.valueOf(400));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user){
        String status = authService.register(user);
        logCryp.logCrypBe.info("status: "+status);
        return ResponseEntity.ok(status);
    }

    @PostMapping("/check")
    public ResponseEntity<AuthCheckDto> check(@AuthenticationPrincipal UserDetail userDetail){
        AuthCheckDto checkDto = new AuthCheckDto();
        if (userDetail != null) {
            checkDto.setRole(userDetail.getAuthorities());
            checkDto.setIsValid(true);
            return ResponseEntity.ok(checkDto);
        }
        checkDto.setIsValid(false);
        return ResponseEntity.ok(checkDto);
    }
}
