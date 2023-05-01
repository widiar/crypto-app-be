package com.project.crypto.controller;

import com.project.crypto.dto.ResponseSaldoDto;
import com.project.crypto.dto.SaldoDto;
import com.project.crypto.model.UserDetail;
import com.project.crypto.service.SaldoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/saldo")
public class SaldoController {


    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SaldoService saldoService;

    @PostMapping
    public ResponseEntity<String> topup(
            @RequestBody SaldoDto saldo,
            @AuthenticationPrincipal UserDetail userDetail
    ){
        LOG.info(saldo.getJumlah().toString());
        saldoService.topup(saldo.getJumlah(), userDetail);
        return ResponseEntity.ok("success");
    }

    @CrossOrigin
    @GetMapping()
    public ResponseEntity<ResponseSaldoDto> inquiry(
            @AuthenticationPrincipal UserDetail userDetail
    ){
        return new ResponseEntity<>(saldoService.inquiry(userDetail), HttpStatusCode.valueOf(200));
    }
}
