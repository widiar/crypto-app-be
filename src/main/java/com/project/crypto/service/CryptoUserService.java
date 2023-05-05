package com.project.crypto.service;

import java.util.List;
import java.util.Optional;

import com.project.crypto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.crypto.config.LoggingConfig;
import com.project.crypto.model.CryptoUser;
import com.project.crypto.repository.CryptoUserRepository;

@Service
public class CryptoUserService {
	
	@Autowired
	private LoggingConfig logCryp;
	
	@Autowired
	private CryptoUserRepository cryptoUserRepository;

	@Autowired
	UserRepository userRepository;
	
	public List<CryptoUser> getList(String username) {
		Integer userId = userRepository.findByUsername(username).getId();
		try {
			logCryp.logCrypBe.info("get list crypto user "+userId);
			return cryptoUserRepository.getList(userId);
		} catch (Exception e) {
			logCryp.logCrypBe.error("error get list: "+e);
		}
		return null;
	}

	public String checkNum(Integer id, Long jumlah){
		Optional<CryptoUser> cryptoUser = cryptoUserRepository.findById(id);
		if (cryptoUser.isPresent()){
			CryptoUser crypto = cryptoUser.get();
			if(jumlah <= crypto.getJumlah()) return "berhasil";
		}
		return "gagal";
	}
	
	public void deleteTxn(int id, Long jumlah) {
		try {
			logCryp.logCrypBe.info("penghapusan txn di postofolio id="+id);
			Optional<CryptoUser> cryptoUser = cryptoUserRepository.findById(id);
			if (cryptoUser.isPresent()){
				CryptoUser crypto = cryptoUser.get();
				if (crypto.getJumlah().equals(jumlah)){
					cryptoUserRepository.deleteTxn(id);
				}else{
					crypto.setJumlah(crypto.getJumlah() - jumlah);
					cryptoUserRepository.save(crypto);
				}
			}
//			cryptoUserRepository.deleteTxn(id);
		} catch (Exception e) {
			logCryp.logCrypBe.error("delete txn di portofolio gagal: "+e);
		}
	}
}
