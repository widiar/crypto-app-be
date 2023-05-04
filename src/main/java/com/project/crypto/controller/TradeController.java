package com.project.crypto.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.project.crypto.config.LoggingConfig;
import com.project.crypto.dto.RequestCrypto;
import com.project.crypto.model.CryptoUser;
import com.project.crypto.model.Saldo;
import com.project.crypto.model.UserDetail;
import com.project.crypto.service.CryptoUserService;
import com.project.crypto.service.SaldoService;

@Controller
@RequestMapping("/trade")
public class TradeController {
	
    @Value("${crypto.url}")
    private String baseUrl;
    
	@Autowired
	private SaldoService saldoService;
	
	@Autowired
	private LoggingConfig logCryp;
	
	@Autowired
	private CryptoUserService cryptoUserService;
	
	@PostMapping("/buy/{id}")
	public ResponseEntity<Object> buyCrypto(@PathVariable Integer id, @RequestBody RequestCrypto reqCrypto,
			@AuthenticationPrincipal UserDetail userDetail) 
			{
		logCryp.logCrypBe.info("trade buy "+userDetail.getUsername());
		Double saldoNasabah = saldoService.saldoNasabah(userDetail.getUsername());
		Double hargaCrypto = reqCrypto.getHarga();
		if ((saldoNasabah - hargaCrypto) < 0) {
			logCryp.logCrypBe.info("Saldo tidak cukup");
			//return "saldo tidak cukup";
		} else {
			logCryp.logCrypBe.info("Saldo nasabah cukup");
			//penambahan saldo admin
			saldoService.updateSaldo(userDetail, hargaCrypto);
			//pengurangan saldo user
			saldoService.penguranganSaldo(userDetail, hargaCrypto, saldoNasabah);
			//insert ke portofolio nasabah
			saldoService.addPortfolioNasabah(userDetail, hargaCrypto, reqCrypto.getNamaCrypto());
			//update jumlah crypto
			String url = baseUrl + "crypto/min/" + id;
			HttpHeaders headers = new HttpHeaders();
	        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	        HttpEntity<RequestCrypto> entity = new HttpEntity<RequestCrypto>(reqCrypto,headers);
	        RestTemplate restTemplate = new RestTemplate();

	        Object result = restTemplate.exchange(url, HttpMethod.PUT, entity, Object.class).getBody();
	        return ResponseEntity.ok(result);
		}
		return null;
	}
	
	@GetMapping("/cryp/{userid}")
	public ResponseEntity<List<CryptoUser>> listCryptoUser(@PathVariable Integer userid) {
		return ResponseEntity.ok(cryptoUserService.getList(userid));
	}
	
	@PostMapping("sell/{id}")
	public ResponseEntity<Object> sellCrypto(@PathVariable Integer id, @RequestBody CryptoUser cryptoUser,
			@AuthenticationPrincipal UserDetail userDetail) {
		logCryp.logCrypBe.info("trade sell "+userDetail.getUsername());
		Double saldoAdmin = saldoService.saldoAdmin();
		Double hargaCrypto = cryptoUser.getHarga();
		if (hargaCrypto > saldoAdmin) {
			logCryp.logCrypBe.info("tidak dapat melakukan penjualan");
		} else {
			//penambahan saldo user
			int user_id = cryptoUser.getUserId();
			System.out.println("1");
			saldoService.penambahanSaldoUser(cryptoUser.getHarga(), user_id);
			//pengurangan saldo admin
			System.out.println("2");
			saldoService.penguranganSaldoAdmin(cryptoUser.getHarga());
			//penghapusan txn di portofolio
			System.out.println("3");
			cryptoUserService.deleteTxn(id);
			//penambahan jumlah crypto
			System.out.println("4");
			String url = baseUrl + "crypto/plus/" +id;
			HttpHeaders headers = new HttpHeaders();
	        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	        HttpEntity<CryptoUser> entity = new HttpEntity<CryptoUser>(cryptoUser,headers);
	        RestTemplate restTemplate = new RestTemplate();

	        Object result = restTemplate.exchange(url, HttpMethod.PUT, entity, Object.class).getBody();
	        return ResponseEntity.ok(result);
		}
		return null;
		
	}
}
