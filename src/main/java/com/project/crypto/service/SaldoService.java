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

    public void topup(Double jumlah, UserDetail userDetail){
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
    }

    public ResponseSaldoDto inquiry(UserDetail userDetail){
        User user = userRepository.findByUsername(userDetail.getUsername());
        ResponseSaldoDto responseSaldoDto = new ResponseSaldoDto();
        responseSaldoDto.setUsername(user.getUsername());
        Saldo saldo = user.getSaldo();
        if (saldo != null) {
            responseSaldoDto.setTotalSaldo(user.getSaldo().getJumlah());
        }else{
            responseSaldoDto.setTotalSaldo(0D);
        }
        return responseSaldoDto;
    }
    
    public void updateSaldo(UserDetail userDetail, Double amount) {
    	User user = userRepository.findByUsername(userDetail.getUsername());
    	Saldo saldo = user.getSaldo();
    	saldo.setJumlah(saldo.getJumlah()+amount);
    	saldoRepository.save(saldo);
    }
    
    public void penguranganSaldo(UserDetail userDetail, Double amount) {
    	User user = userRepository.findByUsername(userDetail.getUsername());
    	Saldo saldo = user.getSaldo();
    	saldo.setJumlah(saldo.getJumlah()-amount);
    	saldoRepository.save(saldo);
    }
}
