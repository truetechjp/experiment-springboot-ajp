package com.example.demo;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class TomcatCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Value("${tomcat.ajp.address:127.0.0.1}")
	private String address;
	
	@Value("${tomcat.ajp.port:9009}")
	private int port;
	
	@Value("${tomcat.ajp.secretRequired:false}")
	private boolean secretRequired;
	
	@Override
	public void customize(TomcatServletWebServerFactory factory) {
		log.debug("address: {}", address);
		log.debug("port: {}", port);
		log.debug("secretRequired: {}", secretRequired);

		if (port == 0) {
			return;
		}

		try {
			InetAddress address = InetAddress.getByName(this.address);
			factory.setAddress(address);
		}
		catch (Exception e) {
			log.error("bad address", e);
		}

		factory.setProtocol("AJP/1.3");
		factory.setPort(port);

		factory.getTomcatConnectorCustomizers()
		.add(c -> c.setAttribute("secretRequired", secretRequired));
	}
}
