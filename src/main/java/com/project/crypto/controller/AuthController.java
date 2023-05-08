package com.project.crypto.controller;

import com.project.crypto.config.LoggingConfig;
import com.project.crypto.dto.AuthCheckDto;
import com.project.crypto.dto.LoginDTO;
import com.project.crypto.dto.ResponseDto;
import com.project.crypto.dto.ResponseLoginDto;
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
    public ResponseEntity<ResponseLoginDto> login(@RequestBody LoginDTO loginDTO){
    	logCryp.logCrypBe.info("User ini login: " +loginDTO.getUsername());
        ResponseLoginDto status = authService.login(loginDTO);
        logCryp.logCrypBe.info("login status: "+status);
        if (status.getRole() != null) {
            return ResponseEntity.ok(status);
        }
        return new ResponseEntity<>(status,HttpStatusCode.valueOf(400));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@RequestBody User user){
        ResponseDto status = authService.register(user);
        logCryp.logCrypBe.info("status: "+status.toString());
        return new ResponseEntity<>(status, HttpStatusCode.valueOf(status.getCode()));
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
