package com.project.crypto.service;

import com.project.crypto.config.LoggingConfig;
import com.project.crypto.dto.RequestCrypto;
import com.project.crypto.dto.ResponseSaldoDto;
import com.project.crypto.model.CryptoUser;
import com.project.crypto.model.Saldo;
import com.project.crypto.model.User;
import com.project.crypto.model.UserDetail;
import com.project.crypto.repository.CryptoUserRepository;
import com.project.crypto.repository.SaldoRepository;
import com.project.crypto.repository.UserRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaldoService {

    @Autowired
    SaldoRepository saldoRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CryptoUserRepository cryptoUser;
    
    @Autowired
    private LoggingConfig logCryp;

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
    
    public Double saldoNasabah(String username) {
    	logCryp.logCrypBe.info("ambil saldo nasabah "+username);
    	Double saldoNasabah = saldoRepository.saldoNasabah(username);
    	return saldoNasabah;
    }
    
    public void updateSaldo(UserDetail userDetail, Double amount) {
//    	saldoRepository.updateSaldoAdmin(amount);
        User user = userRepository.findByRole("admin");
        user.getSaldo().setJumlah(user.getSaldo().getJumlah() + amount);
        userRepository.save(user);
    	logCryp.logCrypBe.info("Penambahan Saldo Admin Berhasil");
    }
    
    public void penguranganSaldo(UserDetail userDetail, Double amount, Double saldoNasabah) {
    	Double updSaldo = saldoNasabah - amount;
    	saldoRepository.penguranganSaldoNasabah(updSaldo, userDetail.getUsername());
    	logCryp.logCrypBe.info("Pengurangan Saldo User "+userDetail.getUsername()+ " Berhasil");
    }
    
    public void addPortfolioNasabah(UserDetail userDetail, Double hargaCrypto, String  namaCrypto, Long jumlah) {
    	try {
    		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        	Date date = new Date();
        	logCryp.logCrypBe.info("buy date: "+date);
//        	int user_id = saldoRepository.user_id(userDetail.getUsername());
            User user = userRepository.findByUsername(userDetail.getUsername());
            CryptoUser cryptoUsr = cryptoUser.findByUserIdAndNamaCrypto(user.getId(), namaCrypto);
            if (cryptoUsr != null) {
                cryptoUsr.setHarga(hargaCrypto);
                cryptoUsr.setJumlah(cryptoUsr.getJumlah() + jumlah);
                cryptoUser.save(cryptoUsr);
            }else{
                CryptoUser cu = new CryptoUser();
                cu.setUser(user);
                cu.setNamaCrypto(namaCrypto);
                cu.setHarga(hargaCrypto);
                cu.setTglBeli(date);
                cu.setJumlah(jumlah);
                cryptoUser.save(cu);
            }
        	logCryp.logCrypBe.info("Add Portofolio user "+userDetail.getUsername()+" Berhasil.");
    	} catch (Exception e) {
			logCryp.logCrypBe.error("Error add Postofolio "+e);
		}
    }
    
    public Double saldoAdmin() {
    	logCryp.logCrypBe.info("get saldo admin");
//    	Double saldoAdmin = saldoRepository.saldoAdmin();
        User user = userRepository.findByRole("admin");
    	return user.getSaldo().getJumlah();
    }
    
    public void penambahanSaldoUser(Double hargaCrypro, String username) {
        Integer user_id = userRepository.findByUsername(username).getId();
        logCryp.logCrypBe.info("Penambahan Saldo "+user_id+" sebesar "+hargaCrypro);
    	saldoRepository.penambahanSaldoUser(hargaCrypro, user_id);
    }
    
    public void penguranganSaldoAdmin(Double hargaCrypto) {
    	logCryp.logCrypBe.info("Pengurangan Saldo Admin");
        User user = userRepository.findByRole("admin");
        user.getSaldo().setJumlah(user.getSaldo().getJumlah() - hargaCrypto);
        userRepository.save(user);
//    	saldoRepository.penguranganSaldoAdmin(hargaCrypto);
    }
}
