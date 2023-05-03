package com.project.crypto.service;

import com.project.crypto.config.LoggingConfig;
import com.project.crypto.dto.LoginDTO;
import com.project.crypto.model.User;
import com.project.crypto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    private LoggingConfig logCryp;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String login(LoginDTO loginDTO){
        User user = userRepository.findByUsername(loginDTO.getUsername());
        logCryp.logCrypBe.info("user: "+user);
        if (user != null) {
            if (encoder.matches(loginDTO.getPassword(), user.getPassword())){
                String raw = String.format("%s:%s", user.getUsername(),loginDTO.getPassword());
                logCryp.logCrypBe.info("Password Match");
                return Base64.getEncoder().encodeToString(raw.getBytes());
            } else {
            	logCryp.logCrypBe.error("Password not Match!");
            }
        }
        return null;
    }

    public String register(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("user");
        userRepository.save(user);
        logCryp.logCrypBe.info("Register Success");
        return "success";
    }
}
