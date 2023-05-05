package com.project.crypto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.crypto.model.CryptoUser;

import jakarta.transaction.Transactional;

@Repository
public interface CryptoUserRepository extends JpaRepository<CryptoUser, Integer>{
	
	@Query(value="Select id, user_id, nama_crypto, harga, tgl_beli, jumlah from crypto_user where user_id=?", nativeQuery=true)
	List<CryptoUser> getList(int userId);
	
	@Modifying
	@Transactional
	@Query(value="delete from crypto_user where id=?", nativeQuery=true)
	void deleteTxn(int id);

	CryptoUser findByUserIdAndNamaCrypto(Integer userId, String namaCrypto);
}
