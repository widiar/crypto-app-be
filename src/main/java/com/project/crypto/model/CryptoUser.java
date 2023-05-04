package com.project.crypto.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="crypto_user")
public class CryptoUser {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	@Column(name="user_id")
	private int userId;
	@Column(name="nama_crypto")
	private String namaCrypto;
	@Column(name="harga")
	private Double harga;
	@Column(name="tgl_beli")
	private Date tglBeli;

}
