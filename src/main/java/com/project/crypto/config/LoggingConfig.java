package com.project.crypto.config;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {
	public final org.slf4j.Logger logCrypBe = LoggerFactory.getLogger("crypApi");

}
