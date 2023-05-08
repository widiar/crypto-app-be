package com.project.crypto.service;

import com.project.crypto.config.LoggingConfig;
import com.project.crypto.dto.LoginDTO;
import com.project.crypto.dto.ResponseDto;
import com.project.crypto.dto.ResponseLoginDto;
import com.project.crypto.model.Saldo;
import com.project.crypto.model.User;
import com.project.crypto.repository.SaldoRepository;
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
    LoggingConfig logCryp;

    @Autowired
    SaldoRepository saldoRepository;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public ResponseLoginDto login(LoginDTO loginDTO){
        User user = userRepository.findByUsername(loginDTO.getUsername());
        ResponseLoginDto responseLogin = new ResponseLoginDto();
        if (user != null) {
            logCryp.logCrypBe.info("user: "+user.getFullName());
            if (encoder.matches(loginDTO.getPassword(), user.getPassword())){
                String raw = String.format("%s:%s", user.getUsername(),loginDTO.getPassword());
                logCryp.logCrypBe.info("Password Match");
                String session =  Base64.getEncoder().encodeToString(raw.getBytes());
                responseLogin.setSession(session);
                responseLogin.setRole(user.getRole());
            } else {
                logCryp.logCrypBe.error("Password not Match!");
                responseLogin.setSession("Password salah");
            }
        }else{
            responseLogin.setSession("User tidak ditemukan");
        }
        return responseLogin;
    }

    public ResponseDto register(User user){
        User chekcUser = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());
        ResponseDto responseDto = new ResponseDto();
        if (chekcUser != null) {
            logCryp.logCrypBe.info("Register Failed");
            responseDto.setFailed("Username or email already taken");
        }else{
            user.setPassword(encoder.encode(user.getPassword()));
            user.setRole("user");
            Saldo newSaldo = new Saldo();
            newSaldo.setUser(user);
            newSaldo.setJumlah(0D);
            saldoRepository.save(newSaldo);
            userRepository.save(user);
            logCryp.logCrypBe.info("Register Success");
            responseDto.setSuccess();
        }
        return responseDto;
    }
}
