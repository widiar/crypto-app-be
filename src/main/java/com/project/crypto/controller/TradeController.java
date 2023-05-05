package com.project.crypto.controller;

import java.util.Arrays;
import java.util.List;

import com.project.crypto.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.project.crypto.config.LoggingConfig;
import com.project.crypto.dto.RequestCrypto;
import com.project.crypto.model.CryptoUser;
import com.project.crypto.model.UserDetail;
import com.project.crypto.service.CryptoUserService;
import com.project.crypto.service.SaldoService;

@Controller
@RequestMapping("/trade")
@CrossOrigin
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
											@AuthenticationPrincipal UserDetail userDetail) {
		logCryp.logCrypBe.info("trade buy "+userDetail.getUsername());
		Double saldoNasabah = saldoService.saldoNasabah(userDetail.getUsername());

		//get harga terupdate crypto
		String urlt = baseUrl + "crypto/" + id;
		RestTemplate restTemplatet = new RestTemplate();
		RequestCrypto crp = restTemplatet.getForObject(urlt, RequestCrypto.class);
//		System.out.println(crp.toString());

		if (crp != null) {
			Double hargaCrypto = crp.getHarga() * reqCrypto.getJumlah();

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
				System.out.println(crp.getHarga());
				saldoService.addPortfolioNasabah(userDetail, crp.getHarga(), crp.getNamaCrypto(), reqCrypto.getJumlah());
				//update jumlah crypto
				String url = baseUrl + "crypto/min/" + id;
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
				HttpEntity<RequestCrypto> entity = new HttpEntity<RequestCrypto>(reqCrypto,headers);
				RestTemplate restTemplate = new RestTemplate();

				Object result = restTemplate.exchange(url, HttpMethod.PUT, entity, Object.class).getBody();
				return ResponseEntity.ok(result);
			}
		}
		return null;
	}

	@GetMapping("/cryp")
	public ResponseEntity<List<CryptoUser>> listCryptoUser(@AuthenticationPrincipal UserDetail userDetail) {
		return ResponseEntity.ok(cryptoUserService.getList(userDetail.getUsername()));
	}

	@PostMapping("sell/{id}")
	public ResponseEntity<Object> sellCrypto(@PathVariable Integer id, @RequestBody CryptoUser cryptoUser,
								  @AuthenticationPrincipal UserDetail userDetail) {
		logCryp.logCrypBe.info("trade sell "+userDetail.getUsername());
		Double saldoAdmin = saldoService.saldoAdmin();
//		Double hargaCrypto = cryptoUser.getHarga();
		ResponseDto responseDto = new ResponseDto();

		String check = cryptoUserService.checkNum(id, cryptoUser.getJumlah());
		if (check.equals("berhasil")){
			String urlt = baseUrl + "crypto/nama/" + cryptoUser.getNamaCrypto();
			RestTemplate restTemplatet = new RestTemplate();
			RequestCrypto crp = restTemplatet.getForObject(urlt, RequestCrypto.class);

			if (crp != null) {
				Double hargaCrypto = crp.getHarga() * cryptoUser.getJumlah();
//			System.out.println(hargaCrypto);
				if (hargaCrypto > saldoAdmin) {
					logCryp.logCrypBe.info("tidak dapat melakukan penjualan");
					responseDto.setFailed("Saldo admin kurang");
				} else {
					//penambahan saldo user
					logCryp.logCrypBe.info("cryptoUser:"+cryptoUser);
//			int user_id = cryptoUser.getUserId();
					saldoService.penambahanSaldoUser(hargaCrypto, userDetail.getUsername());
					//pengurangan saldo admin
					saldoService.penguranganSaldoAdmin(hargaCrypto);
					//penghapusan txn di portofolio
					cryptoUserService.deleteTxn(id, cryptoUser.getJumlah());
					//penambahan jumlah crypto
					String url = baseUrl + "crypto/plus/" +id;
					HttpHeaders headers = new HttpHeaders();
					headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
					HttpEntity<CryptoUser> entity = new HttpEntity<CryptoUser>(cryptoUser,headers);
					RestTemplate restTemplate = new RestTemplate();

					Object result = restTemplate.exchange(url, HttpMethod.PUT, entity, Object.class).getBody();
					return ResponseEntity.ok(result);
				}
			}
		}else{
			logCryp.logCrypBe.info("Jumlah melebihi");
			responseDto.setFailed("Jumlah melebihi");
		}

		return ResponseEntity.ok(responseDto);

	}
}
