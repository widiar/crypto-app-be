package com.project.crypto.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.project.crypto.config.LoggingConfig;
import com.project.crypto.dto.RequestCrypto;
import com.project.crypto.model.Saldo;
import com.project.crypto.model.UserDetail;
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
	
	@PostMapping("/buy/{id}")
	public ResponseEntity<Object> buyCrypto(@PathVariable Integer id, @RequestBody RequestCrypto reqCrypto, 
			@RequestBody Saldo saldo, @AuthenticationPrincipal UserDetail userDetail) {
		Double saldoNasabah = saldo.getJumlah();
		Double hargaCrypto = reqCrypto.getHarga();
		if ((saldoNasabah - hargaCrypto) < 0) {
			logCryp.logCrypBe.info("Saldo tidak cukup");
			//return "saldo tidak cukup";
		} else {
			//penambahan saldo admin
			saldoService.updateSaldo(userDetail, hargaCrypto);
			//pengurangan saldo user
			saldoService.penguranganSaldo(userDetail, hargaCrypto);
			//update jumlah crypto
			String url = baseUrl + "crypto/cryp/" + id;
			HttpHeaders headers = new HttpHeaders();
	        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	        HttpEntity<RequestCrypto> entity = new HttpEntity<RequestCrypto>(reqCrypto,headers);
	        RestTemplate restTemplate = new RestTemplate();

	        Object result = restTemplate.exchange(url, HttpMethod.PUT, entity, Object.class).getBody();
	        return ResponseEntity.ok(result);
		}
		return null;
		
	}

}
