package com.project.crypto.service;

import java.util.List;

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
	
	public List<CryptoUser> getList(Integer userId) {
		try {
			logCryp.logCrypBe.info("get list crypto user "+userId);
			return cryptoUserRepository.getList(userId);
		} catch (Exception e) {
			logCryp.logCrypBe.error("error get list: "+e);
		}
		return null;
	}
	
	public void deleteTxn(int id) {
		try {
			logCryp.logCrypBe.info("penghapusan txn di postofolio id="+id);
			cryptoUserRepository.deleteTxn(id);
		} catch (Exception e) {
			logCryp.logCrypBe.error("delete txn di portofolio gagal: "+e);
		}
	}
}
