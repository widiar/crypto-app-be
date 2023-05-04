package com.project.crypto.repository;

import com.project.crypto.model.CryptoUser;
import com.project.crypto.model.Saldo;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SaldoRepository extends JpaRepository<Saldo, Integer> {
	
	@Query(value="Select jumlah from saldo where user_id = (select id from user where username = ? )", nativeQuery=true)
	Double saldoNasabah(String username);
	
	//id admin = 2 (selalu)
	@Modifying
	@Transactional
	@Query(value="update saldo set jumlah=jumlah+? where user_id=2", nativeQuery=true)
	void updateSaldoAdmin(Double amount);
	
	@Modifying
	@Transactional
	@Query(value="update saldo set jumlah=? where user_id = (select id from user where username = ? )", nativeQuery=true)
	void penguranganSaldoNasabah(Double updSaldo, String username);
	
	@Query(value="Select id from user where username=?", nativeQuery=true)
	int user_id(String username);

	void save(CryptoUser cu);
	
	@Query(value="select jumlah from saldo where user_id=2", nativeQuery=true)
	Double saldoAdmin();
	
	@Modifying
	@Transactional
	@Query(value="update saldo set jumlah=jumlah+? where user_id=?", nativeQuery=true)
	void penambahanSaldoUser(Double amount, int user_id);
	
	@Modifying
	@Transactional
	@Query(value="update saldo set jumlah=jumlah-? where user_id=2", nativeQuery=true)
	void penguranganSaldoAdmin(Double amount);
}
