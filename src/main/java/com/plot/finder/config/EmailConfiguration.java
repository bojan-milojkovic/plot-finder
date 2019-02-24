package com.plot.finder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.security.GeneralSecurityException;
import java.util.Properties;

@Configuration
@PropertySource("classpath:application.properties")
public class EmailConfiguration {
	
	@Bean
	public JavaMailSender javaMailSender(Environment env) throws GeneralSecurityException {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost(env.getProperty("mail.host"));
	    mailSender.setPort(Integer.parseInt((env.getProperty("mail.port"))));
	     
	    mailSender.setUsername("bojan.milojkovic.83");
	    mailSender.setPassword("bmeugpnkrsqvzfbp");
	     
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", env.getProperty("mail.protocol"));
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.starttls.required", "true");
	    props.put("mail.debug", "true");
	    props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
	     
	    return mailSender;
	}

}
