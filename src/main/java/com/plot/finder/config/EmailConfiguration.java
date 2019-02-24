package com.plot.finder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

@Configuration
@PropertySource("classpath:application.properties")
public class EmailConfiguration {
	
	@Bean
	Session session(Environment env){
		Properties props = System.getProperties();
	    props.put("mail.smtp.host", env.getProperty("email.smtpHostServer"));
	    props.put("mail.smtp.port", "587"); //TLS Port
		props.put("mail.smtp.auth", "true"); //enable authentication
		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

	    return Session.getInstance(props, new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(env.getProperty("email.fromEmail").split("@")[0], 
						env.getProperty("email.password"));
			}
		});
	}

}
