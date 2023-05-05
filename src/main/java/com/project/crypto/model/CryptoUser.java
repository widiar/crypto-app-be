package com.project.crypto.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="crypto_user")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CryptoUser {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	@JsonBackReference
	private User user;

	@Column(name="nama_crypto")
	private String namaCrypto;
	@Column(name="harga")
	private Double harga;
	@Column(name="tgl_beli")
	private Date tglBeli;

	private Long jumlah;

}
