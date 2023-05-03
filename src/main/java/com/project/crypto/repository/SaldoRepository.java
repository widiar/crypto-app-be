package com.project.crypto.repository;

import com.project.crypto.model.Saldo;


import org.springframework.data.jpa.repository.JpaRepository;

public interface SaldoRepository extends JpaRepository<Saldo, Integer> {
	
	
}
