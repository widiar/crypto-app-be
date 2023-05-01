package com.project.crypto.service;

import com.project.crypto.dto.ResponseSaldoDto;
import com.project.crypto.model.Saldo;
import com.project.crypto.model.User;
import com.project.crypto.model.UserDetail;
import com.project.crypto.repository.SaldoRepository;
import com.project.crypto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaldoService {

    @Autowired
    SaldoRepository saldoRepository;

    @Autowired
    UserRepository userRepository;

    public String topup(Double jumlah, UserDetail userDetail){
        User user = userRepository.findByUsername(userDetail.getUsername());
        Saldo saldo = user.getSaldo();
        if (saldo != null) {
            saldo.setJumlah(saldo.getJumlah() + jumlah);
            saldoRepository.save(saldo);
        }else{
            Saldo newSaldo = new Saldo();
            newSaldo.setUser(user);
            newSaldo.setJumlah(jumlah);
            saldoRepository.save(newSaldo);
        }
        return "success";
    }

    public ResponseSaldoDto inquiry(UserDetail userDetail){
        User user = userRepository.findByUsername(userDetail.getUsername());
        ResponseSaldoDto responseSaldoDto = new ResponseSaldoDto();
        responseSaldoDto.setUsername(user.getUsername());
        responseSaldoDto.setTotalSaldo(user.getSaldo().getJumlah());
        return responseSaldoDto;
    }
}
